package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableNotFoundException extends RuntimeException {
    public BuildableNotFoundException() {
        super("Cant find this buildable!");
    }

    public BuildableNotFoundException(String message) {
        super(message);
    }

    public BuildableNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
