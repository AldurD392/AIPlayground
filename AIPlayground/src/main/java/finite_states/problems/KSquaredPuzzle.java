package finite_states.problems;

import finite_states.Action;
import finite_states.State;
import finite_states.heuristics.Heuristic;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.IntStream;

public class KSquaredPuzzle extends Problem implements Heuristic {
    /**
     * The dimension of the puzzle.
     */
    public final int k;

    /**
     * Swap 'empty' with the number above.
     */
    public final static Action UP = new Action("Up");

    /**
     * Swap 'empty' with the number below.
     */
    public final static Action DOWN = new Action("Down");

    /**
     * Swap 'empty' with the number on its left.
     */
    public final static Action LEFT = new Action("Left");

    /**
     * Swap 'empty' with the number on its right.
     */
    public final static Action RIGHT = new Action("Right");

    /**
     * For performance, keep a fixed reference to the only goal state fot this puzzle.
     */
    private final State goal;

    public KSquaredPuzzle(@NotNull String name, int k) {
        super(name);

        assert k > 0;
        this.k = k;

        this.goal = new KSquaredState(false);
    }

    @Override
    public boolean isGoal(@NotNull State state) {
        return this.goal.equals(state);
    }

    @Override
    public @NotNull State buildRandomState() {
        assert this.k > 0;
        // TODO: make sure to return a solvable puzzle!
        return new KSquaredState(true);
    }

    /**
     * Calculate the Manhattan distance for a given value and its current position.
     *
     * @param value The value of interest.
     * @param current_position The current position of the value.
     * @return A value greater or equal to 0.
     */
    private int manhattan(int value, int current_position) {
        assert value != 0;
        final int delta = Math.abs((value - 1 - current_position));
        return (delta / k) + (delta % k);
    }

    @Override
    public float getHeuristicValue(State state) {
        assert state instanceof KSquaredState;
        final KSquaredState squaredState = (KSquaredState) state;
        return IntStream.range(0, squaredState.puzzle.length)
                .filter(i -> squaredState.puzzle[i] != 0)
                .map(i -> manhattan(squaredState.puzzle[i], i)).sum();
    }

    public class KSquaredState extends State {
        /**
         * Keep track of the puzzle.
         * For performance and convenience, represent it as a 1D array.
         *
         * A puzzle contains numbers from 1 to k - 1, representing the real game values.
         * 0 represents the 'void'.
         */
        public final int[] puzzle;

        /**
         * Keep track of the position of 'void'.
         */
        private int empty_i = -1;

        /**
         * Build a new puzzle.
         *
         * @param isRandom Whether the generated puzzle should be random or in a solution form.
         */
        public KSquaredState(boolean isRandom) {
            this.puzzle = new int[k * k];

            Integer[] numbers;
            if (isRandom) {
                numbers = IntStream.rangeClosed(0, (k * k) - 1).boxed().toArray(Integer[]::new);
                Collections.shuffle(Arrays.asList(numbers));
            } else {
                numbers = IntStream.rangeClosed(1, k * k).boxed().toArray(Integer[]::new);
                numbers[(k * k) - 1] = 0;
            }
            assert numbers.length == k * k;

            for (int i = 0; i < k * k; i++) {
                this.puzzle[i] = numbers[i];

                if (numbers[i] == 0) {
                    empty_i = i;
                }
            }
        }

        /**
         * Build a new puzzle, cloning the current one and applying the action.
         *
         * @param puzzle The current puzzle.
         * @param empty_i The current position of the empty value.
         * @param action The new action.
         */
        private KSquaredState(int[] puzzle, int empty_i, @NotNull Action action) {
            assert empty_i != -1;

            this.empty_i = empty_i;
            this.puzzle = puzzle.clone();  // You can use 'clone()' as long as 'puzzle' is a 1D int array.

            assert this.getActions().contains(action);
            if (action == UP) {
                this.puzzle[this.empty_i] = puzzle[empty_i - k];
                this.puzzle[this.empty_i - k] = 0;
                this.empty_i -= k;
            } else if (action == DOWN) {
                this.puzzle[this.empty_i] = puzzle[empty_i + k];
                this.puzzle[this.empty_i + k] = 0;
                this.empty_i += k;
            } else if (action == LEFT) {
                this.puzzle[this.empty_i] = puzzle[empty_i - 1];
                this.puzzle[this.empty_i - 1] = 0;
                this.empty_i -= 1;
            } else if (action == RIGHT) {
                this.puzzle[this.empty_i] = puzzle[empty_i + 1];
                this.puzzle[this.empty_i + 1] = 0;
                this.empty_i += 1;
            }
        }

        @Override
        public @NotNull HashSet<Action> getActions() {
            final HashSet<Action> actions = new HashSet<>();

            if (empty_i / k != 0) actions.add(UP);  // First row
            if (empty_i / k != k - 1) actions.add(DOWN);  // Last row
            if (empty_i % k != 0) actions.add(LEFT);  // First column
            if (empty_i % k != k - 1) actions.add(RIGHT);  // Last column

            return actions;
        }

        @Override
        public @NotNull State performAction(Action action) {
            return new KSquaredState(this.puzzle, this.empty_i, action);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KSquaredState state = (KSquaredState)o;
            return Arrays.equals(this.puzzle, state.puzzle);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.puzzle);
        }

        @Override
        public String toString() {
            return Arrays.toString(this.puzzle);
        }
    }
}
