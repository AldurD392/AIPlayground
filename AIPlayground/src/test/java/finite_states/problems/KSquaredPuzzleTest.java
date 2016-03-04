package finite_states.problems;

import finite_states.Action;
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

    public void testHeuristic() {
        KSquaredPuzzle.KSquaredState goal_state = (KSquaredPuzzle.KSquaredState) puzzle.goals.iterator().next();
        assertEquals(puzzle.getHeuristicValue(goal_state), 0.0, 0.0);

        KSquaredPuzzle.KSquaredState new_state = (KSquaredPuzzle.KSquaredState)goal_state.performAction(KSquaredPuzzle.UP);
        assertEquals(puzzle.getHeuristicValue(new_state), 1.0, 0.0);

        new_state = (KSquaredPuzzle.KSquaredState)new_state.performAction(KSquaredPuzzle.UP);
        assertEquals(puzzle.getHeuristicValue(new_state), 2.0, 0.0);

        new_state = (KSquaredPuzzle.KSquaredState)new_state.performAction(KSquaredPuzzle.LEFT);
        assertEquals(puzzle.getHeuristicValue(new_state), 3.0, 0.0);
    }
}
