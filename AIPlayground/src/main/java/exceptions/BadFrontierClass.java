package exceptions;

/**
 * Thrown if trying an error occurs while instantiating the Frontier.
 */
public class BadFrontierClass extends RuntimeException {
    private static final long serialVersionUID = 43L;

    public BadFrontierClass(String message) {
        super(message);
    }
}
