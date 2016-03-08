package problems;

import org.jetbrains.annotations.NotNull;
import problem_elements.State;

/**
 * A utility function.
 * Given a state, evaluate its goodness.
 */
public interface Utility<T extends State> {
    /**
     * Given a state return a score, between 0 and 1.
     *
     * @param state The state to be evaluated.
     * @return A float in [0, 1].
     */
    float score(@NotNull T state);
}
