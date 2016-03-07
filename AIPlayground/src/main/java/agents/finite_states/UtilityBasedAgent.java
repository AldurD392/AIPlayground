package agents.finite_states;

import problem_elements.Node;
import agents.frontiers.Frontier;
import problems.Heuristic;
import problems.Problem;
import org.jetbrains.annotations.NotNull;

import java.io.InvalidClassException;

/**
 * An agent based on utility.
 * Modifies, according to some functions, the weight of the nodes that
 * are being processed, before adding them to the Frontier.
 * The agent represents the algorithms: Best-First Greedy, A*.
 */
public class UtilityBasedAgent extends GoalBasedAgent {

    /**
     * If true, the Agent will consider both the heuristic value and
     * the cost of arriving to a node while selecting the next node to explore.
     *
     * i.e. the agent will perform an A* based search.
     */
    public boolean cost_to_node = false;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem        The problem to be solved. This object must implement the Heuristic interface.
     * @param frontier_class The class of the Frontier to be used while searching.
     */
    public UtilityBasedAgent(@NotNull Problem problem, @NotNull Class<? extends Frontier> frontier_class) throws InvalidClassException {
        super(problem, frontier_class);

        if (!(problem instanceof Heuristic)) {
            throw new InvalidClassException("The problem must implement the Heuristic interface.");
        }
    }

    @Override
    protected @NotNull Node postProcessNode(Node node) {
        Heuristic problem = (Heuristic)this.problem;

        node.weight = problem.getHeuristicValue(node.state);
        if (cost_to_node) {
            node.weight += node.path_cost;
        }

        return node;
    }
}
