import finite_states.agents.GoalBasedAgent;
import finite_states.frontiers.MinHeap;
import finite_states.problems.KSquaredPuzzle;
import finite_states.problems.Problem;

public class Main {
    public static void main(String [ ] args) {
        Problem kSquaredProblem = new KSquaredPuzzle("3x3 puzzle", 3);
        GoalBasedAgent simpleAgent = new GoalBasedAgent(kSquaredProblem, MinHeap.class);
        simpleAgent.depth_limit = 25;
        System.out.println(simpleAgent.solutionToString());
    }
}
