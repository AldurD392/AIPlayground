package problems;

import csp.CSP;
import csp.Constraint;
import csp.Variable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import problem_elements.Action;
import problem_elements.State;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Sudoku game implementation.
 */
public class Sudoku extends Problem implements
        CSPEncoding<Integer> {

    /**
     * The matrix length.
     */
    public final int n;

    /**
     * All the possible actions.
     */
    @NotNull
    public final ArrayList<ArrayList<ArrayList<SudokuAction>>> all_actions;

    /**
     * Generate a new sudoku problem, whose matrix length is `n`.
     *
     * @param name The name of the problem.
     * @param n The length of the sudoku as matrix.
     */
    public Sudoku(@NotNull String name, int n) {
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

    /**
     * Helper method to build a binary all_diff constraint regarding provided variables.
     *
     * @return The created constraints.
     */
    @NotNull
    private HashSet<Constraint<Integer>> allDiffToBinary(List<Variable<Integer>> variables) {
        final int n = variables.size();
        final HashSet<Constraint<Integer>> constraints = new HashSet<>(n * (n - 1) / 2);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                final ArrayList<Variable<Integer>> constrained_variables = new ArrayList<>(2);

                Variable<Integer> u = variables.get(i);
                Variable<Integer> v = variables.get(j);
                constrained_variables.add(u);
                constrained_variables.add(v);

                final Integer[][] allowed_values = new Integer[(n * (n - 1))][2];
                int row = 0;
                for (int k : u.domain) {
                    for (int l : v.domain) {
                        if (k == l) {
                            continue;
                        }

                        allowed_values[row][0] = k;
                        allowed_values[row][1] = l;
                        row += 1;
                    }
                }

                constraints.add(new Constraint<>(constrained_variables, allowed_values));
            }
        }

        return constraints;
    }

    /**
     * @param initial_state A possibly null initial state.
     * @return An encoding of the initial state as CSP.
     */
    @Override
    public CSP<Integer> asCSP(@Nullable State initial_state) {
        assert initial_state instanceof SudokuState;
        final SudokuState state = (SudokuState) initial_state;

        final List<Integer> defaultDomain = Arrays.asList(IntStream.range(1, n + 1)
                .boxed().toArray(Integer[]::new));

        final ArrayList<Variable<Integer>> variables = new ArrayList<>();
        for (int i = 0; i < n * n; i++) {
            if (!state.given_cells[i / n][i % n]) {
                variables.add(new Variable<>(String.format("%d,%d", i / n, i % n),
                        new HashSet<>(defaultDomain)));
            } else {
                final HashSet<Integer> domain = new HashSet<>(1);
                domain.add(state.puzzle[i / n][i % n]);
                variables.add(new Variable<>(String.format("%d,%d", i / n, i % n), domain));
            }
        }
        assert variables.size() == n * n;

        final HashSet<Constraint<Integer>> constraints = new HashSet<>();

        // Values on the rows have to be all distinct
        for (int i = 0; i < n; i++) {
            constraints.addAll(allDiffToBinary(variables.subList(i * n, (i + 1) * n)));
            assert variables.size() == n * n;
        }

        // Values on the columns have to be all distinct
        for (int i = 0; i < n; i++) {
            final int k = i;
            final ArrayList<Variable<Integer>> ith_column = IntStream.range(0, n)
                    .mapToObj(
                            j -> variables.get((j * n) + k)
                    ).collect(Collectors.toCollection(ArrayList::new));
            constraints.addAll(allDiffToBinary(ith_column));
        }

        // Values on the smaller matrices
        final int sqrt_n = (int) Math.sqrt((double) n);
        for (int i = 0; i < n; i++) {
            final int k = i;
            final ArrayList<Variable<Integer>> ith_matrix = IntStream.range(0, n)
                    .mapToObj(
                            j -> variables.get(
                                    (j / sqrt_n) * n + (k / sqrt_n) * (sqrt_n - 1) * n + (j % sqrt_n) + (k * sqrt_n)
                            )
                    ).collect(Collectors.toCollection(ArrayList::new));
            constraints.addAll(allDiffToBinary(ith_matrix));
        }

        return new CSP<>(variables, constraints);
    }

    @NotNull
    @Override
    public State stateFromCSP(@NotNull  List<Variable<Integer>> assignment) {
        assert assignment.size() == n * n;

        final int[][] puzzle = new int[n][n];
        for (int i = 0; i < n * n; i++) {
            Integer value = assignment.get(i).value;
            assert value != null;
            puzzle[i / n][i % n] = value;
        }

        // Given cells is empty, but at this point is not important anymore.
        return new SudokuState(puzzle, new boolean[n][n]);
    }

    @Override
    public @NotNull State buildRandomState() {
        // TODO: build a real random state.
        assert this.n == 9;

        final int[][] static_puzzle = new int[this.n][this.n];
        final boolean[][] static_filled_cells = new boolean[this.n][this.n];

        {
            // This is bad, we know! :)
            final HashMap<Map.Entry<Integer, Integer>, Integer> cells = new HashMap<>();
            cells.put(new AbstractMap.SimpleImmutableEntry<>(0, 2), 3);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(0, 4), 2);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(0, 6), 6);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(1, 0), 9);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(1, 3), 3);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(1, 5), 5);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(1, 8), 1);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(2, 2), 1);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(2, 3), 8);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(2, 5), 6);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(2, 6), 4);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(3, 2), 8);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(3, 3), 1);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(3, 5), 2);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(3, 6), 9);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(4, 0), 7);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(4, 8), 8);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(5, 2), 6);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(5, 3), 7);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(5, 5), 8);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(5, 6), 2);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(6, 2), 2);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(6, 3), 6);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(6, 5), 9);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(6, 6), 5);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(7, 0), 8);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(7, 3), 2);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(7, 5), 3);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(7, 8), 9);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(8, 2), 5);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(8, 4), 1);
            cells.put(new AbstractMap.SimpleImmutableEntry<>(8, 6), 3);

            for (Map.Entry<Map.Entry<Integer, Integer>, Integer> entry : cells.entrySet()) {
                int i = entry.getKey().getKey();
                int j = entry.getKey().getValue();

                static_puzzle[i][j] = entry.getValue();
                static_filled_cells[i][j] = true;
            }
        }

        return new SudokuState(static_puzzle, static_filled_cells);
    }

    @Override
    public boolean isGoal(@NotNull State state) {
        assert state instanceof SudokuState;
        SudokuState sudokuState = (SudokuState) state;

        final int sqrt_n = (int) Math.sqrt((double) n);
        return Arrays.stream(sudokuState.puzzle)  // Check for rows
                .filter(row -> Arrays.stream(row).filter(i -> i > 0).distinct().count() == n
                ).count() == n
                &&
                IntStream.range(0, n)  // Check for columns
                        .filter(i -> IntStream.range(0, n)
                                .map(j -> sudokuState.puzzle[j][i]).filter(k -> k > 0)
                                .distinct().count() == n
                        ).count() == n
                &&
                IntStream.range(0, n)  // Check for sub-matrices
                        .filter(matrix_index -> IntStream.range(0, n).filter(
                                row_column_index -> sudokuState.puzzle[
                                        (row_column_index / sqrt_n) + (matrix_index / sqrt_n) * sqrt_n
                                        ][
                                        (row_column_index % sqrt_n) + (matrix_index % sqrt_n) * sqrt_n
                                        ] > 0
                        ).distinct().count() == n
                ).count() == n;
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
        public final int[][] puzzle;

        /**
         * The sudoku already given cells.
         */
        @NotNull
        public final boolean[][] given_cells;

        /**
         * Generate a configuration for the sudoku using the input configuration.
         *
         * @param puzzle The given puzzle.
         * @param given_cells Unmodifiable cells.
         */
        public SudokuState(@NotNull int[][] puzzle, @NotNull boolean[][] given_cells) {
            assert puzzle.length == n;

            this.puzzle = new int[n][n];
            this.given_cells = new boolean[n][n];

            for (int i = 0; i < n; i++){
                for (int j = 0; j < n; j++){
                    this.puzzle[i][j] = puzzle[i][j];
                    this.given_cells[i][j] = given_cells[i][j];
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
            this(state.puzzle, state.given_cells);
            assert action instanceof SudokuAction;

            SudokuAction a = (SudokuAction) action;
            this.puzzle[a.row][a.column] = a.value;
            this.given_cells[a.row][a.column] = true;
        }

        @Override
        public @NotNull Iterable<Action> getActions() {
            final ArrayList<Action> actions = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {

                    if(!given_cells[i][j]){
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
            return Arrays.deepEquals(this.puzzle, state.puzzle);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(this.puzzle);
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder("\n");

            for (int i = 0; i < n; i++) {
                s.append(Arrays.toString(this.puzzle[i]));
                s.append("\n");
            }

            return s.toString();
        }
    }
}
