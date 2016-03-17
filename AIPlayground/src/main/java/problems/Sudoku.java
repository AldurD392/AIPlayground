package problems;

import csp.CSP;
import org.jetbrains.annotations.NotNull;
import problem_elements.Action;
import problem_elements.State;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Sudoku game implementation.
 */
public class Sudoku extends Problem implements
        CSPEncoding<Integer> {

    /**
     * The matrix lenght.
     */
    public final int n;

    /**
     * All the possible actions.
     */
    @NotNull
    public final ArrayList<ArrayList<ArrayList<SudokuAction>>> all_actions;

    protected Sudoku(@NotNull String name, int n) {
        super(name);

        assert n > 0;
        assert Math.pow((int) Math.sqrt((double) n), 2) == n;
        this.n = n;

        this.all_actions = new ArrayList<>(n);

        for (int i=0; i < n; i++){
            final ArrayList<ArrayList<SudokuAction>> row_actions = new ArrayList<>(n);
            for (int j=0; j < n; j++){
                final ArrayList<SudokuAction> column_actions = new ArrayList<>(n);
                for (int k=1; k <= n; k++){
                    column_actions.add(
                            new SudokuAction(
                                    String.format("(%d, %d) -> %d", i, j, k),
                                    i, j, k)
                    );
                }
                row_actions.add(column_actions);
            }
            all_actions.add(row_actions);
        }
    }

    @Override
    public @NotNull CSP<Integer> asCSP() {
        return null;
    }

    @Override
    public @NotNull State buildRandomState() {
        return null;
    }

    @Override
    public boolean isGoal(@NotNull State state) {
        return false;
    }

    /**
     * An action, for this problem, consists of put a number inside a cell.
     */
    public class SudokuAction extends Action {

        /**
         * The row of the cell.
         */
        public final int row;

        /**
         * The column of the cell.
         */
        public final int column;

        /**
         * The value to put inside the cell.
         */
        public final int value;

        /**
         * An Action for the Sudoku problem.
         *
         * @param name   A human readable name for this action.
         * @param row    The row of the selected cell.
         * @param column The column of the selected cell.
         * @param value  The value to put in the fixed cell.
         */
        public SudokuAction(@NotNull String name, int row, int column, int value) {
            super(name, 1);

            assert row < n && row >= 0;
            assert column < n && column >= 0;
            assert value <= n && value >= 0;

            this.row = row;
            this.column = column;
            this.value = value;
        }
    }

    /**
     * Represent a possible state for the world of this problem.
     */
    public class SudokuState extends State {

        /**
         * The sudoku matrix
         */
        @NotNull
        public final int[][] sudokuMatrix;

        /**
         * The sudoku already filled cells.
         */
        @NotNull
        public final boolean[][] filled_cells;

//        /**
//         * Generate a random configuration for the sudoku.
//         */
//        public SudokuState(int fixed_values) {
//            // TODO
//            sudokuMatrix = new int[n][n];
//
//            Integer[] numbers = IntStream.rangeClosed(1, sudokuMatrix.length)
//                    .boxed().toArray(Integer[]::new);
//            assert numbers.length == n;
//
//            for (int i = 0; i < n; i++) {
//                Collections.shuffle(Arrays.asList(numbers));
//                for (int j = 0; j < n; j++) {
//                    this.sudokuMatrix[i][j] = numbers[j];
//                }
//            }
//        }

        /**
         * Generate a configuration for the sudoku using the input configuration.
         */
        public SudokuState(@NotNull int[][] matrix, @NotNull boolean[][] filled_cells) {
            assert matrix.length == n;

            this.sudokuMatrix = new int[n][n];
            this.filled_cells = new boolean[n][n];

            for(int i=0; i < n; i++){
                for(int j=0; j < n; j++){
                    this.sudokuMatrix[i][j] = matrix[i][j];
                    this.filled_cells[i][j] = filled_cells[i][j];
                }
            }
        }

        /**
         * Build a new puzzle, starting from the current one and applying the action.
         *
         * @param state The current sudoku state.
         * @param action The action to be performed.
         */
        private SudokuState(@NotNull SudokuState state, @NotNull Action action) {
            this(state.sudokuMatrix, state.filled_cells);
            assert action instanceof SudokuAction;

            SudokuAction a = (SudokuAction) action;
            this.sudokuMatrix[a.row][a.column] = a.value;
            this.filled_cells[a.row][a.column] = true;
        }

        @Override
        public @NotNull Iterable<Action> getActions() {
            final ArrayList<Action> actions = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {

                    if(!filled_cells[i][j]){
                        actions.addAll(all_actions.get(i).get(j));
                    }

                }
            }

            return actions;
        }

        @Override
        public @NotNull State performAction(Action action) { return new SudokuState(this, action); }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SudokuState state = (SudokuState) o;
            return Arrays.deepEquals(this.sudokuMatrix, state.sudokuMatrix);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(this.sudokuMatrix);
        }

        @Override
        public String toString() {
            return Arrays.deepToString(this.sudokuMatrix);
        }
    }
}
