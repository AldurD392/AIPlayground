package agents.csp;

import csp.Constraint;
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
 * An agent that performs GAC3 propagation on CSP problems, before solving them.
 */
public class PropagatorAgent extends BackTrackerAgent {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    public PropagatorAgent(@NotNull Problem problem) throws InvalidClassException {
        super(problem);
    }

    @Override
    public void ensureKConsistency(int k) throws UnsolvableProblem {
        logger.debug("Ensuring {}-consistency.", k);
        final List<AbstractMap.SimpleImmutableEntry<Variable<Object>, Constraint<Object>>>
                q = new ArrayList<>();

        for (Constraint<Object> c : this.csp.constraints) {
            q.addAll(c.variables.keySet().stream()
                    .map(v -> new AbstractMap.SimpleImmutableEntry<>(v, c))
                    .collect(Collectors.toList()));
        }
        final HashSet<AbstractMap.SimpleImmutableEntry<Variable<Object>, Constraint<Object>>> q_set =
                new HashSet<>(q);

        ListIterator<AbstractMap.SimpleImmutableEntry<Variable<Object>, Constraint<Object>>> it = q.listIterator();
        while (it.hasNext()) {
            final AbstractMap.SimpleImmutableEntry<Variable<Object>, Constraint<Object>> pair = it.next();
            it.remove();
            q_set.remove(pair);

            final Variable<Object> v = pair.getKey();
            final Constraint<Object> c = pair.getValue();
            if (this.removeNotConsistentValues(v, c, k)) {
                if (v.domain.isEmpty()) {
                    throw new UnsolvableProblem(
                            String.format("The domain of the variable %s has become empty.", v)
                    );
                }

                for (Constraint<Object> constraint : this.csp.constraints) {
                    if (constraint.equals(c)) {
                        continue;
                    }

                    for (Variable<Object> variable : constraint.variables.keySet()) {
                        final AbstractMap.SimpleImmutableEntry<Variable<Object>, Constraint<Object>> new_pair =
                                new AbstractMap.SimpleImmutableEntry<>(variable, constraint);
                        if (!(variable.equals(v)) && !(q_set.contains(new_pair))) {
                            it.add(new_pair);
                            it.previous();
                            q_set.add(new_pair);
                        }
                    }
                }
            }
        }
    }

    /**
     * Remove not consistent values from the domain of 'v', given the constraint 'c'.
     *
     * @param v A variable.
     * @param c A constraint.
     * @param k The maximum number of constrained variables to be considered at one time.
     * @return True if something was removed from the domain.
     */
    private boolean removeNotConsistentValues(Variable<Object> v, Constraint<Object> c, int k) {
        boolean removed = false;

        Iterator<Object> iterator = v.domain.iterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            if (!c.valueIsAllowed(v, value, k)) {
                iterator.remove();
                logger.debug("Removed values {} from domain of variable '{}' -> {}.",
                        value, v, Arrays.toString(v.domain.toArray()));
                removed = true;
            }
        }

        return removed;
    }
}
