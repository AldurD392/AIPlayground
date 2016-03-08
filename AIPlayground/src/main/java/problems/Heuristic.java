package problems;

import problem_elements.State;

/**
 * An heuristic.
 * Given a state, return a value greater or equal to 0.
 * The objective state should be the only one returning a value of 0.
 */
public interface Heuristic<T extends State> {

    /**
     * Return the heuristic value greater or equal to 0.
     *
     * @param state The state for which you're calculating the value.
     * @return A value greater or equal to 0.
     */
    float getHeuristicValue(T state);
}
