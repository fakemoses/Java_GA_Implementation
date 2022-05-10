import java.util.*;

public class MyownGA {
    float [] k_Regler;
    float [] stufen;
    int num_pop;

    float temporaryWeight = 0;
    int counter = 0;
    float [] finalWeight;

    public MyownGA(int num_pop, float [] k_Regler, float [] stufen) {
        this.num_pop = num_pop;
        this.k_Regler = k_Regler;
        this.stufen = stufen;
        this.finalWeight = new float[num_pop];
    }

    // create first generation of data
    float [][] createInitialConf(int num_pop, float[] k_Regler, float[] stufen){
        float [][] E = new float[num_pop][2];
        Random rand = new Random();
        int len = k_Regler.length;
        //generate num_pop amount of car configs randomly
        for (int i = 0; i < num_pop; i++ ){
            int stufen_randIdx = rand.nextInt(stufen.length);
            int k_randIdx = rand.nextInt(k_Regler.length);

            E[i][0] = k_Regler[k_randIdx];
            E[i][1] = stufen[stufen_randIdx];
        }
        return E;
    }

    // loop for each config to obtain the data
    // get 10 frames
    // compute the density of red pixels (avg)
    // return the fitness of each config. 1-1
    // higher density == fitter
    void computeFittness(int configIdx, int numberofRedPixels, int sizeofImg) {
        if (this.counter < 10) {
            //take 10 frames
            //compute and save in temp
            float tmp = ((float)numberofRedPixels/(float)sizeofImg);
            this.temporaryWeight += tmp;
            //increment till 10 frames reached
            this.counter += 1;
        } else if (counter == 10) {
            //save the final temporaryWeight in the final array
            this.finalWeight[configIdx] = this.temporaryWeight/(float)10.0;
            //System.out.println("finalWeight: " + this.finalWeight[configIdx]);
            //reset counter & temporaryWeight var
            this.temporaryWeight = 0;
            this.counter = 0;
        }

    }

    //selection takes the config and the fitness of each config
    int [] selection(int selection_percentage) {
        //sort the finalWeight array
        HashMap<Integer, Float> map = new HashMap<Integer, Float>();
        for (int i = 0; i < this.finalWeight.length; i++) {
            map.put(i, this.finalWeight[i]);
        }
        //print finalweight before sort
        //System.out.println("finalWeight before sort: "+ Arrays.toString(this.finalWeight));
        Arrays.sort(finalWeight);
        // find the index of the last 2 arrays in the hashmap
        int [] originalIdx = new int[selection_percentage];
        for (int i = 0; i < selection_percentage; i++) {
            for (int j = 0; j < this.finalWeight.length; j++) {
                if (map.get(j) == this.finalWeight[this.finalWeight.length - i - 1]) {
                    originalIdx[i] = j;
                }
            }
        }
        //System.out.println("orginalIdx: " + Arrays.toString(originalIdx));
        return originalIdx;

    }

    //crossover takes the config and the fitness of each config
    //crossover takes 4 configs and crossover them
    //return the new config
    float[][] offspring_crossover(float[][] parentParams, int num_pop) {
        float[][] new_E = new float[num_pop][2];
        Random rand = new Random();

        for (int i = 0; i < num_pop; i++) {
            //single solution crossover divided into 2 parts
            int parents1Idx = i%parentParams.length;
            int parents2Idx = (i+1)%parentParams.length;
            //randomly choose the crossover point
            int crossover_point = rand.nextInt(parentParams[0].length);
            //copy the first part of the parent1 to the new_E
            for (int j = 0; j < crossover_point; j++) {
                new_E[i][j] = parentParams[parents1Idx][j];
            }
            //copy the second part of the parent2 to the new_E
            for (int j = crossover_point; j < parentParams[0].length; j++) {
                new_E[i][j] = parentParams[parents2Idx][j];
            }
        }
        return new_E;
    }

    float [][] offspring_mutation(float[][] offspring) {
        float[][] new_E = new float[num_pop][2];
        Random rand = new Random();
        //mutation
        for (int i = 0; i < num_pop; i++) {
            for (int j = 0; j < offspring[0].length; j++) {
                if (rand.nextFloat() < 0.1) {
                    new_E[i][j] = rand.nextFloat();
                }
            }
        }

        return new_E;
    }




}
