package finite_states;

import exceptions.BadProblemFormulation;
import exceptions.UnsolvableProblem;
import finite_states.frontiers.Frontier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * An agent that explores a deterministic and completely known world, whose states are finite.
 */
public abstract class FiniteStatesAgent extends Agent {

    public Frontier frontier;  // TODO: make me better.

    public FiniteStatesAgent(@NotNull State current_state) throws BadProblemFormulation {
        super(current_state);
    }

    @Override
    public Action nextAction(State current_state) throws UnsolvableProblem {
        /* On first run, this method will explore the possible states space in order to find a solution. */
        if (this.actions_sequence == null) {
            this.actions_sequence = searchSolutionInTree();

            if (this.actions_sequence == null) {
                throw new UnsolvableProblem("There is no way to arrive from the current state to the final state");
            }
        }

        return this.actions_sequence.remove(0);
    }

    @Override
    public @Nullable State findGoal(State current_state) {
        return null;
    }

    /**
     * Explore the spaces tree in order to find a suitable solution to the problem.
     *
     * @return A (possibly null) sequence of actions, leading from the initial state to the goal.
     */
    protected @Nullable ArrayList<Action> searchSolutionInTree() {
        final Node initial_node = new Node(this.initial_state);
        final HashSet<State> explored = new HashSet<State>();

        frontier.add(initial_node);

        Node current_node;
        while ((current_node = frontier.pick()) != null) {
            final State current_state = current_node.state;

            // We have found a way to the objective state.
            if (current_state.equals(goal))  {
                final ArrayList<Action> action_sequence = new ArrayList<Action>();

                Node node = current_node;
                while (node.parent != null) {
                    action_sequence.add(0, node.parent_action);
                    node = node.parent;
                }

                return action_sequence;
            }

            explored.add(current_node.state);

            for (Action a : current_state.getActions()) {
                final Node child = new Node(current_state.performAction(a));

                if (explored.contains(child.state)) {
                    continue;
                }

                frontier.add(child);
            }
        }

        return null;
    }
}
