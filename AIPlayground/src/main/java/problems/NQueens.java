package problems;

import problem_elements.Action;
import problem_elements.State;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * The famous N-Queens problem.
 * A solution for this problem is a
 */
public class NQueens extends Problem implements Utility {

    /**
     * The number of queens in play.
     */
    public final int n;

    /**
     * The possible actions, for each puzzle instance, are finite.
     * Generate them once and for all.
     */
    private final NQueensAction[][] possible_actions;

    public NQueens(@NotNull String name, int n) {
        super(name);

        assert n > 0;
        this.n = n;

        possible_actions = new NQueensAction[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                possible_actions[i][j] = new NQueensAction(
                        String.format("Queen #%d in row %d", j, i), i, j
                );
            }
        }
    }

    @Override
    public boolean isGoal(@NotNull State state) {
        if (!(state instanceof NQueensState)) {
            assert false;  // This is a mistake, alert the developer.
            return false;
        }
        NQueensState qState = (NQueensState)state;

        // Check that the queen is on a different row.
        if (Arrays.stream(qState.positions).distinct().count() < n) {
            return false;
        }

        // Be sure that queens are on different diagonal segments.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(qState.positions[i] - qState.positions[j]) == j - i) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public @NotNull State buildRandomState() {
        return new NQueensState();
    }

    /**
     * Return 1 if the provided state is a solution.
     * Otherwise return a float greater or equal to 0 that reflects
     * how many queens attack each other.
     *
     * @param state The state to be evaluated.
     * @return 1 on solution, something in [0, 1) otherwise.
     */
    @Override
    public float score(@NotNull State state) {
        if (!(state instanceof NQueensState)) {
            assert false;  // This is a mistake, alert the developer.
            return 0;
        }
        NQueensState qState = (NQueensState)state;

        // Numbers of queens on the same row.
        int fighting_queens = (int)(n - Arrays.stream(qState.positions).distinct().count());

        // Count queens that lie on the same diagonal.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(qState.positions[i] - qState.positions[j]) == j - i) {
                    fighting_queens++;
                }
            }
        }

        return 1.0f - (fighting_queens * 2 / (n * (n - 1)));
    }

    /**
     * An action, for this problem, consists of moving a single queen through
     * her column on the chessboard, up to a specified row.
     */
    public class NQueensAction extends Action {

        /**
         * The index of the queen to be moved.
         */
        public final int column;

        /**
         * The index on which the queen will be moved.
         */
        public final int row;

        /**
         * An Action for the NQueens problem.
         *
         * @param name A human readable name for this action.
         * @param row The row to which selected queen will be moved.
         * @param column The column selecting the queen to be moved.
         */
        public NQueensAction(@NotNull String name, int row, int column) {
            super(name, 1);

            assert row < n;
            assert column < n;

            this.row = row;
            this.column = column;
        }
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
        public final int[] positions;

        /**
         * Generate a random configuration for the queens.
         */
        public NQueensState() {
            positions = new int[n];

            Integer[] numbers;
            numbers = IntStream.range(0, positions.length)
                    .boxed().toArray(Integer[]::new);
            Collections.shuffle(Arrays.asList(numbers));
            assert numbers.length == n;

            for (int i = 0; i < n; i++) {
                this.positions[i] = numbers[i];
            }
        }

        /**
         * Build a new puzzle, starting from the current one and applying the action.
         *
         * @param positions The current position of the queens.
         * @param action    The action to be performed.
         */
        private NQueensState(int[] positions, @NotNull Action action) {
            assert Arrays.stream(positions).max().orElse(n) < n;
            assert Arrays.stream(positions).min().orElse(-1) >= 0;
            assert action instanceof NQueensAction;

            NQueensAction qAction = (NQueensAction)action;
            this.positions = positions.clone();
            this.positions[qAction.column] = qAction.row;
        }

        @Override
        public @NotNull Iterable<Action> getActions() {
            final ArrayList<Action> actions = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (positions[j] != i) {
                        actions.add(possible_actions[i][j]);
                    }
                }
            }

            return actions;
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
