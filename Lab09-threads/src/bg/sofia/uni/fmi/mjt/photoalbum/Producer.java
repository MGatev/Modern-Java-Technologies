package bg.sofia.uni.fmi.mjt.photoalbum;

import java.nio.file.Path;

public class Producer extends Thread {
    ImageConverter imageConverter;

    Path dir;

    public Producer(ImageConverter imageConverter, Path dir) {
        this.imageConverter = imageConverter;
        this.dir = dir;
    }

    @Override
    public void run() {
        imageConverter.loadImage(dir);
    }
}
