package exceptions;

/**
 * This exception is thrown on bad problem formulations.
 */
public class BadProblemFormulation extends Exception {
    public BadProblemFormulation(String message) {
        super(message);
    }
}
