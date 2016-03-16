package csp;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A constraint in the CSP representation of the problem.
 */
public class Constraint<T> {

    /**
     * An array of variables.
     */
    public final @NotNull HashMap<Variable<T>, Integer> variables;

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

        this.variables = new HashMap<>();
        for (int i = 0; i < variables.size(); i++) {
            this.variables.put(variables.get(i), i);
        }
        this.allowed_assignments = allowed_assignments;
    }

    /**
     * Return true if the assignment is consistent with this constraint.
     * @param assignment An assignment.
     * @return True if the assignment is consistent.
     */
    public boolean isConsistent(@NotNull List<Variable<T>> assignment) {
        if (assignment.parallelStream().filter(v -> (v.value != null)
                && variables.get(v) != null).count() == 0) {
            return true;
        }

        for (final T[] allowed_values : allowed_assignments) {
            boolean is_allowed = true;
            for (Variable<T> variable : assignment) {
                final Integer index = this.variables.get(variable);
                if (index == null) {
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
