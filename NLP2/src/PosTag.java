import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 26.04.2016.
 */
public class PosTag {
    String name;
    HashMap<String, Double> wordProbabilites = new HashMap<>();
    HashMap<String, Double> previousPosProbs = new HashMap<>();
    HashMap<String, Double> nextPosProbs = new HashMap<>();
    int totalOccurence;

    public PosTag(String name) {
        this.name = name;
        this.totalOccurence = 0;
    }

    public int getTotalOccurence() {
        return totalOccurence;
    }

    public void setTotalOccurence(int totalOccurence) {
        this.totalOccurence = totalOccurence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getWordProbabilites() {
        return wordProbabilites;
    }

    public void setWordProbabilites(HashMap<String, Double> wordProbabilites) {
        this.wordProbabilites = wordProbabilites;
    }

    public HashMap<String, Double> getPreviousPosProbs() {
        return previousPosProbs;
    }

    public void setPreviousPosProbs(HashMap<String, Double> previousPosProbs) {
        this.previousPosProbs = previousPosProbs;
    }

    public HashMap<String, Double> getNextPosProbs() {
        return nextPosProbs;
    }

    public void setNextPosProbs(HashMap<String, Double> nextPosProbs) {
        this.nextPosProbs = nextPosProbs;
    }

    public void addElement(HashMap<String, Double> hashmap, String posName, double value) {
        if (hashmap.containsKey(posName)) {
            double newValue = hashmap.get(posName) + 1;
            hashmap.put(posName, newValue);
        } else {
            hashmap.put(posName, value);
        }
    }
    public void normalize(){
        for (String word : wordProbabilites.keySet()) {
            double newVal = wordProbabilites.get(word) / (double) totalOccurence;
            wordProbabilites.put(word, newVal);
        }
        for (String posTag : previousPosProbs.keySet()) {
            double newVal = previousPosProbs.get(posTag) / (double) totalOccurence;
            previousPosProbs.put(posTag, newVal);
        }
        for (String posTag : nextPosProbs.keySet()) {
            double newVal = nextPosProbs.get(posTag) / (double) totalOccurence;
            nextPosProbs.put(posTag, newVal);
        }
    }
}
