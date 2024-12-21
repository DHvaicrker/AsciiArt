package ascii_art;

import java.io.IOException;
import java.util.TreeSet;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageUtility;
import image_char_matching.SubImgCharMatcher;

/**
 * The Shell class acts as the command-line interface for generating ASCII art.
 */
public class Shell {
    private static final String
            INCORRECT_RESOLUTION_FORMAT_ERROR_MSG =
            "Did not change resolution due to incorrect format.";
    // Constants
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
    private static final String SPACE = "space";
    private static final char SPACE_CHAR = ' ';
    private static final String OUTPUT_ERROR_MSG =
            "Did not change output method due to incorrect format.";
    private static final String SPACE_SPERATOR = " ";
    private static final int SMALLEST_ASCII_CHAR = 32;
    private static final int LARGEST_ASCII_CHAR = 126;
    private static final int FIRST_CHAR_INDEX = 0;
    private static final String RANGE_REGEX = ".-.";
    private static final int NUM_OF_CHAR_IN_RANGE = 3;
    private static final int SECOND_CHAR_INDEX = 2;
    private static final String ADD_ERROR_MSG = "Did not add due to incorrect format.";
    private static final String ADD_ALL_COMMAND = "all";
    private static final String IMG_LOADING_ERROR_MSG = "Error loading image: ";
    private static final String RESOLUTION_STATUS_MSG = "Resolution set to %d.%n";
    private static final String ILLIGAL_COMMAD_ERROR_MSG = "Did not execute due to incorrect command.";
    private static final int MIN_NUM_OF_CHARS = 2;
    private static final String CHARSET_TOO_SMALL_ERROR_MSG = "Did not execute. Charset is too small.";
    private static final String OUTPUT_FILE_NAME = "out.html";
    private static final String OUTPUT_FILE_FONT = "Courier New";
    private static final String INCREASE_RESOLUTION = "up";
    private static final String DECREASE_RESOLTUION = "down";
    private static final String
            RESOLUTION_EXCEEDING_BOUNDARIES_ERROR_MSG =
            "Did not change resolution due to exceeding boundaries.";
    private static final int MIN_CHARS_IN_ROW = 1;
    private static final String ROUNDING_ERROR_MSG =
            "Did not change rounding method due to incorrect format.";
    private static final String REMOVE_ERROR_MSG = "Did not remove due to incorrect format.";

    // Attributes
    private static boolean isHtmlOutput = false; // Default output is console
    private int resolution = DEFAULT_RES;
    private final SubImgCharMatcher subImgCharMatcher;
    private AsciiArtAlgorithm asciiArtAlgorithm;
    private Image image;

