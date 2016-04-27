package TestViterbi;

import DataTrainer.PosTag;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 27.04.2016.
 */
public class Tester {
    public static HashMap<String, PosTag> allPosTags = new HashMap<>();
    public static ArrayList<ArrayList<String>> sentences = new ArrayList<>();
    public static ArrayList<String> pos = new ArrayList<>();
    public static ArrayList<String> result = new ArrayList<>();
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        deserialize(args[2]);
        readFile(args[0]);
        viterbi(sentences);
        write(args[1]);
        System.out.println();
    }

    private static void write(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        for (String line : result) {
            writer.println(line);
        }
        writer.close();
    }

    private static void viterbi(ArrayList<ArrayList<String>> sentences) {

        for (ArrayList<String> sentence : sentences) {
            String previousState = "start";
            for (String token : sentence) {
                String currentState;
                currentState = findAndRetrieveMostProbable(token, previousState);
                result.add(token + " " + currentState);
                previousState = currentState;
            }
        }
    }

    private static String findAndRetrieveMostProbable(String token, String previousState) {
        String winner = "";
        double max = 0;
        for (String posName: allPosTags.keySet()) {
            double wordResult = 0.000000001;
            double posResult =  0.000000001;
            PosTag candidate = allPosTags.get(posName);
            if (candidate.getWordProbabilites().containsKey(token)) {
                wordResult = candidate.getWordProbabilites().get(token);
            }
            if (candidate.getPreviousPosProbs().containsKey(previousState)) {
                posResult = candidate.getPreviousPosProbs().get(previousState);
            }
            if (wordResult * posResult >= max) { // temp winner
                max = wordResult * posResult;
                winner = candidate.getName();
            }
        }
        return winner;
    }

    private static void readFile(String filepath) throws IOException {
        BufferedReader read;
        File infile = new File(filepath);
        read = new BufferedReader(new FileReader(infile));
        String str = "";
        ArrayList<String> sentence = new ArrayList<>();
        while (true) {
            str = read.readLine();
            if(str == null || str.equals("")) {
                sentences.add(sentence);
                sentence = new ArrayList<>();
                if (str == null) {
                    return;
                }
            }

            else if (str.contains("\t")) {
                String[] tokens = str.split("\t");
                if (!tokens[1].equals("_")) {
                    sentence.add(tokens[1].toLowerCase());
                    pos.add(tokens[4].toLowerCase());
                }
            }
        }
    }

    private static void deserialize(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        allPosTags = (HashMap<String, PosTag>) in.readObject();
        in.close();
        fileIn.close();
    }
}