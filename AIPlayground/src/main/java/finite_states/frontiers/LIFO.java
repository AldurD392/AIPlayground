package finite_states.frontiers;

import finite_states.Node;
import finite_states.State;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Last-in-first-out frontier.
 * Ignore already contained states.
 */
public class LIFO implements Frontier {
    private final HashSet<State> contained_states = new HashSet<State>();
    private final ArrayList<Node> _frontier = new ArrayList<Node>();

    public void add(@NotNull Node node) {
        if (contained_states.contains(node.state)) {
            return;
        }

        contained_states.add(node.state);
        _frontier.add(0, node);
    }

    public Node pick() {
        return this._frontier.isEmpty() ? null : this._frontier.remove(0);
    }
}
