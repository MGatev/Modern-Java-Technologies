package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableAlreadyExistsException extends RuntimeException {
    public BuildableAlreadyExistsException() {
        super("This buildable already exists!");
    }

    public BuildableAlreadyExistsException(String message) {
        super(message);
    }

    public BuildableAlreadyExistsException(String message, Exception e) {
        super(message, e);
    }
}
