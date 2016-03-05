import finite_states.agents.GoalBasedAgent;
import finite_states.frontiers.MinHeap;
import finite_states.problems.NQueens;
import finite_states.problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String [ ] args) throws InvalidClassException {
        Problem problem = new NQueens("4 queens", 4);
        GoalBasedAgent agent = new GoalBasedAgent(problem, MinHeap.class);
        System.out.println(agent.solutionToString());
    }
}
