package finite_states.frontiers;

import finite_states.Node;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A frontier is a data structure, used by agents while searching for solutions.
 * Invariant: at any time, there is at most a single instance per different node.
 */
public interface Frontier {
    /**
     * Add a new node to the frontier.
     * If the node is already there, the implementation may choose according to its own policies.
     * e.g. it may ignore the new node or replace the old one.
     *
     * @param node: the node to be added.
     */
    void add(@NotNull Node node);

    /**
     * Return and remove a Node from the frontier.
     * @return A Node from the frontier.
     */
    @Nullable
    Node pick();
}
