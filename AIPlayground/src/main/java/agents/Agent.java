package agents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem_elements.State;
import problems.Problem;

import java.lang.invoke.MethodHandles;

public abstract class Agent {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

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
        logger.debug("Gathering initial state from problem instance...", problem.name);
        this.initial_state = problem.buildRandomState();
    }

    /**
     * Allow subclasses to add comments / print the solution.
     * Subclasses are invited to class `startToString()`.
     *
     * @return A string describing the solution.
     */
    @NotNull
    public abstract String solutionToString();

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
