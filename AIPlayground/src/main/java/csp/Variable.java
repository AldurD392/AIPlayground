package csp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A variable in the CSP representation of the problem.
 */
public class Variable<T> {

    /**
     * A human readable name for this variable.
     */
    @NotNull
    public final String name;

    /**
     * A value for this variable.
     * Null when the variable has no value.
     */
    @Nullable
    public T value;

    /**
     * The domain for this variable.
     */
    @NotNull
    final public Set<T> domain;

    public Variable(@NotNull String name, @NotNull Set<T> domain) {
        this.name = name;
        this.domain = domain;
    }

    @Override
    public String toString() {
        return String.format("Variable '%s' with value '%s'.",
                this.name, this.value);
    }
}
