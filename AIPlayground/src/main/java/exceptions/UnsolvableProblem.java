package exceptions;

/**
 * This exception is thrown on problems that can't be solved.
 */
public class UnsolvableProblem extends Exception {
    public UnsolvableProblem(String message) {
        super(message);
    }
}
