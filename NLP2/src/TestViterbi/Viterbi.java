package TestViterbi;

import DataTrainer.PosTag;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 27.04.2016.
 */
public class Viterbi {
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

    /**
     * Writes the output of the program in to the given file in the format of <w1 pos1>
     * @param filename input filename
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private static void write(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        for (String line : result) {
            if (line.equals("\n")) {
                writer.println("");
            }
            else {
                writer.println(line);
            }
        }
        writer.close();
    }

    /**
     * Calculates the best possible series of PosTags for each of the sentences
     * @param sentences list of sentences in the test file
     */
    private static void viterbi(ArrayList<ArrayList<String>> sentences) {

        for (ArrayList<String> sentence : sentences) {
            String previousState = "start";
            for (String token : sentence) {
                String currentState;
                currentState = findAndRetrieveMostProbable(token, previousState);
                result.add(token + " " + currentState);
                previousState = currentState;
            }
            result.add("\n");
        }
    }

    /**
     * Considering the current word and the previous PosTag, calculates the current PosTag
     * @param token current Word
     * @param previousState previous PosTag
     * @return name of the winner PosTag
     */
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

    /**
     * Reads the test file.
     * @param filepath Path to the test file given as argument
     * @throws IOException
     */
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

    /**
     * Deserializes the output of DataTrainer
     * @param filename Path to the output of DataTrainer's .ser file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void deserialize(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        allPosTags = (HashMap<String, PosTag>) in.readObject();
        in.close();
        fileIn.close();
    }
}
