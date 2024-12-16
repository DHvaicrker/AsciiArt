package ascii_art;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.TreeSet;
import java.util.Arrays;

import image.Image;
import image_char_matching.SubImgCharMatcher;


public class Shell {

    // constants
    private static final int IMAGE_PATH_INDEX = 0;
    private static final int COMMAND_IDX = 0;
    private static final int ARG1_IDX = 1;
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
    private static final char[] DEFAULT_CHAR_SET = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    private static final int DEFAULT_RES = 2;
    private static final String ABS_ROUND_METHOD = "abs";

    // attributes
    private int resolution = DEFAULT_RES;
    private String round_method = ABS_ROUND_METHOD;

    private final SubImgCharMatcher subImgCharMatcher;
    private AsciiArtAlgorithm asciiArtAlgorithm;

    public Shell() {
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
    }

    private void imageHandling(String imagePath) throws IOException {
        Image image = new Image(imagePath);
        asciiArtAlgorithm = new AsciiArtAlgorithm(image, DEFAULT_RES,
                subImgCharMatcher);
    }

    public void run(String name) {
        // namefile handling
        try {
            imageHandling(name);
        } catch (IOException e) {
            System.out.println("yo");
            return;
        }

        // while not exit, keep running
        while (true) {
            System.out.print(COMMAND_PREFIX);
            String[] userInput = KeyboardInput.readLine().split(SPACE_REGEX);
            String command = userInput[COMMAND_IDX];
            String arg1 = userInput[ARG1_IDX];
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
                    changeResolution(arg1);
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

    private void changeResolution(String changeFactor) {
        if (changeFactor.equals("up")) {
            resolution *= 2;
        } else if (changeFactor.equals("down")) {
            resolution /= 2;
        }
        System.out.printf("Resolution set to %d.\n", resolution);
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[IMAGE_PATH_INDEX]);
    }
}
