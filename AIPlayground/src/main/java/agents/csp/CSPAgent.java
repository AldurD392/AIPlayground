package agents.csp;

import agents.Agent;
import csp.CSP;
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

    /**
     * Hold the CSP encoding of the problem.
     */
    protected final @NotNull CSP<Object> csp;

    /**
     * Keep a reference to the problem, as a CSP encoding.
     */
    protected final @NotNull CSPEncoding<Object> csp_problem;

    public CSPAgent(@NotNull Problem problem) throws InvalidClassException {
        super(problem);

        if (!(problem instanceof CSPEncoding<?>)) {
            throw new InvalidClassException(
                    "Problems for the CSP agent need a CSP encoding.");
        }

        @SuppressWarnings("unchecked")  // We check it.
        final CSPEncoding<Object> csp_problem = (CSPEncoding<Object>) problem;
        this.csp_problem = csp_problem;
        this.csp = csp_problem.asCSP();
    }

    /**
     * Pre-process the definition of the problem in order to restrict the domains of the variables.
     *
     * @param k The maximum number of constrained variables to be considered at one time.
     * @throws UnsolvableProblem if the agent can't find a solution.
     */
    protected abstract void ensureKConsistency(int k) throws UnsolvableProblem;

    /**
     * After the pre-processing, solve the problem given the provided constraints and variable domains.
     *
     * @return The solution as a list of variables and assigned values.
     * @throws UnsolvableProblem if the agent can't find a solution.
     */
    protected abstract @NotNull List<Variable<Object>> solve() throws UnsolvableProblem;

    /**
     * Given a initial state and the encoding of the problem, find a solution.
     *
     * @return The solution as a list of variables and assigned values.
     * @throws UnsolvableProblem if the agent can't find a solution.
     */
    public @NotNull List<Variable<Object>> findSolution() throws UnsolvableProblem {
        this.ensureKConsistency(1);
        this.ensureKConsistency(2);
        return this.solve();
    }
}
