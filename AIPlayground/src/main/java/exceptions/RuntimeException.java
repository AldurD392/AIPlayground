package exceptions;

/**
 * A generic exception happened at runtime.
 */
public class RuntimeException extends Exception {
    private static final long serialVersionUID = 01L;

    public RuntimeException(String message) {
        super(message);
    }
}
