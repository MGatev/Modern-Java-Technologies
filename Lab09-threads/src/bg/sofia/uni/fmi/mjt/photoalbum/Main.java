package bg.sofia.uni.fmi.mjt.photoalbum;

public class Main {
    public static void main(String... args) {
        ParallelMonochromeAlbumCreator temp = new ParallelMonochromeAlbumCreator(5);

        temp.processImages("H:\\test", "H:\\saveTest");
    }
}
