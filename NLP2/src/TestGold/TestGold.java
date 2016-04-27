package TestGold;

import java.io.*;

/**
 * Created by Atakan Arıkan on 27.04.2016.
 */
public class TestGold {
    public static void main(String[] args) throws IOException {
        readAndCalculate(args[0], args[1], args[2]);
    }


    /**
     * reads and calculates the success rate of the program with respect to given golden standart file
     * @param outputfilename output of the Viterbi file
     * @param goldfilename path to the golden standart file
     * @param posType --[cpostag | postag]
     * @throws IOException
     */
    private static void readAndCalculate(String outputfilename, String goldfilename, String posType) throws IOException {
        BufferedReader read;
        BufferedReader read2;
        File infile = new File(outputfilename);
        read = new BufferedReader(new FileReader(infile));
        String str1;
        String str2;
        File infile2 = new File(goldfilename);
        read2 = new BufferedReader(new FileReader(infile2));
        int count = 0;
        int total = 0;
        int index = 4;
        if(posType.contains("cpostag")) {
            index = 3;
        }
        while ((str2 = read2.readLine()) != null) {
            String[] arr2 = str2.split("\t");
            if (str2.equals("")) {
                read.readLine();
            }
            else if (!arr2[1].equals("_")) {
                str1 = read.readLine();
                String[] arr1 = str1.split(" ");
                if (arr2.length > 1 && !arr2[1].equals("_") && arr1.length > 1) {
                    total++;
                    if (arr1[1].toLowerCase().equals(arr2[index].toLowerCase()) && arr1[0].equals(arr2[1].toLowerCase())){
                        count++;
                    }
                }

            }
        }
        System.out.println(count + "/" + total + " = " + (double) count / (double) total);
    }

}
