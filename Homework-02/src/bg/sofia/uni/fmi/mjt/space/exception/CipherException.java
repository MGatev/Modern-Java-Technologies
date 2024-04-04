package bg.sofia.uni.fmi.mjt.space.exception;

public class CipherException extends Exception {

    public CipherException() {
        super("Encrypt/Decrypt operation cannot be completed successfully");
    }

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable e) {
        super(message, e);
    }
}
