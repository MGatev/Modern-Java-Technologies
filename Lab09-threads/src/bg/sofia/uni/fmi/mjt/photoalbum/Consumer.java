package bg.sofia.uni.fmi.mjt.photoalbum;

public class Consumer extends Thread {
    private final ImageConverter imageConverter;

    public Consumer(ImageConverter imageConverter) {
        this.imageConverter = imageConverter;
    }

    @Override
    public void run() {
        imageConverter.convertToBlackAndWhite();
    }
}
