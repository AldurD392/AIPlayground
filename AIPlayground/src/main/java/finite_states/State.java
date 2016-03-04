package finite_states;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * A state represents an atomic configuration of the world.
 */
public abstract class State {
    /**
     * @return the list of available actions from the current state.
     */
    @NotNull
    public abstract HashSet<Action> getActions();

    /**
     * From the current state, perform an action, into the next state.
     * If the given action is invalid, do nothing and return `this`.
     *
     * @param action: The action to be performed.
     * @return The next state.
     */
    @NotNull
    public abstract State performAction(Action action);

    /**
     * States are required to implement this method, in order to allow for comparisons.
     *
     * @param other Another object.
     * @return True on equality.
     */
    @Override
    public abstract boolean equals(Object other);

    /**
     * States are required to implement this method, in order to allow them to be used inside Hash* data structures.
     * @return A hash.
     */
    @Override
    public abstract int hashCode();
}
