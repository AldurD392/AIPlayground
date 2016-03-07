package agents.frontiers;

import problem_elements.Node;
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
