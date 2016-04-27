package TestViterbi;

import DataTrainer.PosTag;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 27.04.2016.
 */
public class Tester {
    public static HashMap<String, PosTag> allPosTags = new HashMap<>();

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        deserialize(args[2]);
    }

    private static void deserialize(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        allPosTags = (HashMap<String, PosTag>) in.readObject();
        in.close();
        fileIn.close();
    }
}
