import io.reactivex.rxjava3.schedulers.Schedulers;
import models.MandelbrotInput;
import models.Complex;
import models.ImageData;
import models.ImageWork;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class MandelbrotClient {

    private MandelbrotInput input;
    private LoadBalancer loadBalancer;
    private ImageWorkDivider imageWorkDivider;
    private ImageCreater imageCreater;
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
        this.loadBalancer = new RandomLoadBalancer();
        input.getServers().forEach(loadBalancer::addServer);
        this.imageWorkDivider = new ImageWorkDivider();
        this.imageCreater = new ImageCreater(input.getHeight(), input.getWidth());
        this.httpClient = HttpClient.newHttpClient();
    }

    private void run() {
        System.out.println("Dividing work on " + input.getServers().size() + " servers...");
        imageWorkDivider.DivideWork(input)
                .parallel()
                .runOn(Schedulers.io())
                .map(imageWork -> sendWork(imageWork, loadBalancer, httpClient))
                .runOn(Schedulers.computation())
                .doOnNext(imageCreater::addImagePart)
                .sequentialDelayError()
                .blockingSubscribe();

        System.out.println("Finished work. Writing image to disk...");

        var fileName = "output.pgm";
        try {
            imageCreater.imageToDisk(fileName);
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

    private static String constructUrl(String server, ImageWork imageWork) {
        return "http://" + server + "/mandelbrot" +
                "/" + imageWork.getMinC().getReal() +
                "/" + imageWork.getMinC().getImaginary() +
                "/" + imageWork.getMaxC().getReal() +
                "/" + imageWork.getMaxC().getImaginary() +
                "/" + imageWork.getImagePart().getWidth() +
                "/" + imageWork.getImagePart().getHeight() +
                "/" + imageWork.getMaxNrOfIterations();
    }

    private static ImageData sendWork(ImageWork imageWork, LoadBalancer loadBalancer, HttpClient httpClient) {
        var server = loadBalancer.getServer();
        var url = constructUrl(server, imageWork);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        byte[] bytes = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .join();

        var result = new ImageData();
        result.setBytes(bytes);
        result.setImagePart(imageWork.getImagePart());
        return result;
    }

}
