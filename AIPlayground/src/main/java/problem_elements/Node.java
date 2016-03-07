package problem_elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Node implements Comparable<Node> {
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
    public final @Nullable Action arriving_action;

    /**
     * Count the depth of this node from the root.
     */
    public final int depth;

    /**
     * The cost of the path from the root to this node.
     */
    public final float path_cost;

    /**
     * The weight of this node.
     * By default, it is equal to the `path_cost`.
     */
    public float weight;

    /**
     * Build a node, given a state, a parent node and an action (leading here from the parent).
     *
     * @param state A state for this node.
     * @param parent A parent for this node.
     * @param arriving_action An action leading from the parent to this node.
     */
    public Node(@NotNull State state, @Nullable Node parent, @Nullable Action arriving_action) {
        this.state = state;
        this.parent = parent;
        this.arriving_action = arriving_action;

        assert (parent == null && arriving_action == null) || (parent != null && arriving_action != null);
        if (parent != null) {
            this.depth = parent.depth + 1;
            this.path_cost = parent.path_cost + arriving_action.cost;
        } else {
            this.depth = 0;
            this.path_cost = 0;
        }

        this.weight = this.path_cost;
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

    @Override
    public int compareTo(@NotNull Node o) {
        return Float.compare(this.weight, o.weight);
    }
}
