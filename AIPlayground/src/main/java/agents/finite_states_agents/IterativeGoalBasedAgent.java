package agents.finite_states_agents;

import exceptions.BadFrontierClass;
import exceptions.UnsolvableProblem;
import problem_elements.Action;
import agents.frontiers.Frontier;
import problems.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;

/**
 * An agent that, iteratively, tries looking for solutions
 * at greater depth.
 * The agent represents the algorithms: Limited-DFS, Limited-BFS, Iterative-Deepening-DFS, Iterative-Deepening-BFS.
 */
public class IterativeGoalBasedAgent extends GoalBasedAgent {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    /**
     * Do not try trees greater than this value.
     */
    public int max_depth_increase = Integer.MAX_VALUE;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem        The problem to be solved.
     * @param frontier_class The class of the Frontier to be used while searching.
     */
    public IterativeGoalBasedAgent(@NotNull Problem problem, @NotNull Class<? extends Frontier> frontier_class) {
        super(problem, frontier_class);
    }

    @Override
    public @Nullable Action nextAction() throws UnsolvableProblem, BadFrontierClass {
        this.depth_limit = 0;

        while (true) {
            try {
                if (this.actions_sequence == null) {
                    logger.info("Trying exploration with depth: {}.", this.depth_limit);
                }

                return super.nextAction();
            } catch (UnsolvableProblem e) {
                this.depth_limit++;

                if (this.depth_limit >= max_depth_increase) {
                    throw new UnsolvableProblem("Unable to find a solution in selected depth range.");
                }
            }
        }
    }
}
