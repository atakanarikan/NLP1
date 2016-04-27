package DataTrainer;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 26.04.2016.
 */
public class Reader {
    /**
     * Reads the file.
     *
     * Calculates the number of occurrences of a word for each PosTag.
     * Calculates the number of occurrences of a PosTag before the current PosTag
     * Calculates the number of occurrences of a PosTag after the current PosTag
     * @param filepath filePath given as the first argument.
     * @param posList training data to be filled.
     * @throws IOException
     */
    public static void ReadFile(String filepath, HashMap<String, PosTag> posList) throws IOException {
        FilePermission permission = new FilePermission(filepath, "read");
        BufferedReader read;
        File infile = new File(filepath);
        read = new BufferedReader(new FileReader(infile));
        String str;
        String previousPOS = "start";
        while ((str = read.readLine()) != null) {
            if (str.equals("")){ //beginning of a new sentence
                PosTag currentPOSObject = posList.get(previousPOS);
                currentPOSObject.addElement(currentPOSObject.getNextPosProbs(), "end", 1.0);
                posList.put(previousPOS, currentPOSObject);
                previousPOS = "start";
            }
            if (str.contains("\t")) {
                String[] elements = str.split("\t");
                String word;
                PosTag currentPOSObject;
                if (elements.length > 3){
                    word = elements[1].toLowerCase();
                    if(elements[1].equals("_")){
                        word = elements[2].toLowerCase();
                    }
                    String currentPOS = elements[4];
                    if (posList.containsKey(currentPOS)) {
                        currentPOSObject = posList.get(currentPOS);
                    } else {
                        currentPOSObject = new PosTag(elements[4]);
                    }
                    currentPOSObject.addElement(currentPOSObject.getPreviousPosProbs(), previousPOS, 1.0);
                    currentPOSObject.addElement(currentPOSObject.getWordProbabilites(), word, 1.0);
                    currentPOSObject.setTotalOccurence(currentPOSObject.getTotalOccurence() + 1);
                    PosTag previousPOSObject = posList.get(previousPOS);
                    previousPOSObject.addElement(previousPOSObject.getNextPosProbs(), currentPOS, 1.0);
                    posList.put(currentPOS, currentPOSObject);
                    posList.put(previousPOS, previousPOSObject);
                    previousPOS = currentPOS;
                }
            }
        }
    }

    /**
     * Calculates all the probabilities.
     *
     * Instead of number of occurrence, calculates and sets the probabilities for each PosTag.
     * @param allPosTags filled training data after reading the file.
     */
    public static void normalize(HashMap<String, PosTag> allPosTags) {
        for (String posName : allPosTags.keySet()) {
            allPosTags.get(posName).normalize();
        }
    }
}
