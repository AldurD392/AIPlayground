package finite_states.problems;

import finite_states.State;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * A generic problem instance.
 *
 * Problem instances should subclass the generic `State`,
 * provide an initial state and an objective state.
 */
public abstract class Problem {

    /**
     * A human readable name for this problem.
     */
    @NotNull
    public final String name;

    /**
     * A set of objective goals.
     */
    public final @NotNull Set<State> goals;

    protected Problem(@NotNull String name) {
        this.name = name;
        this.goals = new HashSet<>();
    }

    /**
     * Subclasses must override this method in order to allow generating new problem instances
     * in form of initial states.
     * They could also make sure that the returned state leads to a feasible solution.
     *
     * @return A new state.
     */
    @NotNull
    public abstract State buildRandomState();
}
