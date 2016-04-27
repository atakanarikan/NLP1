import DataTrainer.DataTrainer;
import TestGold.TestGold;
import TestViterbi.Viterbi;

import java.io.IOException;

/**
 * Created by Atakan Arıkan on 28.04.2016.
 */
public class Runner {
    /**
     * Given all the parameters, runs the programs one by one.
     * First 3 parameters belong to DataTrainer
     * Next 3 parameters belong to TestViterbi
     * Last 3 parameters belong to TestGold
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String[] dataTrainerArgs, viterbiArgs, testGoldArgs;
        dataTrainerArgs = new String[3];
        viterbiArgs = new String[3];
        testGoldArgs = new String[3];
        for (int i = 0; i < args.length; i++) {
            if (i < 3) { //datatrainerargs
                dataTrainerArgs[i] = args[i];
            } else if (i < 6) { //viterbiargs
                viterbiArgs[i-3] = args[i];
            }
            else { //goldtesterargs
                testGoldArgs[i-6] = args[i];
            }
        }
        DataTrainer.main(dataTrainerArgs);
        Viterbi.main(viterbiArgs);
        TestGold.main(testGoldArgs);
    }
}
