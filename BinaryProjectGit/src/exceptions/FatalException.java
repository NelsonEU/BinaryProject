package exceptions;

public class FatalException extends RuntimeException {
    public FatalException() {
    }

    public FatalException(String message) {
        super(message);
    }
}
