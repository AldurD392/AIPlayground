import agents.csp.PropagatorAgent;
import problems.NQueens;
import problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String[] args) throws InvalidClassException {
        Problem problem = new NQueens("4 Queens puzzle", 4);
        PropagatorAgent agent = new PropagatorAgent(problem);
        System.out.println(agent.solutionToString());
    }
}