package agents.finite_states;

import agents.Agent;
import exceptions.RuntimeException;
import exceptions.UnsolvableProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem_elements.Action;
import problem_elements.State;
import problems.Problem;

import java.lang.invoke.MethodHandles;

/**
 * A generic agent working against a world where possible state have a limited number.
 */
public abstract class FiniteStateAgent extends Agent {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    /**
     * Keep track of the initial state of the problem.
     */
    public final @NotNull State initial_state;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     */
    public FiniteStateAgent(@NotNull Problem problem) {
        super(problem);

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
    public abstract @Nullable Action nextAction() throws UnsolvableProblem, RuntimeException;

    /**
     * Format the sequence of actions to the solution as a string.
     *
     * @return The sequence of actions to the solution as a string.
     */
    @NotNull
    @Override
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

}
