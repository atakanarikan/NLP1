import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 26.04.2016.
 */
public class Reader {
    public static void ReadFile(String filepath, HashMap<String, PosTag> posList) throws IOException {
        BufferedReader read;
        read = new BufferedReader(new FileReader(new File(filepath)));
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

    public static void normalize(HashMap<String, PosTag> allPosTags) {
        for (String posName : allPosTags.keySet()) {
            allPosTags.get(posName).normalize();
        }
    }
}
