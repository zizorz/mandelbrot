import models.ImageData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

class ImageCreator {

    private volatile byte[][] image;
    private volatile AtomicLong nrOfAddedBytes;
    private long totalBytes;

    ImageCreator(int height, int width) {
        image = new byte[height][width];
        totalBytes = height * width;
        this.nrOfAddedBytes = new AtomicLong();
    }

    void addImagePart(ImageData imageData) {
        var height = imageData.getImagePart().getHeight();
        var width = imageData.getImagePart().getWidth();
        var topLeftY = imageData.getImagePart().getTopLeftY();
        var topLeftX = imageData.getImagePart().getTopLeftX();

        var bytes = imageData.getBytes();

        for(int i = 0 ; i < height; i++) {
            var bytesStartPos = i * width;
            System.arraycopy(bytes, bytesStartPos, image[topLeftY + i], topLeftX, width);
        }
        nrOfAddedBytes.addAndGet(width * height);
    }

    void imageToDisk(String filename) throws IOException {
        Path file = Paths.get(filename);
        Files.write(file, "P5 ".getBytes());
        var dimensions = image[0].length + " " + image.length + " ";
        Files.write(file, dimensions.getBytes(), StandardOpenOption.APPEND);
        Files.write(file, "255\n".getBytes(), StandardOpenOption.APPEND);
        for (byte[] bytes : image) {
            Files.write(file, bytes, StandardOpenOption.APPEND);
        }
    }

    int getProgress() {
        return (int)(((nrOfAddedBytes.get()) / (double)totalBytes) * 100);
    }
}
