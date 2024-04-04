package bg.sofia.uni.fmi.mjt.cooking.exception;

import bg.sofia.uni.fmi.mjt.cooking.error.RecipeError;

public class InvalidRequestException extends Exception {
    public InvalidRequestException() {
        super("Invalid Request has been made and the reason is unknown.");
    }

    public InvalidRequestException(RecipeError rError) {
        super("Invalid Request has been made. The reason is" + rError.message());
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
