package ascii_art;

import java.util.List;
import java.util.TreeSet;
import java.util.Arrays;

public class Shell {

    // constants
    private static final int IMAGE_PATH_INDEX = 0;
    private static final int COMMAND_IDX = 0;
    private static final String COMMAND_PREFIX = ">>> ";
    private static final String SPACE_REGEX = "\\s+";

    private static final String EXIT_PROGRAM_COMMAND = "exit";
    private static final String SHOW_CHARS_COMMAND = "chars";
    private static final String ADD_CHAR_COMMAND = "add";
    private static final String REMOVE_CHAR_COMMAND = "remove";
    private static final String CHANGE_RES_COMMAND = "res";
    private static final String CHANGE_OUTPUT_COMMAND = "output";
    private static final String ROUND_METHOD_COMMAND = "round";
    private static final String RUN_ALGORITHM_COMMAND = "asciiArt";
    private static final List<Character> DEFAULT_CHAR_SET = Arrays.asList('1',
            '2', '3', '4', '5', '6', '7', '8', '9', '0');
    private static final int DEFAULT_RES = 2;
    private static final String ABS_ROUND_METHOD = "abs";

    // attributes
    private TreeSet<Character> charSet = new TreeSet<>(DEFAULT_CHAR_SET);
    private int resultion = DEFAULT_RES;
    private String round_method = ABS_ROUND_METHOD;

    public Shell() {
    }

    public void run(String name) {
        // namefile handling

        // while not exit, keep running
        while (true) {
            System.out.print(COMMAND_PREFIX);
            String command = KeyboardInput.readLine().split(SPACE_REGEX)[COMMAND_IDX];
            System.out.println(command);
            switch (command) {
                case EXIT_PROGRAM_COMMAND:
                    System.out.println("EXIT_PROGRAM_COMMAND");
                    return;
                case SHOW_CHARS_COMMAND:
                    System.out.println("SHOW_CHARS_COMMAND");
                    break;
                case ADD_CHAR_COMMAND:
                    System.out.println("ADD_CHAR_COMMAND");
                    break;
                case REMOVE_CHAR_COMMAND:
                    System.out.println("REMOVE_CHAR_COMMAND");
                    break;
                case CHANGE_RES_COMMAND:
                    System.out.println("CHANGE_RES_COMMAND");
                    break;
                case CHANGE_OUTPUT_COMMAND:
                    System.out.println("CHANGE_OUTPUT_COMMAND");
                    break;
                case ROUND_METHOD_COMMAND:
                    System.out.println("ROUND_METHOD_COMMAND");
                    break;
                case RUN_ALGORITHM_COMMAND:
                    System.out.println("RUN_ALGORITHM_COMMAND");
                    break;
                default:
                    System.out.println("Invalid command");
                    break;
            }
        }

    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[IMAGE_PATH_INDEX]);
    }
}
