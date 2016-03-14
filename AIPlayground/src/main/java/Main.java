import agents.csp.BackTrackerAgent;
import problems.NQueens;
import problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String[] args) throws InvalidClassException {
        Problem problem = new NQueens("8 Queens puzzle", 8);
        BackTrackerAgent agent = new BackTrackerAgent(problem);
        System.out.println(agent.solutionToString());
    }
}