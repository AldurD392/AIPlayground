package exceptions;

/**
 * This exception is thrown on problems that can't be solved.
 */
public class UnsolvableProblem extends Exception {
    private static final long serialVersionUID = 42L;

    public UnsolvableProblem(String message) {
        super(message);
    }
}
