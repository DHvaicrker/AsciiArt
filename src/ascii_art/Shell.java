package ascii_art;

import java.io.IOException;
import java.util.TreeSet;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageManager;
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
    private static final String ROUND_ROUND_METHOD = "abs";
    public static final String SPACE = "space";
    public static final char SPACE_CHAR = ' ';
    private static boolean is_html_output = false; // the default is the console output

    // attributes
    private int resolution = DEFAULT_RES;
    private String round_method = ABS_ROUND_METHOD;

    private final SubImgCharMatcher subImgCharMatcher;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    // Image is padded from the beginning.
    private Image image;

    public Shell() {
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
    }

    private void changeOutput(String output) throws IllegalArgumentException {
        if (output.equals("html")) {
            is_html_output = true;
        } else if (output.equals("console")) {
            is_html_output = false;
        } else {
            throw new IllegalArgumentException("Did not change output method due to incorrect format.");

        }
    }

    private void imageHandling(String imagePath) throws IOException {
        image = new Image(imagePath);
        image = ImageManager.imagePadding(image);
        asciiArtAlgorithm = new AsciiArtAlgorithm(image, DEFAULT_RES,
                subImgCharMatcher);
    }

    private void showChars() {
        TreeSet<Character> charSet = subImgCharMatcher.getCharSet();
        for (Character c : charSet) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private void modifyCharSet(String strArg, boolean isDeleteMode) throws IllegalArgumentException {
        // add <char> command handling
        if (strArg.length() == 1) {
            char charToModify = strArg.charAt(0);
            if (charToModify < 32 || charToModify > 126) {
                throw new IllegalArgumentException("Did not add due to incorrect format.");
            }
            executeModificationCharSet(charToModify, isDeleteMode);
            // Add range of Chars
        } else if (strArg.matches(".-.") && strArg.length() == 3) {
            char firstChar = (char) Math.min((int) strArg.charAt(0), (int) strArg.charAt(2));
            char secondChar = (char) Math.max((int) strArg.charAt(0), (int) strArg.charAt(2));
            for (char c = firstChar; c <= secondChar; c++) {
                executeModificationCharSet(c, isDeleteMode);
            }
            // Add every char
        } else if (strArg.equals("all")) { // add all command handling
            for (int charToModify = 32; charToModify <= 126; charToModify++) {
                executeModificationCharSet((char) charToModify, isDeleteMode);
            }
            // Add Space char
        } else if (strArg.equals(SPACE)) {
            executeModificationCharSet(SPACE_CHAR, isDeleteMode);
        } else {
            throw new IllegalArgumentException("Did not add due to incorrect format.");
        }
    }

    private void executeModificationCharSet(char charToModify,
                                            boolean isDeleteMode) {
        if (isDeleteMode) {
            subImgCharMatcher.removeChar(charToModify);
        } else {
            if (!subImgCharMatcher.containsChar(charToModify)) {
                subImgCharMatcher.addChar(charToModify);
            }
        }
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
            System.out.println(command);
            switch (command) {
                case EXIT_PROGRAM_COMMAND:
                    System.out.println("EXIT_PROGRAM_COMMAND");
                    return;
                case SHOW_CHARS_COMMAND:
                    showChars();
                    System.out.println("SHOW_CHARS_COMMAND");
                    break;
                case ADD_CHAR_COMMAND:
                    try {
                        modifyCharSet(userInput[ARG1_IDX], false);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("ADD_CHAR_COMMAND");
                    break;
                case REMOVE_CHAR_COMMAND:
                    try {
                        modifyCharSet(userInput[ARG1_IDX], true);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("REMOVE_CHAR_COMMAND");
                    break;
                case CHANGE_RES_COMMAND:
                    if (userInput.length == 1) {
                        System.out.printf("Resolution set to %d.\n", resolution);
                    } else {
                        changeResolution(userInput[ARG1_IDX]);
                    }
                    System.out.println("CHANGE_RES_COMMAND");
                    break;
                case CHANGE_OUTPUT_COMMAND:
                    if (userInput.length == 1) {
                        System.out.println("Did not change output method due to incorrect format.");
                    } else {
                        try {
                            changeOutput(userInput[ARG1_IDX]);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    System.out.println("CHANGE_OUTPUT_COMMAND");
                    break;
                case ROUND_METHOD_COMMAND:
                    try {
                        subImgCharMatcher.setRoundMethod(userInput[ARG1_IDX]);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("ROUND_METHOD_COMMAND");
                    break;
                case RUN_ALGORITHM_COMMAND:
                    try {
                        runAlgorithm();
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println("RUN_ALGORITHM_COMMAND");
                    break;
                default:
                    System.out.println("Did not execute due to incorrect command.");
                    break;
            }
        }

    }

    private void runAlgorithm() throws IllegalArgumentException {
        if (subImgCharMatcher.getCharSet().size() < 2) {
            throw new IllegalArgumentException("Did not execute. Charset is too small.");
        }
        char[][] result = asciiArtAlgorithm.run();
        if (is_html_output) {
            HtmlAsciiOutput output = new HtmlAsciiOutput("out" +
                    ".html", "Courier New");
            output.out(result);
        } else {
            ConsoleAsciiOutput output = new ConsoleAsciiOutput();
            output.out(result);
        }
    }

    private void changeResolution(String changeFactor) {
        // increase resolution
        if (changeFactor.equals("up")) {
            // check if resolution can be increased
            if (resolution * 2 > image.getHeight() || resolution * 2 > image.getWidth()) {
                System.out.println("Did not change resolution due to exceeding boundaries.");
                return;
            }
            resolution *= 2;
            // decrease resolution
        } else if (changeFactor.equals("down")) {
            // check if resolution can be decreased
            if (resolution / 2 < minCharsInRow()) {
                System.out.println("Did not change resolution due to exceeding boundaries.");
                return;
            }
            resolution /= 2;
        } else {
            System.out.println("Did not change resolution due to incorrect format.");
            return;
        }
        asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution,
                subImgCharMatcher);
        System.out.printf("Resolution set to %d.\n", resolution);
    }

    private int minCharsInRow() {
        return Math.max(image.getWidth() / image.getHeight(), 1);
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[IMAGE_PATH_INDEX]);
    }
}
