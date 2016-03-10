package problems;

import org.jetbrains.annotations.NotNull;
import problem_elements.State;

/**
 * Allow problems to return encoding of their states.
 * This will allow a Genetic Agent to build joint genetic inheritance and crossover.
 */
public interface GeneticEncoding<T extends State, U> extends Utility<T> {

    /**
     * Return an encoding for this state.
     *
     * @param state The state to be encoded.
     * @return An encoding.
     */
    @NotNull
    U[] getEncoding(@NotNull T state);

    /**
     * Return a state from the encoding.
     *
     * @param encoding An encoding for this state.
     * @return A state from the encoding.
     */
    @NotNull
    T encode(@NotNull U[] encoding);

    /**
     * Return a mutation of the state.
     *
     * @param state The state to be mutated.
     * @return A mutation of the state.
     */
    @NotNull
    T mutate(@NotNull T state);
}
