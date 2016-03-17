import java.io.IOException;

/**
 * Created by Atakan Arıkan on 17.03.2016.
 */
public class Runner {
    public static void main(String[] args) throws IOException {
        ArrangeInput arrangeInput = new ArrangeInput();
        Main main = new Main();
        String[] args1 = {"raw_texts", "outputfolder"};
        String[] args2 = {"outputfolder/training", "outputfolder/test"};
        arrangeInput.main(args1);
        main.main(args2);
    }
}
