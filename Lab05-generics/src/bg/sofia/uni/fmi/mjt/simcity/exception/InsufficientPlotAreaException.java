package bg.sofia.uni.fmi.mjt.simcity.exception;

public class InsufficientPlotAreaException extends RuntimeException {
    public InsufficientPlotAreaException() {
        super("Insufficient Plot Area");
    }

    public InsufficientPlotAreaException(String message) {
        super(message);
    }

    public InsufficientPlotAreaException(String message, Exception e) {
        super(message, e);
    }
}
