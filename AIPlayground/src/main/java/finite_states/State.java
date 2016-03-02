package finite_states;

import java.util.HashSet;

/**
 * A state represents an atomic configuration of the world.
 */
public interface State {
    /**
     * @return the list of available actions from the current state.
     */
    HashSet<Action> getActions();

    /**
     * From the current state, perform an action, into the next state.
     *
     * @param action: The action to be performed.
     * @return A new state.
     */
    State performAction(Action action);
}
