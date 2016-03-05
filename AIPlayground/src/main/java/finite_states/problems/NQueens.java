package finite_states.problems;

import finite_states.Action;
import finite_states.State;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.IntStream;

/**
 * The famous N-Queens problem.
 * A solution for this problem is a
 */
public class NQueens extends Problem {

    /**
     * The number of queens in play.
     */
    public final int n;

    protected NQueens(@NotNull String name, int n) {
        super(name);

        assert n > 0;
        this.n = n;
    }

    @Override
    public boolean isGoal(@NotNull State state) {
        // TODO: implement me!
        return false;
    }

    @Override
    public @NotNull State buildRandomState() {
        return new NQueensState();
    }

    /**
     * Represent a possible state for the world of this problem.
     * i.e. a possible configuration of the queens on the chessboard.
     */
    public class NQueensState extends State {

        /**
         * An array keeping representing columns of the chessboard.
         * Values of this array represent the row of the i-th queen.
         */
        private final int[] positions;

        /**
         * Generate a random configuration for the queens.
         */
        public NQueensState() {
            positions = new int[n];

            Integer[] numbers;
            numbers = IntStream.rangeClosed(0, positions.length)
                    .boxed().toArray(Integer[]::new);
            Collections.shuffle(Arrays.asList(numbers));
            assert numbers.length == n;

            for (int i = 0; i < n * n; i++) {
                this.positions[i] = numbers[i];
            }
        }

        /**
         * Build a new puzzle, starting from the current one and applying the action.
         *
         * @param positions The current position of the queens.
         * @param action    The action to be performed.
         */
        public NQueensState(int[] positions, @NotNull Action action) {
            // TODO: Return a new state given specified action.
            this.positions = positions;
        }

        @Override
        public @NotNull HashSet<Action> getActions() {
            // TODO: return possible actions.
            return null;
        }

        @Override
        public @NotNull State performAction(Action action) {
            return new NQueensState(this.positions, action);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NQueensState state = (NQueensState) o;
            return Arrays.equals(this.positions, state.positions);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.positions);
        }

        @Override
        public String toString() {
            return Arrays.toString(this.positions);
        }
    }
}
