class MandelbrotInput {

    private Complex minC;
    private Complex maxC;

    private int width;
    private int height;

    private int maxNrOfIterations;

    Complex getMinC() {
        return minC;
    }

    void setMinC(Complex minC) {
        this.minC = minC;
    }

    Complex getMaxC() {
        return maxC;
    }

    void setMaxC(Complex maxC) {
        this.maxC = maxC;
    }

    int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    int getMaxNrOfIterations() {
        return maxNrOfIterations;
    }

    void setMaxNrOfIterations(int maxNrOfIterations) {
        this.maxNrOfIterations = maxNrOfIterations;
    }
}
