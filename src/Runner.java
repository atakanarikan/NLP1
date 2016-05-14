import java.io.IOException;

/**
 * Created by Atakan Arıkan on 17.03.2016.
 */
public class Runner {
    public static void main(String[] args) throws IOException {
        String[] args1 = {args[0], args[1]};
        String[] args2 = {args[1] +"/training", args[1] +"/test"};
        ArrangeInput.main(args1);
        Main.main(args2);
    }
}
