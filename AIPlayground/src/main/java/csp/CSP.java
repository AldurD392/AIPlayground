package csp;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A CS representation of the problem having both variables and constraints.
 */
public class CSP<T> {
    @NotNull
    public final Set<Variable<T>> variables;

    @NotNull
    public final Set<Constraint<T>> constraints;

    public CSP(@NotNull Set<Variable<T>> variables,
               @NotNull Set<Constraint<T>> constraints) {
        this.variables = variables;
        this.constraints = constraints;
    }
}
