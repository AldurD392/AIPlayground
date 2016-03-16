package agents.csp;

import csp.Variable;
import exceptions.UnsolvableProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import problems.Problem;

import java.io.InvalidClassException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An agent performing backtracking on the space of solutions.
 */
public class BackTrackerAgent extends CSPAgent {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    public BackTrackerAgent(@NotNull Problem problem) throws InvalidClassException {
        super(problem);
    }

    @Override
    public void ensureKConsistency(int k) throws UnsolvableProblem {}  // This agent does not perform pre-processing.

    /**
     * Backtrack on the space of the solutions.
     *
     * @return A solution, i.e. a consistent and complete assignment for every variable.
     */
    public @NotNull List<Variable<Object>> solve() throws UnsolvableProblem {
        List<Variable<Object>> assignment = new ArrayList<>(csp.variables);
        List<Set<Object>> domains = new ArrayList<>(csp.variables.size());
        domains.addAll(csp.variables.stream().map(v -> new HashSet<>(v.domain)).collect(Collectors.toList()));

        int assigned_variables = 0;  // We've set variables up to this index.
        while (!csp.isComplete(assignment)) {
            logger.debug(Arrays.toString(assignment.toArray()));
            Variable<Object> variable = assignment.get(assigned_variables);
            variable.value = null;

            Iterator<Object> iterator = variable.domain.iterator();
            while (iterator.hasNext()) {
                variable.value = iterator.next();
                logger.debug("Assigning variable: {}.", variable);
                logger.debug(Arrays.toString(assignment.toArray()));
                iterator.remove();

                if (!csp.isConsistent(assignment)) {
                    variable.value = null;
                    logger.debug("Value was not consistent.");
                } else {
                    logger.debug("Value was consistent.");
                    assigned_variables++;
                    break;
                }
            }

            if (variable.value == null) {
                variable.domain = new HashSet<>(domains.get(assigned_variables));
                assigned_variables--;

                if (assigned_variables == -1) {
                    throw new UnsolvableProblem("Given the provided constraints this problem is unsolvable.");
                }
            }
        }

        return assignment;
    }

    @Override
    public @NotNull String solutionToString() {
        final StringBuilder output = new StringBuilder("\n");

        try {
            final List<Variable<Object>> assignment = this.findSolution();
            output.append(String.format("%s found a solution: ", this.getClass().getSimpleName()));
            output.append(Arrays.toString(assignment.toArray()));
            // TODO: better print of assignment.
            output.append("\n");
        } catch (UnsolvableProblem e) {
            output.append(e.toString());
        }

        final String stats = this.statsToString();
        if (stats != null) {
            output.insert(1, stats + "\n");
        }

        return output.toString();
    }
}
