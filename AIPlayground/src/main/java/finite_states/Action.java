package finite_states;

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

    public Action(@NotNull String name, float cost) {
        this.name = name;
        this.cost = cost;
    }
}
