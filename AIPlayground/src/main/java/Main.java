import finite_states.frontiers.MinHeap;
import finite_states.agents.UtilityBasedAgent;
import finite_states.problems.KSquaredPuzzle;
import finite_states.problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String [ ] args) throws InvalidClassException {
        Problem kSquaredProblem = new KSquaredPuzzle("3x3 puzzle", 3);
        UtilityBasedAgent simpleAgent = new UtilityBasedAgent(kSquaredProblem, MinHeap.class);
        simpleAgent.cost_to_node = true;
        System.out.println(simpleAgent.solutionToString());
    }
}
