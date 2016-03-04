package finite_states;

import exceptions.UnsolvableProblem;
import finite_states.frontiers.Frontier;
import finite_states.frontiers.MinHeap;
import finite_states.problems.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * An agent that explores a deterministic and completely known world, whose states are finite.
 * Its actions are based on a specific goal.
 */
public class GoalBasedAgent extends Agent {

    private static final Logger logger = LogManager.getLogger(Agent.class.getSimpleName());

    /**
     * Keep the number of explored states.
     */
    private int explored_states = -1;

    @NotNull
    private final Frontier frontier = new MinHeap();  // TODO: make me better.

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     */
    public GoalBasedAgent(@NotNull Problem problem) {
        super(problem);
    }

    @Override
    @Nullable
    public Action nextAction() throws UnsolvableProblem {
        /* On first run, this method will explore the possible states space in order to find a solution. */
        if (this.actions_sequence == null) {
            this.actions_sequence = searchSolutionInTree();

            if (this.actions_sequence == null) {
                throw new UnsolvableProblem("There is no way to arrive from the current state to the final state.");
            }
        }

        return !this.actions_sequence.isEmpty() ? this.actions_sequence.remove(0) : null;
    }

    /**
     * Explore the spaces tree in order to find a suitable solution to the problem.
     *
     * @return A (possibly null) sequence of actions, leading from the initial state to the goal.
     */
    protected @Nullable ArrayList<Action> searchSolutionInTree() {
        final Node initial_node = new Node(this.initial_state);
        frontier.add(initial_node);

        logger.info("Starting exploration from initial state: {}.", initial_node.state);
        final HashSet<State> explored = new HashSet<>();

        Node current_node;
        while ((current_node = frontier.pick()) != null) {
            final State current_state = current_node.state;
            logger.debug("Current state: {}.", current_state);

            // We have found a way to the objective state.
            if (this.problem.goals.contains(current_state))  {
                final ArrayList<Action> action_sequence = new ArrayList<>();

                Node node = current_node;
                while (node.parent != null) {
                    action_sequence.add(0, node.parent_action);
                    node = node.parent;
                }

                this.explored_states = explored.size();
                return action_sequence;
            }

            explored.add(current_node.state);
            for (Action a : current_state.getActions()) {
                final Node child = new Node(current_state.performAction(a), current_node, a);

                if (explored.contains(child.state)) {
                    continue;
                }

                frontier.add(child);
            }
        }

        this.explored_states = explored.size();
        assert explored.size() > 0;
        return null;
    }

    @Override
    public @Nullable String statsToString() {
        return String.format("Initial state: %s.\n", this.initial_state) +
                String.format("Number of explored states: %d.\n", this.explored_states);
    }
}
