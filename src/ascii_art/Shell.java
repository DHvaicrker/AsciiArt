package ascii_art;

public class Shell {

    public static final int IMAGE_PATH_INDEX = 0;
    public static final int COMMAND_IDX = 0;
    public static final String COMMAND_PREFIX = ">>> ";
    public static final String SPACE_REGEX = "\\s+";
    public static final String EXIT_PROGRAM_COMMAND = "exit";
    public static final String SHOW_CHARS_COMMAND = "chars";
    public static final String ADD_CHAR_COMMAND = "add";
    public static final String REMOVE_CHAR_COMMAND = "remove";
    public static final String CHANGE_RES_COMMAND = "res";
    public static final String CHANGE_OUTPUT_COMMAND = "output";
    public static final String ROUND_METHOD_COMMAND = "round";
    public static final String RUN_ALGORITHM_COMMAND = "asciiArt";

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
