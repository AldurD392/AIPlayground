package finite_states.problems;

import finite_states.agents.Action;
import finite_states.State;
import junit.framework.TestCase;

import java.util.HashSet;

public class KSquaredPuzzleTest extends TestCase {

    public final int k = 4;
    public final static String PUZZLE_TEST_NAME = "test_puzzle";
    public final KSquaredPuzzle puzzle = new KSquaredPuzzle(PUZZLE_TEST_NAME, k);

    public void testGoalState() {
        assertEquals(puzzle.goals.size(), 1);
        for (State g : puzzle.goals) {
            assertEquals(g.getClass(), KSquaredPuzzle.KSquaredState.class);
            KSquaredPuzzle.KSquaredState squared = (KSquaredPuzzle.KSquaredState)g;

            int i;
            for (i = 0; i < (k * k) - 1; i++) {
                assertEquals(i + 1, squared.puzzle[i]);
            }

            assertEquals(squared.puzzle[i], 0);
        }
    }

    public void testGetActions() {
        KSquaredPuzzle.KSquaredState goal_state = (KSquaredPuzzle.KSquaredState) puzzle.goals.iterator().next();
        final HashSet<Action> actions = goal_state.getActions();

        assertEquals(actions.size(), 2);
        assertFalse(actions.contains(KSquaredPuzzle.DOWN));
        assertFalse(actions.contains(KSquaredPuzzle.RIGHT));
    }
}
