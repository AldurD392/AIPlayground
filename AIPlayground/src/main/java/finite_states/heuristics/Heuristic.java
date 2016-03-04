package finite_states.heuristics;

import finite_states.State;

/**
 * An heuristic.
 * Given a state, return a value greater or equal to 0.
 * The objective state should be the only one returning a value of 0.
 */
public interface Heuristic {

    /**
     * Return the heuristic value greater or equal to 0.
     *
     * @param state The state for which you're calculating the value.
     * @return A value greater or equal to 0.
     */
    float getHeuristicValue(State state);
}
