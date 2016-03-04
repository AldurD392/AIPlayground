package finite_states.frontiers;

import finite_states.Node;
import org.jetbrains.annotations.Nullable;

/**
 * Last-in-first-out frontier.
 *
 * Ignore already contained states.
 */
public class LIFO extends Queue {
    @Nullable
    public Node pick() {
        if (!this._frontier.isEmpty()) {
            Node node = this._frontier.remove(0);
            this.remove(node);
            return node;
        }

        return null;
    }
}
