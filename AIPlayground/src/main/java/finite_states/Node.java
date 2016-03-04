package finite_states;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Node {
    /**
     * A node refers to a specific state.
     */
    @NotNull
    public final State state;

    /**
     * A node may have a parent node.
     */
    @Nullable
    public final Node parent;

    /**
     * A node may have an `Action`, connecting it to the `parent`.
     */
    @Nullable
    public final Action parent_action;

    /**
     * Count the depth of this node from the root.
     */
    public final int depth;

    /**
     * The weight of the path from the root to this node.
     */
    public final float weight;

    /**
     * Build a node, given a state, a parent node and an action (leading here from the parent).
     *
     * @param state A state for this node.
     * @param parent A parent for this node.
     * @param parent_action An action leading from the parent to this node.
     */
    public Node(@NotNull State state, @Nullable Node parent, @Nullable Action parent_action) {
        this.state = state;
        this.parent = parent;
        this.parent_action = parent_action;

        assert (parent == null && parent_action == null) || (parent != null && parent_action != null);
        if (parent != null) {
            this.depth = parent.depth + 1;
            this.weight = parent.weight + parent_action.cost;
        } else {
            this.depth = 0;
            this.weight = 0;
        }
    }

    public Node(@NotNull State state) {
        this(state, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;
        return state.equals(node.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}
