package exceptions;

/**
 * This exception is thrown on bad problem formulations.
 */
public class BadProblemFormulation extends Exception {
    private static final long serialVersionUID = 41L;

    public BadProblemFormulation(String message) {
        super(message);
    }
}
