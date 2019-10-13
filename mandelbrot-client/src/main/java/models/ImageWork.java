package models;

public class ImageWork {

    private ImagePart imagePart;

    private Complex minC;
    private Complex maxC;

    private int maxNrOfIterations;

    public Complex getMinC() {
        return minC;
    }

    public void setMinC(Complex minC) {
        this.minC = minC;
    }

    public Complex getMaxC() {
        return maxC;
    }

    public void setMaxC(Complex maxC) {
        this.maxC = maxC;
    }

    public int getMaxNrOfIterations() {
        return maxNrOfIterations;
    }

    public void setMaxNrOfIterations(int maxNrOfIterations) {
        this.maxNrOfIterations = maxNrOfIterations;
    }

    public ImagePart getImagePart() {
        return imagePart;
    }

    public void setImagePart(ImagePart imagePart) {
        this.imagePart = imagePart;
    }
}
