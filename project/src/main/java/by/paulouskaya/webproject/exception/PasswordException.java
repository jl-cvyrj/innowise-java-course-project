package by.paulouskaya.webproject.exception;

public class PasswordException extends Exception {
    public PasswordException() {}
    public PasswordException(String message) {
        super(message);
    }
    public PasswordException(Throwable cause) {
        super(cause);
    }
    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}