    /**
     * Initializes a new Shell instance with a default character set.
     */
    public Shell() {
        subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHAR_SET);
    }

    /**
     * Changes the output method between "html" and "console".
     *
     * @param output the desired output method
     * @throws InvalidCommandException if the output method is invalid
     */
    private void changeOutput(String output) throws InvalidCommandException {
        if (output.equals("html")) {
            isHtmlOutput = true;
        } else if (output.equals("console")) {
            isHtmlOutput = false;
        } else {
            throw new InvalidCommandException(OUTPUT_ERROR_MSG);
        }
    }

    /**
     * Loads and prepares the image for processing.
     *
     * @param imagePath the path to the image file
     * @throws IOException if the image cannot be loaded
     */
    private void imageHandling(String imagePath) throws IOException {
        image = new Image(imagePath);
        image = ImageUtility.imagePadding(image);
        asciiArtAlgorithm = new AsciiArtAlgorithm(image, DEFAULT_RES, subImgCharMatcher);
    }

    /**
     * Displays the current character set.
     */
    private void showChars() {
        TreeSet<Character> charSet = subImgCharMatcher.getCharSet();
        for (Character c : charSet) {
            System.out.print(c + SPACE_SPERATOR);
        }
        System.out.println();
    }

    /**
     * Modifies the character set by adding or removing characters.
     *
     * @param strArg      the character(s) to add or remove
     * @param isDeleteMode true if removing characters, false if adding
     * @throws InvalidCommandException if the input format is invalid
     */
    private void modifyCharSet
    (String strArg, boolean isDeleteMode) throws InvalidCommandException {
        if (strArg.length() == 1) {
            char charToModify = strArg.charAt(FIRST_CHAR_INDEX);
            if (charToModify < SMALLEST_ASCII_CHAR || charToModify > LARGEST_ASCII_CHAR) {
                throwModifyException(isDeleteMode);
            }
            executeModificationCharSet(charToModify, isDeleteMode);
        } else if (strArg.matches(RANGE_REGEX) && strArg.length() == NUM_OF_CHAR_IN_RANGE) {
            char firstChar =
                    (char) Math.min(strArg.charAt(FIRST_CHAR_INDEX), strArg.charAt(SECOND_CHAR_INDEX));
            char secondChar =
                    (char) Math.max(strArg.charAt(FIRST_CHAR_INDEX), strArg.charAt(SECOND_CHAR_INDEX));
            for (char c = firstChar; c <= secondChar; c++) {
                executeModificationCharSet(c, isDeleteMode);
            }
        } else if (strArg.equals(ADD_ALL_COMMAND)) {
            for (int charToModify =
                 SMALLEST_ASCII_CHAR; charToModify <= LARGEST_ASCII_CHAR; charToModify++) {
                executeModificationCharSet((char) charToModify, isDeleteMode);
            }
        } else if (strArg.equals(SPACE)) {
            executeModificationCharSet(SPACE_CHAR, isDeleteMode);
        } else {
            throwModifyException(isDeleteMode);
        }
    }
    private void throwModifyException(boolean isDeleteMode) throws InvalidCommandException {
        if (isDeleteMode) {
            throw new InvalidCommandException(REMOVE_ERROR_MSG);
        } else {
            throw new InvalidCommandException(ADD_ERROR_MSG);
        }

    }

    /**
     * Adds or removes a character in the character set.
     *
     * @param charToModify the character to add or remove
     * @param isDeleteMode true if removing, false if adding
     */
    private void executeModificationCharSet(char charToModify, boolean isDeleteMode) {
        if (isDeleteMode) {
            subImgCharMatcher.removeChar(charToModify);
        } else {
            if (!subImgCharMatcher.containsChar(charToModify)) {
                subImgCharMatcher.addChar(charToModify);
            }
        }
    }

    /**
     * Runs the Shell program.
     *
     * @param name the path to the image file
     */
    public void run(String name) {
        try {
            imageHandling(name);
        } catch (IOException e) {
            System.err.println(IMG_LOADING_ERROR_MSG + e.getMessage());
            return;
        }

        while (true) {
            System.out.print(COMMAND_PREFIX);
            String[] userInput = KeyboardInput.readLine().split(SPACE_REGEX);
            String command = userInput[COMMAND_IDX];

            try {
                switch (command) {
                    case EXIT_PROGRAM_COMMAND:
                        return;
                    case SHOW_CHARS_COMMAND:
                        showChars();
                        break;
                    case ADD_CHAR_COMMAND:
                        if (userInput.length == 1) {
                            System.out.println(ADD_ERROR_MSG);
                        } else {
                            modifyCharSet(userInput[ARG1_IDX], false);
                        }
                        break;
                    case REMOVE_CHAR_COMMAND:
                        if (userInput.length == 1) {
                            System.out.println(REMOVE_ERROR_MSG);
                        } else {
                            modifyCharSet(userInput[ARG1_IDX], true);
                        }
                        break;
                    case CHANGE_RES_COMMAND:
                        if (userInput.length == 1) {
                            System.out.printf(RESOLUTION_STATUS_MSG, resolution);
                        } else {
                            changeResolution(userInput[ARG1_IDX]);
                        }
                        break;
                    case CHANGE_OUTPUT_COMMAND:
                        if (userInput.length == 1) {
                            System.out.println(OUTPUT_ERROR_MSG);
                        } else {
                        changeOutput(userInput[ARG1_IDX]);
                        }
                        break;
                    case ROUND_METHOD_COMMAND:
                        if (userInput.length == 1) {
                            System.out.println(ROUNDING_ERROR_MSG);
                        } else {
                            subImgCharMatcher.setRoundMethod(userInput[ARG1_IDX]);
                        }
                        break;
                    case RUN_ALGORITHM_COMMAND:
                        runAlgorithm();
                        break;
                    default:
                        System.out.println(ILLIGAL_COMMAD_ERROR_MSG);
                }
            } catch (InvalidCommandException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Executes the ASCII art generation algorithm.
     *
     * @throws InvalidCommandException if the character set is too small
     */
    private void runAlgorithm() throws InvalidCommandException {
        if (subImgCharMatcher.getCharSet().size() < MIN_NUM_OF_CHARS) {
            throw new InvalidCommandException(CHARSET_TOO_SMALL_ERROR_MSG);
        }
        char[][] result = asciiArtAlgorithm.run();
        if (isHtmlOutput) {
            HtmlAsciiOutput output = new HtmlAsciiOutput(OUTPUT_FILE_NAME, OUTPUT_FILE_FONT);
            output.out(result);
        } else {
            ConsoleAsciiOutput output = new ConsoleAsciiOutput();
            output.out(result);
        }
    }

    /**
     * Changes the resolution of the ASCII art.
     *
     * @param changeFactor the resolution change direction ("up" or "down")
     */
    private void changeResolution(String changeFactor) {
        // increase resolution
        if (changeFactor.equals(INCREASE_RESOLUTION)) {
            // check if resolution can be increased
            if (resolution * 2 > image.getHeight() || resolution * 2 > image.getWidth()) {
                System.out.println(RESOLUTION_EXCEEDING_BOUNDARIES_ERROR_MSG);
                return;
            }
            resolution *= 2;
            // decrease resolution
        } else if (changeFactor.equals(DECREASE_RESOLTUION)) {
            // check if resolution can be decreased
            if (resolution / 2 < minCharsInRow()) {
                System.out.println(RESOLUTION_EXCEEDING_BOUNDARIES_ERROR_MSG);
                return;
            }
            resolution /= 2;
        } else {
            System.out.println(INCORRECT_RESOLUTION_FORMAT_ERROR_MSG);
            return;
        }
        asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution,
                subImgCharMatcher);
        System.out.printf(RESOLUTION_STATUS_MSG, resolution);
    }

    /**
     * Calculates the minimum number of characters per row based on the image dimensions.
     *
     * @return the minimum number of characters per row
     */
    private int minCharsInRow() {
        return Math.max(image.getWidth() / image.getHeight(), MIN_CHARS_IN_ROW);
    }

    /**
     * The main entry point for the Shell program.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run(args[IMAGE_PATH_INDEX]);
    }
}

/**
 * Exception class for handling invalid commands.
 */
class InvalidCommandException extends Exception {
    public InvalidCommandException(String message) {
        super(message);
    }
}
