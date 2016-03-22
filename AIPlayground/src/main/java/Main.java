import agents.Agent;
import agents.csp.PropagatorAgent;
import problems.Problem;
import problems.Sudoku;

import java.io.InvalidClassException;

public class Main {
    public static void main(String[] args) throws InvalidClassException {
        Problem problem = new Sudoku("9x9 Sudoku", 9);
        Agent agent = new PropagatorAgent(problem);
        System.out.println(agent.solutionToString());
    }
}