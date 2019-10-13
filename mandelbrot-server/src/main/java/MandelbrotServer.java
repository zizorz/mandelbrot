import spark.Request;
import static spark.Spark.*;

public class MandelbrotServer {

    public static void main(String[] args ) {
        int port = 8080;
        if (args.length < 1) {
        } else {
            port = Integer.parseInt(args[0]);
        }

        var server = new MandelbrotServer();
        server.start(port);
    }

    private void start(int port) {
        port(port);

        get("/mandelbrot/:min_c_re/:min_c_im/:max_c_re/:max_c_im/:x/:y/:inf_n", (req, res) -> {
            var input = parseRequest(req);
            var image = MandelbrotImageCreater.createImage(input);
            res.header("Content-Type", "application/octet-stream");
            var outputStream = res.raw().getOutputStream();
            outputStream.flush();
            outputStream.write(image);
            return res.raw();
        });
    }

    private static MandelbrotInput parseRequest(Request req) {
        var input = new MandelbrotInput();

        var minCReal = Float.parseFloat(req.params(":min_c_re"));
        var minCImaginary = Float.parseFloat(req.params(":min_c_im"));
        var minC = new Complex(minCReal, minCImaginary);

        var maxCReal = Float.parseFloat(req.params(":max_c_re"));
        var maxCImaginary = Float.parseFloat(req.params(":max_c_im"));
        var maxC = new Complex(maxCReal, maxCImaginary);

        input.setMinC(minC);
        input.setMaxC(maxC);

        input.setWidth(Integer.parseInt(req.params(":x")));
        input.setHeight(Integer.parseInt(req.params(":y")));
        input.setMaxNrOfIterations(Integer.parseInt(req.params(":inf_n")));

        return input;
    }


}

