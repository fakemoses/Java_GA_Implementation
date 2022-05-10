import java.util.Arrays;
import java.util.Random;

public class main {
    public static void main(String[] args) {
        int num_generations = 10;
        int num_pop = 8;
        float[] k_Regler = {0.1f, 0.2f, 0.3f, 0.4f};
        float[] stufen = {40f, 45, 50f, 55f};

        MyownGA myGA = new MyownGA(num_pop, k_Regler, stufen);
        float[][] initialConf = myGA.createInitialConf(num_pop, k_Regler, stufen);

        System.out.println("Current Dataset:");
        System.out.println(Arrays.deepToString(initialConf));

        for (int gen = 0; gen < num_generations ; gen++) {
            //simulate the 10 cycles with random numbers as it's density
            Random randy = new Random();
            // j is config index
            for (int j = 0; j < initialConf.length; j++) {
                for (int i = 0; i <= 10; i++) {
                    myGA.computeFittness(j, randy.nextInt(160000), 640 * 640);
                }
            }

            int [] bestWeightIdx = myGA.selection(4);
            float [][] parentParams = new float[4][2];

            //fill with 0
            for (float[] parentParam : parentParams) {
                Arrays.fill(parentParam, 0f);
            }
            // copy only the values having the index in the bestWeightIdx
            for (int i = 0; i < bestWeightIdx.length; i++) {
                parentParams[i][0] = initialConf[bestWeightIdx[i]][0];
                parentParams[i][1] = initialConf[bestWeightIdx[i]][1];
            }

            System.out.println("Best Params"+ " gen " + gen + ":" + Arrays.deepToString(parentParams));

            //crossover
            float[][] childParams = myGA.offspring_crossover(parentParams,num_pop);
            //childParams = myGA.offspring_mutation(childParams);
            if (gen < num_generations - 1) {
                System.out.println("Child Params pre mutation:" + Arrays.deepToString(childParams));
                System.out.println("");
            }

        }
    }
}