package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToDestinationException extends Exception {
    public NoPathToDestinationException() {
        super("There is no path to the desired location!");
    }

    public NoPathToDestinationException(String message) {
        super(message);
    }

    public NoPathToDestinationException(String message, Exception e) {
        super(message, e);
    }
}
