import agents.iterative_enhancement.GeneticAgent;
import problems.NQueens;
import problems.Problem;

import java.io.InvalidClassException;

public class Main {
    public static void main(String[] args) throws InvalidClassException {
        Problem problem = new NQueens("4 Queens puzzle", 8);
        GeneticAgent agent = new GeneticAgent(problem, 20);
        agent.minimum_solution_score = 1.0f;
        System.out.println(agent.solutionToString());
    }
}