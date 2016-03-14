package csp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Return true if the assignment is consistent with this constraint.
     * @param assignment An assignment.
     * @return True if the assignment is consistent.
     */
    public boolean isConsistent(@NotNull List<Variable<T>> assignment) {
        if (assignment.parallelStream().filter(v -> (v.value != null) && variables.contains(v)).count() == 0) {
            return true;
        }

        for (final T[] allowed_values : allowed_assignments) {
            boolean is_allowed = true;
            for (Variable<T> variable : assignment) {
                final int index = variables.indexOf(variable);
                if (index == -1) {
                    continue;
                }

                final T value = variable.value;
                if (value != null) {
                    is_allowed &= value.equals(allowed_values[index]);
                }
            }

            if (is_allowed) {
                return true;
            }
        }

        return false;
    }
}
