package bg.sofia.uni.fmi.mjt.space.exception;

public class TimeFrameMismatchException extends RuntimeException {

    public TimeFrameMismatchException() {
        super("Invalid time frame!");
    }

    public TimeFrameMismatchException(String message) {
        super(message);
    }

    public TimeFrameMismatchException(String message, Throwable e) {
        super(message, e);
    }
}
