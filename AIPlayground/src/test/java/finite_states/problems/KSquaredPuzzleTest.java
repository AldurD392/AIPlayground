package finite_states.problems;

import finite_states.Action;
import finite_states.State;
import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class KSquaredPuzzleTest extends TestCase {

    public final int k = 4;
    public final static String PUZZLE_TEST_NAME = "test_puzzle";
    public final KSquaredPuzzle puzzle = new KSquaredPuzzle(PUZZLE_TEST_NAME, k);

    public void testGoalState() throws NoSuchFieldException {
        State g = (State) PrivateAccessor.getField(puzzle, "goal");
        assertEquals(g.getClass(), KSquaredPuzzle.KSquaredState.class);
        KSquaredPuzzle.KSquaredState squared = (KSquaredPuzzle.KSquaredState)g;

        int i;
        for (i = 0; i < (k * k) - 1; i++) {
            assertEquals(i + 1, squared.puzzle[i]);
        }

        assertEquals(squared.puzzle[i], 0);
    }

    public void testGetActions() throws NoSuchFieldException {
        State goal_state = (State) PrivateAccessor.getField(puzzle, "goal");
        final ArrayList<Action> actions = (ArrayList<Action>)goal_state.getActions();

        assertEquals(actions.size(), 2);
        assertFalse(actions.contains(KSquaredPuzzle.DOWN));
        assertFalse(actions.contains(KSquaredPuzzle.RIGHT));
    }

    public void testHeuristic() throws NoSuchFieldException {
        KSquaredPuzzle.KSquaredState goal_state = (KSquaredPuzzle.KSquaredState) PrivateAccessor.getField(puzzle, "goal");
        assertEquals(puzzle.getHeuristicValue(goal_state), 0.0, 0.0);

        KSquaredPuzzle.KSquaredState new_state = (KSquaredPuzzle.KSquaredState)goal_state.performAction(KSquaredPuzzle.UP);
        assertEquals(puzzle.getHeuristicValue(new_state), 1.0, 0.0);

        new_state = (KSquaredPuzzle.KSquaredState)new_state.performAction(KSquaredPuzzle.UP);
        assertEquals(puzzle.getHeuristicValue(new_state), 2.0, 0.0);

        new_state = (KSquaredPuzzle.KSquaredState)new_state.performAction(KSquaredPuzzle.LEFT);
        assertEquals(puzzle.getHeuristicValue(new_state), 3.0, 0.0);
    }

    public void testIsSolvable() throws Throwable {
        final KSquaredPuzzle.KSquaredState goal_state = (KSquaredPuzzle.KSquaredState) PrivateAccessor.getField(puzzle, "goal");
        assertTrue(puzzle.isSolvable(goal_state));

        final int[] unsolvable_puzzle = goal_state.puzzle.clone();
        unsolvable_puzzle[(k * k) - 2] = (k * k) - 2;
        unsolvable_puzzle[(k * k) - 3] = (k * k) - 1;

        final Constructor<KSquaredPuzzle.KSquaredState> constructor =
                KSquaredPuzzle.KSquaredState.class.getDeclaredConstructor(KSquaredPuzzle.class, int[].class, int.class, Action.class);
        constructor.setAccessible(true);
        final KSquaredPuzzle.KSquaredState unsolvable_state =
                constructor.newInstance(puzzle, unsolvable_puzzle, (k * k) - 1, KSquaredPuzzle.LEFT);
        assertFalse(puzzle.isSolvable(unsolvable_state));
    }
}
