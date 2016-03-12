package agents.iterative_enhancement;

import exceptions.UnsolvableProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem_elements.Action;
import problem_elements.State;
import problems.Problem;
import problems.Utility;

import java.io.InvalidClassException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * An Agent performing the Hill Climbing algorithm in order
 * to find a solution.
 */
public class HillClimberAgent extends IterativeEnhancementAgent {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    /**
     * Whether the agent should be allowed to perform lateral moves.
     */
    public boolean allow_lateral_moves = true;

    /**
     * The maximum numbers of steps the agent is allowed to perform.
     */
    public int maximum_steps = Integer.MAX_VALUE;

    /**
     * Perform an action as soon as a state with a better score is found.
     */
    public boolean is_greedy = true;

    /**
     * Count the number of steps executed.
     */
    private int steps = 0;

    /**
     * Keep track of current score.
     */
    private float current_score = -1;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     */
    public HillClimberAgent(@NotNull Problem problem) throws InvalidClassException {
        super(problem);

        if (!(problem instanceof Utility<?>)) {
            throw new InvalidClassException("The problem must implement the Utility interface.");
        }
    }

    @Override
    public @NotNull State findSolution() throws UnsolvableProblem {
        @SuppressWarnings("unchecked")  // We check it in the constructor.
                Utility<State> problem = (Utility<State>) this.problem;
        State current_state = this.problem.buildRandomState();
        this.current_score = problem.score(current_state);
        this.steps = 0;

        logger.debug("Starting climbing from state: {} ({}).", current_state, current_score);

        State best_state;
        do {
            best_state = null;

            // Actions to random list, to avoid determinism.
            List<Action> actions = StreamSupport
                    .stream(current_state.getActions().spliterator(), false)
                    .collect(Collectors.toList());
            Collections.shuffle(actions);

            Action chosen_action = null;
            for (Action a : actions) {
                final State next_state = current_state.performAction(a);
                final float score = problem.score(next_state);

                final float current_best = this.current_score;
                final Predicate<Float> isBetter;
                if (allow_lateral_moves) {
                    isBetter = f -> f >= current_best;
                } else {
                    isBetter = f -> f > current_best;
                }

                if (isBetter.test(score)) {
                    best_state = next_state;
                    this.current_score = score;
                    chosen_action = a;  // For logging/debugging purposes.

                    if (is_greedy) {
                        break;
                    }
                }
            }

            if (best_state != null) {
                // We found a better state/action
                assert chosen_action != null;
                current_state = best_state;
                logger.debug("Performing action '{}' to next state ({}).",
                        chosen_action.name, this.current_score);
            }

            this.steps++;
        } while (this.steps < this.maximum_steps && best_state != null);

        logger.debug("No better states found. The agent is at a (local) top.");
        return current_state;
    }

    @Override
    public @Nullable String statsToString() {
        return String.format("Performed %d actions.\n", this.steps) +
                String.format("Score of the final state: %f.\n", this.current_score);
    }
}
