package finite_states.frontiers;

import finite_states.Node;
import finite_states.State;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Generic frontier implemented as queue.
 * Do not add twice the same state.
 */
public abstract class Queue implements Frontier {
    private final HashSet<State> contained_states = new HashSet<>();
    protected final ArrayList<Node> _frontier = new ArrayList<>();

    public void add(@NotNull Node node) {
        if (contained_states.contains(node.state)) {
            return;
        }

        contained_states.add(node.state);
        _frontier.add(node);
    }

    /**
     * Subclasses MUST call this method before returning from 'pick'.
     *
     * @param node: The node about to be removed.
     */
    protected void remove(@NotNull Node node) {
        contained_states.remove(node.state);
    }
}
