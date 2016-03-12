package agents.iterative_enhancement;

import agents.Agent;
import exceptions.UnsolvableProblem;
import org.jetbrains.annotations.NotNull;
import problem_elements.State;
import problems.Problem;

/**
 * A generic iterative enhancement agent.
 */
public abstract class IterativeEnhancementAgent extends Agent {

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     */
    public IterativeEnhancementAgent(@NotNull Problem problem) {
        super(problem);
    }

    /**
     * Given the initial state, find a solution to the problem.
     *
     * @return The solution.
     * @throws UnsolvableProblem if the agent can't find a solution.
     */
    public abstract @NotNull State findSolution() throws UnsolvableProblem;

    @Override
    public @NotNull String solutionToString() {
        final StringBuilder output = new StringBuilder("\n");

        try {
            final State solution = this.findSolution();
            output.append(String.format("%s found a solution: ", this.getClass().getSimpleName()));
            output.append(solution.toString());
            output.append("\n");
        } catch (UnsolvableProblem e) {
            output.append(e.toString());
        }

        final String stats = this.statsToString();
        if (stats != null) {
            output.insert(1, stats + "\n");
        }

        return output.toString();
    }
}
