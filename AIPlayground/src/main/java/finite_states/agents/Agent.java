package finite_states.agents;

import exceptions.BadFrontierClass;
import exceptions.RuntimeException;
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

public abstract class Agent {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    /**
     * A sequence of actions from the initial state to the goal.
     */
    @Nullable
    protected ArrayList<Action> actions_sequence;

    /**
     * An instance of the problem.
     */
    @NotNull
    protected final Problem problem;

    /**
     * Keep track of the initial state of the problem.
     */
    public final @NotNull State initial_state;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     */
    public Agent(@NotNull Problem problem) {
        this.problem = problem;
        logger.info("Creating new agent '{}' for problem named: '{}'.", this.getClass().getSimpleName(), problem.name);
        assert this.problem.goals.size() > 0;
        logger.debug("Gathering initial state from problem instance...", problem.name);
        this.initial_state = problem.buildRandomState();
    }

    /**
     * Return the next action to be performed by the agent.
     *
     * @throws UnsolvableProblem: If there is no way to arrive to the goal from the current state.
     * @throws RuntimeException: On a generic runtime error.
     * @return The next action to be performed or null if the agent has arrived to a solution.
     */
    @Nullable
    public abstract Action nextAction() throws UnsolvableProblem, RuntimeException;

    /**
     * Format the sequence of actions to the solution as a string.
     *
     * @return The sequence of actions to the solution as a string.
     */
    @NotNull
    public String solutionToString() {
        StringBuilder output = new StringBuilder("\n");

        try {
            int i = 1;

            Action next;
            while ((next = this.nextAction()) != null) {
                output.append(String.format("%d. %s.\n", i++, next.name));
            }
        } catch (UnsolvableProblem e) {
            output.append(e.toString());
        } catch (RuntimeException e) {
            return e.toString();
        }

        final String stats = this.statsToString();
        if (stats != null) {
            output.insert(1, stats + "\n");
        }

        return output.toString();
    }

    /**
     * Allow the agent to return a String of statistics, printed before the steps to the solution.
     *
     * @return A String of statistics or null (by default implementation).
     */
    @Nullable
    public String statsToString() {
        return null;
    }

    /**
     * An agent that explores a deterministic and completely known world, whose states are finite.
     * Its actions are based on a specific goal.
     */
    public static class GoalBasedAgent extends Agent {

        private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

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
                        action_sequence.add(0, node.parent_action);
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
}
