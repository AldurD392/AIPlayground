package csp;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * A CS representation of the problem having both variables and constraints.
 */
public class CSP<T> {
    @NotNull
    public final List<Variable<T>> variables;

    @NotNull
    public final Set<Constraint<T>> constraints;

    public CSP(@NotNull List<Variable<T>> variables,
               @NotNull Set<Constraint<T>> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }

    /**
     * Check if the given assignment is consistent and complete.
     *
     * @param assignment An (partial) assignment.
     * @return True if the assignment is consistent and complete.
     */
    public boolean isComplete(@NotNull List<Variable<T>> assignment) {
        return assignment.stream().filter(v -> v.value == null).count() == 0 &&
                this.isConsistent(assignment);
    }

    /**
     * Check if the given assignment is consistent.
     *
     * @param assignment An (partial) assignment.
     * @return True if the assignment is consistent.
     */
    public boolean isConsistent(@NotNull List<Variable<T>> assignment) {
        assert assignment.size() == variables.size();

        for (Constraint<T> constraint : constraints) {
            if (!constraint.isConsistent(assignment)) {
                return false;
            }
        }

        return true;
    }
}
