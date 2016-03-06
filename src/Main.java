import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Atakan Arıkan on 28.02.2016.
 */
public class Main {
    public static int totalDocNumber;
    public static HashMap<String, HashMap<String, Integer>> authorsTraining = new HashMap<>();
    public static HashMap<String, HashMap<String, Integer>> testDocs = new HashMap<>();
    public static HashMap<String, Integer> authorsDocCount = new HashMap<>();
    public static HashSet<String> vocabulary = new HashSet<>();
    public static HashMap<String, Double> totalWordLengthAuthors = new HashMap<>();
    public static HashMap<String, Double> totalWordLengthTestDocs = new HashMap<>();
    public static HashMap<String, Double> totalWordCountAuthors = new HashMap<>();
    public static HashMap<String, Double> totalWordCountTestDocs = new HashMap<>();
    public static final double NAIVE_BAYES_RATE = 0.6;
    public static final double AVERAGE_WORD_LENGTH_RATE = 0.4;
    /*
    author1 : {
        word1: freq1,
         word2: freq2
         }
    */
    public static void main(String[] args) throws IOException {
        readInput(args[0], args[1]);
        naiveBayes();
    }

    /**
     * calculates and prints the result of naive bayes method for all test documents
     * @throws IOException
     */
    public static void naiveBayes() throws IOException {
        double totalWinners = 0;
        for (String filename: testDocs.keySet()) { // for each of the test documents
            HashMap<String, Integer> testDocWords = testDocs.get(filename); // get the {token: frequency} count of the test document
            double minAuthorScore = -999999;
            String winnerAuthor = "";
            for (String author: authorsTraining.keySet()) { // for each of the authors(classes)
                double authorResult = 0;
                double averageWordLengthAuthor = totalWordLengthAuthors.get(author) / totalWordCountAuthors.get(author);
                double averageWordLengthTestDoc = totalWordLengthTestDocs.get(filename) / totalWordCountTestDocs.get(filename);
                double probAuthor = (double)authorsDocCount.get(author) / (double) totalDocNumber; // P(c)
                HashMap<String, Integer> authorWords = authorsTraining.get(author); //get the {token: frequency} count of the combination of that author's docs
                for (String testDocumentWord: testDocWords.keySet()) { // for each word in the test document
                    int currentWordCount = 0;
                    if(authorWords.containsKey(testDocumentWord)){
                        currentWordCount = authorWords.get(testDocumentWord); // get the author's word frequency
                    }
                    // calculate P(w|c) with Laplace smoothing
                    double tempResult = (double) testDocWords.get(testDocumentWord) * (double) (currentWordCount + 1) / (double) (authorWords.keySet().size() + vocabulary.size());
                   authorResult += Math.log(tempResult); // multiply with P(C)
                }
                authorResult+= NAIVE_BAYES_RATE *Math.log(probAuthor);
                authorResult -= AVERAGE_WORD_LENGTH_RATE * 1000*Math.abs(averageWordLengthAuthor - averageWordLengthTestDoc);
                if(authorResult >= minAuthorScore) {
                    minAuthorScore = authorResult;
                    winnerAuthor = author;
                }
            }
            String testAuthor = filename.substring(filename.indexOf("\\") + 1);
            testAuthor = testAuthor.substring(testAuthor.indexOf("\\") + 1);
            System.out.print(testAuthor + " is classified as from the author: " +winnerAuthor);
            if(filename.contains(winnerAuthor))  {
                System.out.print(" *************** ");
                totalWinners++;
            }
            System.out.println();

        }
        double overallResult = 100*totalWinners/(double) testDocs.size();
        System.out.println("Overall result: " + totalWinners + "/" + testDocs.size() + " = % " + String.format( "%.2f", overallResult ));
    }

