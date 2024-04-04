package bg.sofia.uni.fmi.mjt.photoalbum;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

public class ImageConverter {
    Queue<Image> list = new ArrayDeque<>();
    private final Path outPath;
    private boolean arePhotosLeft = false;

    public ImageConverter(Path outPath) {
        this.outPath = outPath;
    }

    public synchronized void setArePhotosLeft() {
        this.arePhotosLeft = true;
        this.notifyAll();
    }

    public void loadImage(Path imagePath) {
        try {
            BufferedImage imageData = ImageIO.read(imagePath.toFile());
            synchronized (this) {
                list.add(new Image(imagePath.getFileName().toString(), imageData));
                notifyAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to load image %s", imagePath), e);
        }
    }

    public void convertToBlackAndWhite() {
        Image image;
        while ((image = getImage()) != null) {
            BufferedImage processedData =
                new BufferedImage(image.data.getWidth(), image.data.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            processedData.getGraphics().drawImage(image.data, 0, 0, null);

            try {
                File newFile =
                    new File(outPath.toString() + "\\" + image.name);
                ImageIO.write(processedData, "JPG", newFile);
            } catch (IOException e) {
                throw new UncheckedIOException(String.format("Failed to save image %s", outPath), e);
            }
        }
    }

    private synchronized Image getImage() {
        while (list.isEmpty() && !arePhotosLeft) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected error occurred", e);
            }
        }
        return list.poll();
    }
}
