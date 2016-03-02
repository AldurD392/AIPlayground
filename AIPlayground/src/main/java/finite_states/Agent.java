package finite_states;

import exceptions.BadProblemFormulation;
import exceptions.UnsolvableProblem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class Agent {
    @Nullable
    protected ArrayList<Action> actions_sequence;

    @NotNull
    protected State initial_state;

    @NotNull
    protected final State goal;

    // TODO: Problem formulation?

    public Agent(@NotNull State initial_state) throws BadProblemFormulation {
        this.initial_state = initial_state;

        final State goal = findGoal(initial_state);
        if (goal == null) {
            throw new BadProblemFormulation("Can't find goal state.");
        } else {
            this.goal = goal;
        }
    }

    /**
     * Return the next action to be performed by the agent.
     *
     * @param current_state: The current state of the agent.
     * @throws UnsolvableProblem: If there is no way to arrive to the goal from the current state.
     * @return The next action to be performed.
     */
    public abstract Action nextAction(State current_state) throws UnsolvableProblem;

    /**
     * Return the objective state.
     *
     * @param current_state: The current state.
     * @return the objective state.
     */
    @Nullable
    public abstract State findGoal(State current_state);
}
