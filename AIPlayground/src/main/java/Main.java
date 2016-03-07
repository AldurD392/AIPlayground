import agents.finite_states_agents.UtilityBasedAgent;
import agents.frontiers.MinHeap;
import problems.KSquaredPuzzle;
import problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String [ ] args) throws InvalidClassException {
        Problem problem = new KSquaredPuzzle("3x3 puzzle", 3);
        UtilityBasedAgent agent = new UtilityBasedAgent(problem, MinHeap.class);
        agent.cost_to_node = true;  // A* baby!
        System.out.println(agent.solutionToString());
    }
}
