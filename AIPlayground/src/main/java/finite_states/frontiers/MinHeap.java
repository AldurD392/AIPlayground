package finite_states.frontiers;

import finite_states.Node;
import finite_states.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * MinHeap frontier.
 *
 * Extract nodes based on their priority.
 */
public class MinHeap implements Frontier {
    private final HashMap<State, Float> contained_states = new HashMap<>();
    private final PriorityQueue<Node> _frontier = new PriorityQueue<>();

    public void add(@NotNull Node node) {
        Float w = contained_states.get(node.state);

        if (w != null) {  // The frontier already contains this state.
            if (w > node.weight) {  // But it has a bigger value.
                // Remove it and insert it again.
                _frontier.remove(node);
                contained_states.remove(node.state);
            } else {
                // Nothing to do here.
                return;
            }
        }

        contained_states.put(node.state, node.weight);
        _frontier.add(node);
    }

    @Nullable
    public Node pick() {
        if (!this._frontier.isEmpty()) {
            final Node node = this._frontier.poll();
            this.contained_states.remove(node.state);
            return node;
        }

        return null;
    }
}
