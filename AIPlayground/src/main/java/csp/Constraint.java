package csp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A constraint in the CSP representation of the problem.
 */
public class Constraint<T> {

    /**
     * An array of variables.
     */
    @NotNull
    public final ArrayList<Variable<T>> variables;

    /**
     * A matrix of allowed assignments.
     *
     * The number of columns MUST be equal to the number of variables.
     * Each row encode a legal assignment.
     */
    @NotNull
    public final T[][] allowed_assignments;

    public Constraint(@NotNull ArrayList<Variable<T>> variables,
                      @NotNull T[][] allowed_assignments) {
        assert variables.size() > 0;
        assert allowed_assignments.length > 0;
        assert allowed_assignments[0].length == variables.size();

        this.variables = variables;
        this.allowed_assignments = allowed_assignments;
    }
}
