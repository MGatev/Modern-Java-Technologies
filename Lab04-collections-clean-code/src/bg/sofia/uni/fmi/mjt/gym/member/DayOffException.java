package bg.sofia.uni.fmi.mjt.gym.member;

public class DayOffException extends RuntimeException {
    public DayOffException() {
        super("This day is for rest!");
    }

    public DayOffException(String message) {
        super(message);
    }

    public DayOffException(String message, Exception e) {
        super(message, e);
    }
}