    @SuppressWarnings("Duplicates")
    public static void readInput(String trainingInput, String testInput) throws IOException {
        totalDocNumber = 0;
        // read all the files in given folder
        System.out.print("Reading the training files... ");
        Files.walk(Paths.get(trainingInput)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith("txt")) {
                //noinspection TryWithIdenticalCatches
                try {
                    totalDocNumber++;
                    String str = "";
                    String authorName = filePath.toString().substring(filePath.toString().indexOf("\\") + 10, filePath.toString().lastIndexOf("\\"));
                    if(!authorsTraining.containsKey(authorName)) {
                        authorsTraining.put(authorName, new HashMap<>());
                    }
                    if(!authorsDocCount.containsKey(authorName)) {
                        authorsDocCount.put(authorName, 0);
                    }
                    if(!totalWordLengthAuthors.containsKey(authorName)) {
                        totalWordLengthAuthors.put(authorName, 0.0);
                    }
                    if(!totalWordCountAuthors.containsKey(authorName)) {
                        totalWordCountAuthors.put(authorName, 0.0);
                    }
                    authorsDocCount.put(authorName, authorsDocCount.get(authorName) + 1);
                    HashMap<String, Integer> authorsWords = authorsTraining.get(authorName);
                    InputStream bytes = new FileInputStream(filePath.toString());
                    Reader chars = new InputStreamReader(bytes, StandardCharsets.ISO_8859_1);
                    BufferedReader read = new BufferedReader(chars);
                    while((str = read.readLine()) != null) {
                        str = str.toLowerCase();
                        str = tokenize(str);
                        String[] line = str.trim().split(" ");
                        for (String token: line) { // for each word in that specific line in the file
                            totalWordLengthAuthors.put(authorName, totalWordLengthAuthors.get(authorName) + token.length());
                            vocabulary.add(token);
                            if(token.length() > 0) {
                                if (authorsWords.containsKey(token)) {
                                    authorsWords.put(token, authorsWords.get(token) + 1);
                                } else {
                                    authorsWords.put(token, 1);
                                }
                            }
                        }
                        totalWordCountAuthors.put(authorName, totalWordCountAuthors.get(authorName) + line.length);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Done!");
        System.out.print("Reading the test files... ");
        Files.walk(Paths.get(testInput)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith("txt")) {
                //noinspection TryWithIdenticalCatches
                try {
                    String str = "";
                    if(!testDocs.containsKey(filePath.toString())) {
                        testDocs.put(filePath.toString(), new HashMap<>());
                    }
                    if(!totalWordLengthTestDocs.containsKey(filePath.toString())) {
                        totalWordLengthTestDocs.put(filePath.toString(), 0.0);
                    }
                    if(!totalWordCountTestDocs.containsKey(filePath.toString())) {
                        totalWordCountTestDocs.put(filePath.toString(), 0.0);
                    }
                    HashMap<String, Integer> testDocWords = testDocs.get(filePath.toString());
                    InputStream bytes = new FileInputStream(filePath.toString());
                    Reader chars = new InputStreamReader(bytes, StandardCharsets.ISO_8859_1);
                    BufferedReader read = new BufferedReader(chars);
                    while((str = read.readLine()) != null) {
                        str = str.toLowerCase();
                        str = tokenize(str);
                        String[] line = str.trim().split(" ");
                        for (String token: line) { // for each word in that specific line in the file
                            totalWordLengthTestDocs.put(filePath.toString(), totalWordLengthTestDocs.get(filePath.toString()) + token.length()); // add the token length
                            if(token.length() > 0) {
                                if (testDocWords.containsKey(token)) {
                                    testDocWords.put(token, testDocWords.get(token) + 1);
                                } else {
                                    testDocWords.put(token, 1);
                                }
                            }
                        }
                        totalWordCountTestDocs.put(filePath.toString(), totalWordCountTestDocs.get(filePath.toString()) + line.length);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Done!");
    }

    /*
        takes the input, makes all lowercased.
        gets rid of the words containing all nonword characters.
        gets rid of the nonword characters at the beginning of a word.
        gets rid of the nonword characters at the end of a word.
        gets rid of any nonword character in a word (excluding digits) // 19.2 or 16,3 will pass, whereas "It's okay" will be "its okay"
    */
    public static String tokenize(String line) {
        String[] prettyTokens = line.toLowerCase().split(" "); // split the lowercase string by whitespace and avoid the "Subject: " in the beginning
        StringBuilder bigBuilder = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < prettyTokens.length; i++) {
            String active = prettyTokens[i];
            if (active.length() > 0) { // parseDouble thinks "1980." a double. avoid that.
                if (!Character.isDigit(active.charAt(active.length() - 1)) && !Character.isLetter(active.charAt(active.length() - 1))) {
                    active = active.substring(0, active.length() - 1);
                }
            }
            try {
                double x = Double.parseDouble(active);
                bigBuilder.append(active);
                bigBuilder.append(" ");
            } catch (Exception e) {  // so this is not a double value
                String activeTemp = active.replace(',', '.'); //try again
                try {
                    double x = Double.parseDouble(activeTemp);
                    bigBuilder.append(activeTemp);
                    bigBuilder.append(" ");

                } catch (Exception e1) {
                    if (active.contains("&lt;")) { // handle words including this
                        active = active.substring(0, active.indexOf("&lt;")) + active.substring(active.indexOf("&lt;") + 3);
                    }
                    for (int j = 0; j < active.length(); j++) {
                        if (Character.isDigit(active.charAt(j)) || Character.isLetter(active.charAt(j))) {
                            stringBuilder.append(active.charAt(j));
                        }
                    }
                    bigBuilder.append(stringBuilder.toString());
                    bigBuilder.append(" ");
                    stringBuilder.setLength(0);
                }
            }
        }
        return bigBuilder.toString();
    }
}
