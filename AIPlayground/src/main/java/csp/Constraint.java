package csp;

import org.jetbrains.annotations.NotNull;

/**
 * A constraint in the CSP representation of the problem.
 */
public class Constraint<T> {

    /**
     * An array of variables.
     */
    @NotNull
    public final Variable<T>[] variables;

    /**
     * A matrix of allowed assignments.
     */
    @NotNull
    public final T[][] allowed_assignments;

    public Constraint(@NotNull Variable<T>[] variables,
                      @NotNull T[][] allowed_assignments) {
        assert variables.length > 0;
        assert allowed_assignments.length > 0;
        assert allowed_assignments[0].length == variables.length;

        this.variables = variables;
        this.allowed_assignments = allowed_assignments;
    }
}
