package bg.sofia.uni.fmi.mjt.gym;

public class GymCapacityExceededException extends Exception {
    public GymCapacityExceededException() {
        super("Max gym capacity reached");
    }

    public GymCapacityExceededException(String message) {
        super(message);
    }

    public GymCapacityExceededException(String message, Exception e) {
        super(message, e);
    }
}
