package models;

public class ImageData {

    private byte[] bytes;
    private ImagePart imagePart;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public ImagePart getImagePart() {
        return imagePart;
    }

    public void setImagePart(ImagePart imagePart) {
        this.imagePart = imagePart;
    }
}
