package bg.sofia.uni.fmi.mjt.photoalbum;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class ParallelMonochromeAlbumCreator implements MonochromeAlbumCreator {
    private final int imageProcessorsCount;

    public ParallelMonochromeAlbumCreator(int imageProcessorsCount) {
        this.imageProcessorsCount = imageProcessorsCount;
    }

    @Override
    public void processImages(String sourceDirectory, String outputDirectory) {
        Path sourceDir = Path.of(sourceDirectory);
        Path outDir = Path.of(outputDirectory);
        ImageConverter imageConverter = new ImageConverter(outDir);

        Collection<Thread> consumers = startConsumers(imageConverter);
        Collection<Thread> producers = startProducers(imageConverter, sourceDir);

        joinThreads(producers);

        imageConverter.setArePhotosLeft();

        joinThreads(consumers);
    }

    private Collection<Thread> startProducers(ImageConverter imageConverter, Path sourceDir) {
        Collection<Thread> producers = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, "*.{jpg, jpeg, png}")) {
            for (Path path : stream) {
                Producer temp = new Producer(imageConverter, path);
                producers.add(temp);
                temp.start();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Unexpected error with the file system occurred", e);
        }
        return producers;
    }

    private Collection<Thread> startConsumers(ImageConverter imageConverter) {
        Collection<Thread> consumers = new ArrayList<>();
        for (int i = 0; i < imageProcessorsCount; i++) {
            Consumer temp = new Consumer(imageConverter);
            consumers.add(temp);
            temp.start();
        }
        return consumers;
    }

    private void joinThreads(Collection<Thread> threads) {
        for (Thread current : threads) {
            try {
                current.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected error occurred", e);
            }
        }
    }
}
