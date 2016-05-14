import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/**
 * Created by Atakan Arıkan on 03.03.2016.
 */
public class ArrangeInput {
    static int testCount, trainingCount, currentTestCount, currentTrainingCount;
    public static void main(String[] args) throws IOException {
        arrangeinput(args[0], args[1]);
    }

    /**
     *
     * @param inputFilePath
     * @param outPutDirectory
     * @throws IOException
     *
     * takes the source and destination folder as arguments
     * randomly selects %60 of the documents of a specific author and copies the files
     * to the location that is given as an argument.
     * the folder structure will look like:
     * <outputdestination>/training/<authorname>/*.txt
     * <outputdestination>/test/<authorname>/*.txt
     *
     * if you call this method with the same parameters, it will override the existing documents in the output folder
     * but will not delete the older files that means the files in the output destination will not obey the 60/40 rule.
     */
    private static void arrangeinput(String inputFilePath, String outPutDirectory) throws IOException {
        File outPutDir = new File(outPutDirectory);
        if(!outPutDir.exists()){ // create the main output folder if it doesnt exist in the output path
            try {
                //noinspection ResultOfMethodCallIgnored
                outPutDir.mkdir();
            } catch(SecurityException se) {
                se.printStackTrace();
            }
        }
        Files.walk(Paths.get(inputFilePath)).forEach(filePath -> {
            String authorName= "";
            try {
                if(Files.isDirectory(filePath) && !filePath.toString().equals(inputFilePath)) { //directory of an author
                    File testDir = new File(outPutDirectory + "/test");
                    File trainingDir = new File(outPutDirectory + "/training");
                    if(!testDir.exists()){ // create the test folder if it doesnt exist in the output path
                        try {
                            //noinspection ResultOfMethodCallIgnored
                            testDir.mkdir();
                        } catch(SecurityException se) {
                            se.printStackTrace();
                        }
                    }
                    if(!trainingDir.exists()){ // create the training folder if it doesnt exist in the output path
                        try {
                            //noinspection ResultOfMethodCallIgnored
                            trainingDir.mkdir();
                        } catch(SecurityException se) {
                            se.printStackTrace();
                        }
                    }
                    long fileCount = Files.list(filePath).count(); // count the number of documents for that author
                    testCount = (int) (fileCount * 0.4);
                    trainingCount = (int) (fileCount - testCount);
                    currentTestCount = 0;
                    currentTrainingCount = 0;
                    authorName = filePath.toString().substring(filePath.toString().indexOf("\\")+1);
                    File authorTestDir = new File(outPutDirectory + "\\test\\" + authorName);
                    File authorTrainingDir = new File(outPutDirectory + "\\training\\" + authorName);
                    if(!authorTestDir.exists()){ // create the test folder if it doesnt exist in the output path
                        try {
                            //noinspection ResultOfMethodCallIgnored
                            authorTestDir.mkdir();
                        } catch(SecurityException se) {
                            se.printStackTrace();
                        }
                    }
                    if(!authorTrainingDir.exists()){ // create the training folder if it doesnt exist in the output path
                        try {
                            //noinspection ResultOfMethodCallIgnored
                            authorTrainingDir.mkdir();
                        } catch(SecurityException se) {
                            se.printStackTrace();
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith("txt")) {
                Random rand = new Random();
                boolean isTraining; // false means it's a test document
                if(currentTestCount >= testCount) { // we selected enough test documents
                    isTraining = true;
                    currentTrainingCount++;
                }
                else if (currentTrainingCount >= trainingCount) { // we selected enough training documents
                    isTraining = false;
                    currentTestCount++;
                }
                else {
                    double coin = rand.nextDouble();
                    if(coin < 0.6) { //training
                        isTraining = true;
                        currentTrainingCount++;
                    }
                    else {
                        isTraining = false;
                        currentTestCount++;
                    }
                }
                // now we know whether it is a test or training document, we can read and write into output folder.
                try {
                    String authorFileName = filePath.toString().substring(filePath.toString().indexOf("/")+1);
              //      System.out.print("Copying " + filePath.toString() + " to: ");
                    if (isTraining) { // write to training folder
                        String targetPath = outPutDirectory + "/training/" + authorFileName; // determine the output location
                //        System.out.print(targetPath + " ...");
                        File targetFile = new File(targetPath);
                        Files.copy(filePath, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING); // copy the file
                    } else { // write to test folder
                        String targetPath = outPutDirectory + "/test/" + authorFileName; // determine the output location
         //               System.out.print(targetPath + " ...");
                        File targetFile = new File(targetPath);
                        Files.copy(filePath, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING); // copy the file
                    }
          //          System.out.println(" Done!");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
