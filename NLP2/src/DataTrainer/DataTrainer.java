package DataTrainer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 26.04.2016.
 */
public class DataTrainer {
    public static HashMap<String, PosTag> allPosTags = new HashMap<>();
    public static void main(String[] args) throws IOException {
        initialize(args[0]);
        serialize(args[1]);
    }
    /**
    Serializes the trained data. In the second java code, the program will read the serialized data
     and deserialize it, then the data will be loaded to the memory.
     @param filepath: second argument given to the program. name of the output file.
     */
    private static void serialize(String filepath) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filepath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(allPosTags);
        out.close();
        fileOut.close();
    }

    /**
     * Takes the input as an argument and reads the file. You need to give the full path to the training file
     * that has the extension ".conll"
     * @param filepath: first argument of the program. path of the file.
     * @throws IOException
     */
    static void initialize(String filepath) throws IOException {
        allPosTags.put("start", new PosTag("start"));
        allPosTags.put("end", new PosTag("end"));
        Reader.ReadFile(filepath, allPosTags);
        Reader.normalize(allPosTags);
    }
}
