package problems;

import junit.framework.TestCase;
import problem_elements.State;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class SudokuTest extends TestCase {
    public final int n = 4;
    public final int k = 2;

    public final static String PUZZLE_TEST_NAME = "test_sudoku";
    public final Sudoku sudoku = new Sudoku(PUZZLE_TEST_NAME, n);

    public void testGoalState() throws NoSuchFieldException {
        final int[][] puzzle = new int[n][n];
        final Integer[] numbers = IntStream.rangeClosed(1, n).boxed().toArray(Integer[]::new);

        puzzle[0] = Arrays.stream(numbers).mapToInt(i -> i).toArray();
        puzzle[1] = Arrays.stream(numbers).mapToInt(i -> i).toArray();
        puzzle[2] = Arrays.stream(numbers).mapToInt(i -> i).toArray();
        puzzle[3] = Arrays.stream(numbers).mapToInt(i -> i).toArray();

        State goal_state = sudoku.new SudokuState(puzzle, new boolean[n][n]);
        assertFalse(sudoku.isGoal(goal_state));

        puzzle[0] = Arrays.stream(numbers).mapToInt(i -> i).toArray();
        Collections.rotate(Arrays.asList(numbers), 2);
        puzzle[1] = Arrays.stream(numbers).mapToInt(i -> i).toArray();
        Collections.rotate(Arrays.asList(numbers), 3);
        puzzle[2] = Arrays.stream(numbers).mapToInt(i -> i).toArray();
        Collections.rotate(Arrays.asList(numbers), 2);
        puzzle[3] = Arrays.stream(numbers).mapToInt(i -> i).toArray();

        goal_state = sudoku.new SudokuState(puzzle, new boolean[n][n]);
        assertTrue(sudoku.isGoal(goal_state));

        // TODO: remove me when `buildRandomState` is correctly implemented.
        final Sudoku bigger_sudoku = new Sudoku("big_test_sudoku", 9);
        assertFalse(bigger_sudoku.isGoal(bigger_sudoku.buildRandomState()));
    }
}
