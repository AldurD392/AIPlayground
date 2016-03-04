package finite_states.frontiers;

import finite_states.Node;
import finite_states.agents.GoalBasedAgent;
import finite_states.heuristics.Heuristic;
import finite_states.problems.Problem;
import org.jetbrains.annotations.NotNull;

/**
 * An agent based on utility.
 * Modifies, according to some functions, the weight of the nodes that
 * are being processed, before adding them to the Frontier.
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
     * @param problem        The problem to be solved.
     * @param frontier_class The class of the Frontier to be used while searching.
     */
    public UtilityBasedAgent(@NotNull Problem problem, @NotNull Class<? extends Frontier> frontier_class) {
        super(problem, frontier_class);

        // TODO: change Problem to a better type!
        assert problem instanceof Heuristic;
    }

    @Override
    protected @NotNull Node postProcessNode(Node node) {
        Heuristic problem = (Heuristic)this.problem;
        float h = problem.getHeuristicValue(node.state);

        if (cost_to_node) {
            node.weight += h;
        } else {
            node.weight = h;
        }

        return node;
    }
}
