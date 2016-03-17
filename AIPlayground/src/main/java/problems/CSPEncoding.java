package problems;

import csp.CSP;
import csp.Variable;
import org.jetbrains.annotations.NotNull;
import problem_elements.State;

import java.util.List;

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

    /**
     * Allow problems to return a state from the variables they
     * selected for their CSP representation.
     *
     * @param assignment An assignment of variables.
     * @return A state.
     */
    @NotNull
    State stateFromCSP(@NotNull List<Variable<T>> assignment);
}
