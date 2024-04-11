import java.util.Arrays;
import java.util.Random;

public class GoldMiningABC {
    private static final int NUM_GOLD_SOURCES = 15;
    private static final int POPULATION_SIZE = 20;
    private static final int MAX_ITERATIONS = 100;
    private static final double ABANDONMENT_THRESHOLD = 10; // Stagnation threshold

    private double[] goldYields;
    private double[][] solutions;
    private Random random;

    public GoldMiningABC() {
        this.goldYields = generateGoldYields();
        this.solutions = new double[POPULATION_SIZE][NUM_GOLD_SOURCES];
        this.random = new Random();
    }

    private double[] generateGoldYields() {
        double[] yields = new double[NUM_GOLD_SOURCES];
        for (int i = 0; i < NUM_GOLD_SOURCES; i++) {
            // Generate random yields for each gold source
            yields[i] = random.nextDouble() * 100; // Example: Yield between 0 and 100
        }
        return yields;
    }

    private void initializePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            solutions[i] = generateRandomSolution();
        }
    }

    private double[] generateRandomSolution() {
        double[] solution = new double[NUM_GOLD_SOURCES];
        for (int i = 0; i < NUM_GOLD_SOURCES; i++) {
            // Generate random decision variables (mining levels) for each gold source
            solution[i] = random.nextDouble(); // Example: Mining level between 0 and 1
        }
        return solution;
    }

    private double evaluateSolution(double[] solution) {
        double totalYield = 0;
        for (int i = 0; i < NUM_GOLD_SOURCES; i++) {
            // Calculate total yield based on mining levels and gold yields
            totalYield += solution[i] * goldYields[i];
        }
        return totalYield;
    }

    private void runAlgorithm() {
        initializePopulation();
        double[] fitnessValues = new double[POPULATION_SIZE];
        int[] iterationsSinceImprovement = new int[POPULATION_SIZE];

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            // Evaluate fitness for each solution
            for (int i = 0; i < POPULATION_SIZE; i++) {
                fitnessValues[i] = evaluateSolution(solutions[i]);
            }

            // Sort solutions by fitness
            Arrays.sort(solutions, (a, b) -> Double.compare(evaluateSolution(b), evaluateSolution(a)));

            // Check for stagnation and abandon solutions
            for (int i = 0; i < POPULATION_SIZE; i++) {
                if (i > 0 && fitnessValues[i] == fitnessValues[i - 1]) {
                    iterationsSinceImprovement[i]++;
                } else {
                    iterationsSinceImprovement[i] = 0;
                }
                if (iterationsSinceImprovement[i] >= ABANDONMENT_THRESHOLD) {
                    solutions[i] = generateRandomSolution(); // Abandon and replace with new solution
                    iterationsSinceImprovement[i] = 0;
                }
            }

            // Employed bees phase (local search)
            // For simplicity, we'll skip this phase in this example

            // Onlooker bees phase (neighbor exploration)
            // For simplicity, we'll skip this phase in this example

            // Scout bees phase (re-initialization of abandoned solutions)
            // This is already handled in the stagnation check above
        }

        // Print the best solution
        System.out.println("Best solution:");
        for (int i = 0; i < NUM_GOLD_SOURCES; i++) {
            System.out.println("Gold source " + (i + 1) + ": Mining level " + solutions[0][i]);
        }
        System.out.println("Total yield: " + evaluateSolution(solutions[0]));
    }

    public static void main(String[] args) {
        GoldMiningABC abc = new GoldMiningABC();
        abc.runAlgorithm();
    }
}

