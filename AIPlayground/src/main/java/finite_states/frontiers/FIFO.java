package finite_states.frontiers;

import finite_states.Node;
import finite_states.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * First-in-first-out frontier.
 * Ignore already contained states.
 */
public class FIFO implements Frontier {
    private final HashSet<State> contained_states = new HashSet<>();
    private final ArrayList<Node> _frontier = new ArrayList<>();

    public void add(@NotNull Node node) {
        if (contained_states.contains(node.state)) {
            return;
        }

        contained_states.add(node.state);
        _frontier.add(node);
    }

    @Nullable
    public Node pick() {
        return this._frontier.isEmpty() ? null : this._frontier.remove(0);
    }
}
