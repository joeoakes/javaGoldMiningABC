import java.util.Arrays;
import java.util.Random;

/*
Each solution represents a potential allocation of mining intensity across different gold sources.
The fitness of each solution is evaluated as the total gold yield obtained from all sources.
Stagnant solutions (those with the same fitness value for a certain number of iterations) are replaced with new random solutions.
During each iteration, the employed bees phase introduces small random changes to the mining intensity at a randomly selected source.
 */

public class GoldMiningABC {
    private static final int NUM_GOLD_SOURCES = 15;
    private static final int POPULATION_SIZE = 20;
    private static final int MAX_ITERATIONS = 100;
    private static final int ABANDONMENT_THRESHOLD = 10;

    private double[] goldYields;
    private double[][] solutions;
    private double[] fitnessValues;
    private Random random;

    public GoldMiningABC() {
        this.random = new Random();
        this.goldYields = generateGoldYields();
        this.solutions = new double[POPULATION_SIZE][NUM_GOLD_SOURCES];
        this.fitnessValues = new double[POPULATION_SIZE];

    }

    private double[] generateGoldYields() {
        double[] yields = new double[NUM_GOLD_SOURCES];
        for (int i = 0; i < NUM_GOLD_SOURCES; i++) {
            yields[i] = random.nextDouble() * 100; // Random gold yield between 0 and 100
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
            solution[i] = random.nextDouble(); // Random mining intensity between 0 and 1
        }
        return solution;
    }

    private double evaluateSolution(double[] solution) {
        double totalYield = 0;
        for (int i = 0; i < NUM_GOLD_SOURCES; i++) {
            totalYield += solution[i] * goldYields[i]; // Total gold yield from all sources
        }
        return totalYield;
    }

    private void evaluatePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            fitnessValues[i] = evaluateSolution(solutions[i]); // Evaluate fitness of each solution
        }
    }

    private void runAlgorithm() {
        initializePopulation();
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            evaluatePopulation(); // Evaluate fitness of current solutions
            Arrays.sort(fitnessValues); // Sort solutions by fitness

            // Check for stagnation and abandon solutions
            for (int i = 1; i < POPULATION_SIZE; i++) {
                if (fitnessValues[i] == fitnessValues[i - 1]) {
                    if (i >= ABANDONMENT_THRESHOLD) {
                        solutions[i] = generateRandomSolution(); // Replace stagnant solution
                    }
                }
            }

            // Employed bees phase (local search)
            for (int i = 0; i < POPULATION_SIZE; i++) {
                int sourceIndex = random.nextInt(NUM_GOLD_SOURCES);
                double oldValue = solutions[i][sourceIndex];
                double newValue = oldValue + (random.nextDouble() - 0.5); // Random small change
                newValue = Math.max(0, Math.min(1, newValue)); // Ensure newValue is within [0, 1]
                solutions[i][sourceIndex] = newValue; // Update solution
            }
        }

        // Print the best solution
        double bestFitness = fitnessValues[POPULATION_SIZE - 1];
        int index = Arrays.binarySearch(fitnessValues, bestFitness);
        System.out.println("Best solution:");
        System.out.println(Arrays.toString(solutions[index]));
        System.out.println("Total yield: " + bestFitness);
    }

    public static void main(String[] args) {
        GoldMiningABC abc = new GoldMiningABC();
        abc.runAlgorithm();
    }
}
