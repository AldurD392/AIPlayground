package agents.csp;

import agents.Agent;
import csp.Variable;
import exceptions.UnsolvableProblem;
import org.jetbrains.annotations.NotNull;
import problems.CSPEncoding;
import problems.Problem;

import java.io.InvalidClassException;
import java.util.List;

/**
 * A CSP based solver agent.
 */
public abstract class CSPAgent extends Agent {

    public CSPAgent(@NotNull Problem problem) throws InvalidClassException {
        super(problem);

        if (!(problem instanceof CSPEncoding<?>)) {
            throw new InvalidClassException(
                    "Problems for the CSP agent need a CSP encoding.");
        }
    }

    /**
     * Given a initial state and the encoding of the problem, find a solution.
     *
     * @return The solution as a list of variables and assigned values.
     * @throws UnsolvableProblem if the agent can't find a solution.
     */
    public abstract @NotNull List<Variable<Object>> findSolution() throws UnsolvableProblem;
}
