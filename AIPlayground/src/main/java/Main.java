import finite_states.agents.IterativeGoalBasedAgent;
import finite_states.frontiers.LIFO;
import finite_states.problems.KSquaredPuzzle;
import finite_states.problems.Problem;

public class Main {
    public static void main(String [ ] args) {
        Problem kSquaredProblem = new KSquaredPuzzle("3x3 puzzle", 3);
        IterativeGoalBasedAgent simpleAgent = new IterativeGoalBasedAgent(kSquaredProblem, LIFO.class);
        simpleAgent.max_depth_increase = 20;
        System.out.println(simpleAgent.solutionToString());
    }
}
