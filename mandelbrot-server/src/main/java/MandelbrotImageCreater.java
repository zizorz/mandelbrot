class MandelbrotImageCreater {

    private static final float INFINITY_BOUNDARY = 2;

    static byte[] createImage(MandelbrotInput input) {
        var height = input.getHeight();
        var width = input.getWidth();
        var image = new byte[height*width];

        var minC = input.getMinC();
        var maxC = input.getMaxC();

        var stepX = (maxC.getReal() - minC.getReal()) / width;
        var stepY = (maxC.getImaginary() - minC.getImaginary()) / height;

        var iterations = input.getMaxNrOfIterations();

        var x = minC.getReal();
        var y = maxC.getImaginary();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image[(width * i) + j] = calcPixelValue(new Complex(x, y), iterations);
                x += stepX;
            }
            x = minC.getReal();
            y -= stepY;
        }

        return image;
    }

    private static byte calcPixelValue(Complex c, int maxIterations) {
        var current = new Complex(0, 0);
        var i = 0;
        for (; i < maxIterations; i++) {
            current = current.square();
            current = current.add(c);
            if (current.abs() > INFINITY_BOUNDARY) {
                break;
            }
        }
        return (byte) (i % 256);
    }
}
