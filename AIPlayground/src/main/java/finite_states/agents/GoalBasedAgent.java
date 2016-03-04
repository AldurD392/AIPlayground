package finite_states.agents;

import exceptions.BadFrontierClass;
import exceptions.UnsolvableProblem;
import finite_states.Action;
import finite_states.Node;
import finite_states.State;
import finite_states.frontiers.Frontier;
import finite_states.problems.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * An agent that explores a deterministic and completely known world, whose states are finite.
 * Its actions are based on a specific goal.
 * The agent represents the algorithms: DFS, BFS, Min-Cost.
 */
public class GoalBasedAgent extends Agent {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    /**
     * A sequence of actions from the initial state to the goal.
     */
    protected @Nullable List<Action> actions_sequence;


    /**
     * Keep the number of explored states.
     */
    private long explored_states = -1;

    /**
     * The maximum tree-depth allowed while searching for solutions.
     */
    public int depth_limit = Integer.MAX_VALUE;

    @NotNull
    private final Class<? extends Frontier> frontier_class;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     * @param frontier_class The class of the Frontier to be used while searching.
     */
    public GoalBasedAgent(@NotNull Problem problem,
                          @NotNull Class<? extends Frontier> frontier_class) {
        super(problem);
        this.frontier_class = frontier_class;
    }

    /**
     * Instantiate a `Frontier` from the given class.
     *
     * @return A `Frontier` object.
     * @throws BadFrontierClass On bad `Frontier` class.
     */
    private Frontier instantiateFrontierFromClass() throws BadFrontierClass {
        try {
            return this.frontier_class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BadFrontierClass("Error while instantiating frontier.");
        }

    }

    @Override
    @Nullable
    public Action nextAction() throws UnsolvableProblem, BadFrontierClass {
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
     * Allow subclasses to perform custom modification on the node.
     *
     * @param node The node to be modified.
     * @return An instance of Node.
     */
    @NotNull
    protected Node postProcessNode(Node node) {
        return node;
    }

    /**
     * Explore the spaces tree in order to find a suitable solution to the problem.
     *
     * @return A (possibly null) sequence of actions, leading from the initial state to the goal.
     */
    protected @Nullable ArrayList<Action> searchSolutionInTree() throws BadFrontierClass {
        assert this.depth_limit >= 0;
        final Frontier frontier = this.instantiateFrontierFromClass();

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
                    action_sequence.add(0, node.arriving_action);
                    node = node.parent;
                }

                this.explored_states = explored.size();
                return action_sequence;
            }

            if (current_node.depth >= depth_limit) {
                continue;
            }

            explored.add(current_node.state);
            for (Action a : current_state.getActions()) {
                Node child = new Node(current_state.performAction(a), current_node, a);

                if (explored.contains(child.state)) {
                    continue;
                }

                child = this.postProcessNode(child);
                frontier.add(child);
            }
        }

        this.explored_states = explored.size();
        return null;
    }

    @Override
    public @Nullable String statsToString() {
        return String.format("Initial state: %s.\n", this.initial_state) +
                String.format("Number of explored states: %d.\n", this.explored_states);
    }
}
