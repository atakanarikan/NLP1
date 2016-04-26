import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 26.04.2016.
 */
public class Main {
    public static HashMap<String, PosTag> allPosTags = new HashMap<>();
    public static void main(String[] args) throws IOException {
        initialize();

    }
    static void initialize() throws IOException {
        allPosTags.put("start", new PosTag("start"));
        allPosTags.put("end", new PosTag("end"));
        Reader.ReadFile("C:\\Users\\Atakan Arıkan\\Documents\\GitHub\\NLP1\\NLP2\\metu_sabanci_cmpe_561_v2\\train\\turkish_metu_sabanci_train.conll", allPosTags);
        Reader.normalize(allPosTags);
    }
}
