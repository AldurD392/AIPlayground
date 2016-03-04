package finite_states;

import exceptions.RuntimeException;
import exceptions.UnsolvableProblem;
import finite_states.problems.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

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
    @NotNull
    public final State initial_state;

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
}
