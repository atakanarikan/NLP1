import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Atakan Arıkan on 28.02.2016.
 */
public class Main {
    public static int totalDocNumber;
    public static HashMap<String, HashMap<String, Integer>> authorsTraining = new HashMap<>();
    public static HashMap<String, HashMap<String, Integer>> testDocs = new HashMap<>();
    public static HashMap<String, Integer> authorsDocCount = new HashMap<>();
    public static HashSet<String> vocabulary = new HashSet<>();
    /*
    author1 : {
        word1: freq1,
         word2: freq2
         }
    */
    public static void main(String[] args) throws IOException {
        System.out.println("hello");
        readInput(args[0], args[1]);
        naiveBayes();
        /*
        String key1 = "gulseBirsel";
        System.out.println(key1 + ": {");
        for(String key : authorsTraining.get(key1).keySet()){
            System.out.println("   " + key + ": " + authorsTraining.get(key1).get(key) + ",");
        }
        System.out.println("}");
        */
    }


    public static void naiveBayes() throws IOException {
        for (String filename: testDocs.keySet()) { // for each of the test documents
            boolean x = true;
            HashMap<String, Integer> testDocWords = testDocs.get(filename); // get the {token: frequency} count of the test document
            double minAuthorScore = 999999;
            String winnerAuthor = "";
            for (String author: authorsTraining.keySet()) { // for each of the authors(classes)
                double authorResult = 0;
                winnerAuthor = "";
                minAuthorScore = -99999;
                double probAuthor = (double)authorsDocCount.get(author) / (double) totalDocNumber; // P(c)
                HashMap<String, Integer> authorWords = authorsTraining.get(author); //get the {token: frequency} count of the combination of that author's docs
                for (String testDocumentWord: testDocWords.keySet()) { // for each word in the test document
                    int currentWordCount = 0;
                    if(authorWords.containsKey(testDocumentWord)){
                        currentWordCount = authorWords.get(testDocumentWord); // get the author's word frequency
                    }
                    double tempResult = (double) (currentWordCount + 1) / (double) (authorWords.keySet().size() + vocabulary.size());
                  //  System.out.println(testDocumentWord + " : " +tempResult);
                    authorResult+= Math.log(tempResult);
                }
                authorResult+= Math.log(probAuthor);

                if(authorResult > minAuthorScore) {
                    minAuthorScore = authorResult;
                    winnerAuthor = author;
                    System.out.println(filename + " + " + winnerAuthor+ " = " +minAuthorScore);
                }

            }
        }

    }

    @SuppressWarnings("Duplicates")
    public static void readInput(String trainingInput, String testInput) throws IOException {
        totalDocNumber = 0;
        // read all the files in given folder
        Files.walk(Paths.get(trainingInput)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith("txt")) {
                //noinspection TryWithIdenticalCatches
                try {
                    totalDocNumber++;
                    String str = "";
                    //System.out.println(filePath);
                    String authorName = filePath.toString().substring(filePath.toString().indexOf("\\") + 10, filePath.toString().lastIndexOf("\\"));
                    if(!authorsTraining.containsKey(authorName)) {
                        authorsTraining.put(authorName, new HashMap<>());
                    }
                    if(!authorsDocCount.containsKey(authorName)) {
                        authorsDocCount.put(authorName, 0);
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
                            vocabulary.add(token);
                            if(token.length() > 0) {
                                if (authorsWords.containsKey(token)) {
                                    authorsWords.put(token, authorsWords.get(token) + 1);
                                } else {
                                    authorsWords.put(token, 1);
                                }
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Files.walk(Paths.get(testInput)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith("txt")) {
                //noinspection TryWithIdenticalCatches
                try {
                    String str = "";
                    if(!testDocs.containsKey(filePath.toString())) {
                        testDocs.put(filePath.toString(), new HashMap<>());
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
                            if(token.length() > 0) {
                                if (testDocWords.containsKey(token)) {
                                    testDocWords.put(token, testDocWords.get(token) + 1);
                                } else {
                                    testDocWords.put(token, 1);
                                }
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
