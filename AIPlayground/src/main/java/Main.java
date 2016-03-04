import finite_states.Agent;
import finite_states.GoalBasedAgent;
import finite_states.problems.KSquaredPuzzle;
import finite_states.problems.Problem;

public class Main {
    public static void main(String [ ] args) {
        Problem kSquaredProblem = new KSquaredPuzzle("3x3 puzzle", 3);
        Agent simpleAgent = new GoalBasedAgent(kSquaredProblem);
        System.out.println(simpleAgent.solutionToString());
    }
}
