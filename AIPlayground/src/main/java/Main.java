import agents.iterative_enhancement.HillClimberAgent;
import problems.NQueens;
import problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String[] args) throws InvalidClassException {
        Problem problem = new NQueens("128 Queens puzzle", 128);
        HillClimberAgent agent = new HillClimberAgent(problem);
        agent.is_greedy = false;
        agent.allow_lateral_moves = true;
        agent.maximum_steps = (int) 10E4;
        System.out.println(agent.solutionToString());
    }
}