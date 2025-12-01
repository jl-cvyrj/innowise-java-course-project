package by.paulouskaya.webproject.exception;

public class ServletSalonException extends Exception {
    public ServletSalonException() {
    }

    public ServletSalonException(String message) {
        super(message);
    }

    public ServletSalonException(Throwable cause) {
        super(cause);
    }

    public ServletSalonException(String message, Throwable cause) {
        super(message, cause);
    }
}
