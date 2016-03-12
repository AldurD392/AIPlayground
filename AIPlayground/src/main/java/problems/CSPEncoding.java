package problems;

import csp.CSP;
import org.jetbrains.annotations.NotNull;

/**
 * A CSP encoding for the problem.
 */
public interface CSPEncoding<T> {

    /**
     * Allow problems to return a CSP representation.
     *
     * @return A CSP representation.
     */
    @NotNull
    CSP<T> asCSP();
}
