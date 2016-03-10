package agents.iterative_enhancement;

import exceptions.UnsolvableProblem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import problem_elements.State;
import problems.GeneticEncoding;
import problems.Problem;

import java.io.InvalidClassException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 * An Agent implementing genetic algorithms to find a solution.
 */
public class GeneticAgent extends IterativeEnhancementAgent {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    /**
     * The probability of mutating a newly created element.
     */
    public static final float MUTATION_P = 0.3f;

    /**
     * The minimum score required to a solution to be returned.
     */
    public static final float MIN_SOLUTION_SCORE = 0.75f;

    /**
     * The number of individuals created for each new generation.
     */
    private final int population_size;

    /**
     * The maximum number of generation created.
     */
    public int generation_count = Integer.MAX_VALUE;

    /**
     * Before returning a solution, require it to have at least this score.
     */
    public float minimum_solution_score = MIN_SOLUTION_SCORE;

    /**
     * Defines the probability of a mutation.
     */
    public float mutation_probability = MUTATION_P;

    /**
     * Build a new agent, starting from an instance of problem.
     *
     * @param problem The problem to be solved.
     */
    public GeneticAgent(@NotNull Problem problem, int population_size) throws InvalidClassException {
        super(problem);
        if (!(problem instanceof GeneticEncoding<?, ?>)) {
            throw new InvalidClassException(
                    "Problems for the genetic agent need a utility function and a genetic encoding.");
        }

        this.population_size = population_size;
    }

    @Override
    public @NotNull State findSolution() throws UnsolvableProblem {
        assert (0.0f <= mutation_probability) && (mutation_probability <= 1.0f);

        @SuppressWarnings("unchecked")  // We check it in the constructor.
        final GeneticEncoding<State, Object> genetic_problem = (GeneticEncoding<State, Object>) this.problem;

        State[] generation = Stream.generate(problem::buildRandomState)
                .limit(this.population_size)
                .toArray(State[]::new);
        double[] scores = Arrays.stream(generation)
                .mapToDouble(genetic_problem::score).toArray();

        final Random r = new Random();
        for (int i = 0; i < this.generation_count; i++) {
            {
                // Check if we have a solution that is good enough.
                int arg_max = 0;
                for (int j = 1; j < this.population_size; j++) {
                    if (scores[j] > scores[arg_max]) {
                        arg_max = j;
                    }
                }

                System.out.println(scores[arg_max]);
                if (scores[arg_max] >= this.minimum_solution_score) {
                    return generation[arg_max];
                }
            }

            final int scores_sum = (int) (Arrays.stream(scores).sum() * 10);
            final int[] to_generation_with_score = new int[scores_sum];

            {
                // Build a new array, allowing to pick elements with a probability based on their score
                int j = 0;
                for (int k = 0; k < scores.length; k++) {
                    double score = scores[k];
                    for (; j < score * 10; j++) {
                        to_generation_with_score[j] = k;
                    }
                }
            }

            final State[] next_generation = new State[this.population_size];
            for (int j = 0; j < this.population_size; j++) {
                final Object[] father = genetic_problem.getEncoding(
                        generation[to_generation_with_score[r.nextInt(scores_sum)]]);
                final Object[] mother = genetic_problem.getEncoding(
                        generation[to_generation_with_score[r.nextInt(scores_sum)]]);
                assert father.length == mother.length;

                logger.debug(Arrays.toString(father));
                logger.debug(Arrays.toString(mother));

                final int crossover_index = r.nextInt(father.length);
                final Object[] child = new Object[father.length];
                logger.debug(Arrays.toString(child));

                // Crossover of mother and father
                System.arraycopy(father, 0, child, 0, crossover_index);
                System.arraycopy(mother, crossover_index, child, crossover_index, child.length - crossover_index);

                State child_state = genetic_problem.encode(child);
                final float p_mutate = r.nextFloat();
                if (p_mutate >= mutation_probability) {
                    child_state = genetic_problem.mutate(child_state);
                }

                next_generation[j] = child_state;
                scores[j] = genetic_problem.score(child_state);

            }

            generation = next_generation;
        }

        throw new UnsolvableProblem(String.format("%s couldn't find a solution", this.getClass().getSimpleName()));
    }

    @Override
    public @NotNull String solutionToString() {
        final StringBuilder output = new StringBuilder("\n");

        try {
            final State solution = this.findSolution();
            output.append(String.format("%s found a solution: ", this.getClass().getSimpleName()));
            output.append(solution.toString());
            output.append("\n");
        } catch (UnsolvableProblem e) {
            output.append(e.toString());
        }

        final String stats = this.statsToString();
        if (stats != null) {
            output.insert(1, stats + "\n");
        }

        return output.toString();
    }
}
