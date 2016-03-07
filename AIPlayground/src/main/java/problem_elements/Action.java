package problem_elements;

import org.jetbrains.annotations.NotNull;

/**
 * An action: agents perform actions to interact with the surrounding world.
 */
public class Action {
    /**
     * A human readable name for the action.
     */
    @NotNull
    public final String name;

    /**
     * A cost for performing the action.
     */
    public final float cost;

    /**
     * The simplest action.
     *
     * @param name A human readable name for this action.
     * @param cost A cost associated to the action.
     */
    public Action(@NotNull String name, float cost) {
        this.name = name;
        this.cost = cost;
    }

    /**
     * The simplest action.
     *
     * @param name A human readable name for this action.
     */
    public Action(@NotNull String name) {
        this.name = name;
        this.cost = 1;
    }
}
