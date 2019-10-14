import io.reactivex.rxjava3.schedulers.Schedulers;
import loadbalancer.LoadBalancer;
import loadbalancer.MandelbrotServer;
import loadbalancer.IdleQueueLoadBalancer;
import loadbalancer.Server;
import models.MandelbrotInput;
import models.Complex;
import models.ImageData;
import models.ImageWork;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.stream.Collectors;

class MandelbrotClient {

    private MandelbrotInput input;
    private LoadBalancer loadBalancer;
    private ImageWorkDivider imageWorkDivider;
    private ImageCreator imageCreator;
    private HttpClient httpClient;

    public static void main(String[] args) {
        System.out.println("Parsing arguments...");
        if (args.length < 9) {
            System.out.println("Too few arguments");
            return;
        }
        var arguments = parseArguments(args);

        System.out.println("Creating client...");
        var mandelbrotClient = new MandelbrotClient(arguments);
        mandelbrotClient.run();

        System.out.println("Done.");
    }

    private MandelbrotClient(MandelbrotInput input) {
        this.input = input;
        this.httpClient = HttpClient.newHttpClient();

        List<Server> servers = input.getServers().stream().map(host -> new MandelbrotServer(httpClient, host)).collect(Collectors.toList());
        this.loadBalancer = new IdleQueueLoadBalancer();
        loadBalancer.addServers(servers);

        this.imageWorkDivider = new ImageWorkDivider();
        this.imageCreator = new ImageCreator(input.getHeight(), input.getWidth());
    }

    private void run() {
        System.out.println("Dividing work on " + input.getServers().size() + " servers...");

        Runnable progressTask = () -> {
            for(;;) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println("Constructed approximately " + this.imageCreator.getProgress() + " % of the image.");
            }
        };

        var progressThread = new Thread(progressTask);
        progressThread.start();

        imageWorkDivider.DivideWork(input)
                .parallel(loadBalancer.getNrOfResources())
                .runOn(Schedulers.io())
                .map(imageWork -> sendWork(imageWork, loadBalancer, httpClient))
                .runOn(Schedulers.computation())
                .doOnNext(imageCreator::addImagePart)
                .sequentialDelayError()
                .blockingSubscribe();

        progressThread.interrupt();

        System.out.println("Finished work. Writing image to disk...");

        var fileName = "output.pgm";
        try {
            imageCreator.imageToDisk(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Image written to disk: " + fileName);
    }



    private static MandelbrotInput parseArguments(String[] args) {
        var arguments = new MandelbrotInput();
        var minC = new Complex(Float.parseFloat(args[0]), Float.parseFloat(args[1]));
        var maxC = new Complex(Float.parseFloat(args[2]), Float.parseFloat(args[3]));
        arguments.setMinC(minC);
        arguments.setMaxC(maxC);

        arguments.setMaxNrOfIterations(Integer.parseInt(args[4]));
        arguments.setWidth(Integer.parseInt(args[5]));
        arguments.setHeight(Integer.parseInt(args[6]));
        arguments.setDivisions(Integer.parseInt(args[7]));

        for (int i = 8; i < args.length; i++) {
            arguments.addServer(args[i]);
        }
        return arguments;
    }

    private static String constructPath(ImageWork imageWork) {
        return "mandelbrot" +
                "/" + imageWork.getMinC().getReal() +
                "/" + imageWork.getMinC().getImaginary() +
                "/" + imageWork.getMaxC().getReal() +
                "/" + imageWork.getMaxC().getImaginary() +
                "/" + imageWork.getImagePart().getWidth() +
                "/" + imageWork.getImagePart().getHeight() +
                "/" + imageWork.getMaxNrOfIterations();
    }

    private static ImageData sendWork(ImageWork imageWork, LoadBalancer loadBalancer, HttpClient httpClient) {
        var path = constructPath(imageWork);

        byte[] bytes = loadBalancer.sendGetRequest(path, (data) -> data);

        var result = new ImageData();
        result.setBytes(bytes);
        result.setImagePart(imageWork.getImagePart());
        return result;
    }

}
