package problems;

import junit.framework.TestCase;

public class NQueensTest extends TestCase {

    public final int n = 4;
    public final static String PROBLEM_TEST_NAME = "test_n_queens";
    public final NQueens queens_problem = new NQueens(PROBLEM_TEST_NAME, n);
    private final static double DELTA = 10E-5;

    public void testGoalState() throws NoSuchFieldException {
        NQueens.NQueensState state = (NQueens.NQueensState) queens_problem.buildRandomState();

        state.positions[0] = 1;
        state.positions[1] = 3;
        state.positions[2] = 0;
        state.positions[3] = 2;
        assertEquals(queens_problem.isGoal(state), true);

        state.positions[0] = 2;
        assertEquals(queens_problem.isGoal(state), false);

        state.positions[0] = 3;
        state.positions[1] = 0;
        state.positions[2] = 1;
        state.positions[3] = 2;
        assertEquals(queens_problem.isGoal(state), false);

        state.positions[0] = 1;
        state.positions[1] = 3;
        state.positions[2] = 1;
        state.positions[3] = 1;
        assertEquals(queens_problem.isGoal(state), false);

        state.positions[0] = 0;
        state.positions[1] = 1;
        state.positions[2] = 2;
        state.positions[3] = 3;
        assertEquals(queens_problem.isGoal(state), false);

        state.positions[0] = 3;
        state.positions[1] = 2;
        state.positions[2] = 1;
        state.positions[3] = 0;
        assertEquals(queens_problem.isGoal(state), false);

        state.positions[0] = 3;
        state.positions[1] = 1;
        state.positions[2] = 2;
        state.positions[3] = 0;
        assertEquals(queens_problem.isGoal(state), false);
    }

    public void testScore() throws NoSuchFieldException {
        NQueens.NQueensState state = (NQueens.NQueensState)queens_problem.buildRandomState();

        state.positions[0] = 1;
        state.positions[1] = 3;
        state.positions[2] = 0;
        state.positions[3] = 2;
        assertEquals(this.queens_problem.score(state), 1.0, DELTA);

        state.positions[0] = 2;
        state.positions[1] = 1;
        state.positions[2] = 2;
        state.positions[3] = 1;
        double fighting_queens = 5.0f;
        assertEquals(this.queens_problem.score(state), 1.0f - ((fighting_queens * 2) / (n * (n - 1))), DELTA);

        state.positions[0] = 2;
        state.positions[1] = 0;
        state.positions[2] = 2;
        state.positions[3] = 1;
        fighting_queens = 2.0f;
        assertEquals(this.queens_problem.score(state), 1.0f - ((fighting_queens * 2) / (n * (n - 1))), DELTA);

        state.positions[0] = 3;
        state.positions[1] = 2;
        state.positions[2] = 0;
        state.positions[3] = 1;
        fighting_queens = 2.0f;
        assertEquals(this.queens_problem.score(state), 1.0f - ((fighting_queens * 2) / (n * (n - 1))), DELTA);
    }
}