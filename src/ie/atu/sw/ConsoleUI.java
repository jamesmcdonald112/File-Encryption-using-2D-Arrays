package ie.atu.sw;

import java.util.Arrays;
import java.util.Scanner;

/**
 * The {@code ConsoleUI} class is responsible for managing the user interaction
 * in the console. It provides methods to display messages, ask for user input, and
 * handle different prompts related to encryption and decryption processes.
 *
 * <p><b>Methods:</b></p>
 * <ul>
 *     <li>{@link ##printProgress(int index, int total)}</li>
 *     <li>{@link #askUserToChangeSettings()}</li>
 *     <li>{@link #displayDecryptionStarted()}</li>
 *     <li>{@link #displayEncryptionStarted()}</li>
 *     <li>{@link #displayErrorMessage(String)}</li>
 *     <li>{@link #displaySuccessMessage(String)}</li>
 *     <li>{@link #displayCurrentSetting(String, String, char[])}</li>
 *     <li>{@link #displayMessage(String)}</li>
 *     <li>{@link #getDirectoryName()}</li>
 *     <li>{@link #getKeyName()}</li>
 *     <li>{@link #performActionIfUserConfirms(String)}</li>
 *     <li>{@link #createOrSelectDirectory(String)}</li>
 *     <li>{@link #getUserConfirmation(String)}</li>
 *     <li>{@link #closeScanner()}</li>
 * </ul>
 *
 * <p><b>Constants:</b></p>
 * <ul>
 *     <li>{@link #CHANGE_DIRECTORY_PROMPT}</li>
 *     <li>{@link #CHANGE_KEY_PROMPT}</li>
 *     <li>{@link #CREATE_OR_SELECT_DIRECTORY_PROMPT}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 */
public class ConsoleUI {

    /**
     * Scanner object to read user input throughout the application.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Constant prompt asking the user if they would like to change the directory.
     */
    public static final String CHANGE_DIRECTORY_PROMPT = "Would you like to change the directory? (y/n)";

    /**
     * A Constant prompt message used to ask the user whether they want to change the key.
     */
    public static final String CHANGE_KEY_PROMPT = "Would you like to change the key? (y/n)";

    /**
     * Constant prompt asking the user if they would like to create a new output directory, select an existing one,
     * or return to the previous menu. The available options are 'c' (create), 's' (select existing), or 'r' (return).
     */
    public static final String CREATE_OR_SELECT_DIRECTORY_PROMPT = "Would you like to create a new output " +
            "directory (c), select an existing output directory (s) or return (r)?";

    /*
     *  Terminal Progress Meter
     *  -----------------------
     *  You might find the progress meter below useful. The progress effect
     *  works best if you call this method from inside a loop and do not call
     *  System.out.println(....) until the progress meter is finished.
     *
     *  Please note the following carefully:
     *
     *  1) The progress meter will NOT work in the Eclipse console, but will
     *     work on Windows (DOS), Mac and Linux terminals.
     *
     *  2) The meter works by using the line feed character "\r" to return to
     *     the start of the current line and writes out the updated progress
     *     over the existing information. If you output any text between
     *     calling this method, i.e. System.out.println(....), then the next
     *     call to the progress meter will output the status to the next line.
     *
     *  3) If the variable size is greater than the terminal width, a new line
     *     escape character "\n" will be automatically added and the meter won't
     *     work properly.
     *
     *
     */
    public static void printProgress(int index, int total) {
        if (index > total) return;    //Out of range
        int size = 50;                //Must be less than console width
        char done = '█';            //Change to whatever you like.
        char todo = '░';            //Change to whatever you like.

        //Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;

        /*
         * A StringBuilder should be used for string concatenation inside a
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append((i < completeLen) ? done : todo);
        }

        /*
         * The line feed escape character "\r" returns the cursor to the
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");

        //Once the meter reaches its max, move to a new line.
        if (done == total) System.out.println("\n");
    }

    public static void progressMeter() {
        //You may want to include a progress meter in you assignment!
        System.out.print(ConsoleColour.YELLOW);    //Change the colour of the console text
        int size = 100;                            //The size of the meter. 100 equates to 100%
        for (int i = 0; i < size; i++) {        //The loop equates to a sequence of processing steps
            ConsoleUI.printProgress(i + 1, size);        //After each (some) steps, update the progress meter
            try {
                Thread.sleep(10);                    //Slows things down so the animation is visible
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(ConsoleColour.RESET);
    }


    /**
     * Asks the user if they would like to change any settings.
     *
     * @return {@code true} if the user wants to change settings, {@code false} otherwise.
     */
    public static boolean askUserToChangeSettings() {
        return performActionIfUserConfirms("Would you like to change any setting? (y/n)");
    }

    /**
     * Displays a message indicating that the decryption process has started.
     */
    public static void displayDecryptionStarted() {
        displaySuccessMessage("Decrypting...");
    }

    /**
     * Displays a message indicating that the encryption process has started.
     */
    public static void displayEncryptionStarted() {
        displaySuccessMessage("Encrypting...");
    }

    /**
     * Displays an error message provided as an argument.
     *
     * @param message The error message to be displayed
     */
    public static void displayErrorMessage(String message) {
        System.out.println(ConsoleColour.RED_BOLD);
        System.out.println(message);
        System.out.println(ConsoleColour.RESET);
    }

    /**
     * Displays a success message provided as an argument.
     *
     * @param message The success message to be displayed
     */
    public static void displaySuccessMessage(String message) {
        System.out.println(ConsoleColour.GREEN_BOLD);
        System.out.println(message);
        System.out.println(ConsoleColour.RESET);
    }

    /**
     * Displays the current settings, including input directory, output directory, and original key.
     *
     * @param inputDirectory  The path of the input directory
     * @param outputDirectory The path of the output directory
     * @param originalKey     The original key used for encryption/ decryption.
     */
    public static void displayCurrentSetting(String inputDirectory, String outputDirectory, char[] originalKey) {
        System.out.println(ConsoleColour.GREEN_BOLD);
        System.out.println("Current input directory: " + inputDirectory);
        System.out.println("Current output directory: " + outputDirectory);
        System.out.println("Current key: " + Arrays.toString(originalKey));
        System.out.println(ConsoleColour.RESET);
    }

    /**
     * Displays a general message provided as an argument.
     *
     * @param message The message to be displayed.
     */
    public static void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prompts the user to enter the name of the directory.
     *
     * @return The name of the directory entered by the user.
     */
    public static String getDirectoryName() {
        System.out.println("Enter the name of the directory or press 'q' to quit");
        return scanner.nextLine();
    }

    public static boolean shouldUserQuit(String userInput) {
        return userInput.equalsIgnoreCase("q");
    }

    /**
     * Prompts the user to select a key name, ensuring that the length is between 5-16 characters and without duplicates.
     *
     * @return The key name entered by the user.
     */
    public static String getKeyName() {
        System.out.println("Please select a key length between 5-16 characters in length with no duplicate characters.");
        return scanner.nextLine();
    }

    /**
     * Confirms an action with the user by displaying a provided message.
     *
     * @param message The message to be displayed for confirmation.
     * @return {@code true} if the user confirms, {@code false} otherwise.
     */
    public static boolean performActionIfUserConfirms(String message) {
        return getUserConfirmation(message);
    }

    /**
     * Prompts the user to create or select a directory based on a given prompt, or return without making a selection.
     *
     * @param prompt The message to be displayed for the user.
     * @return A string representing the user's choice ('c' for create, 's' for select, 'r' for return).
     */
    public static String createOrSelectDirectory(String prompt) {
        while (true) {
            System.out.println(prompt);
            String userInput = scanner.nextLine().toLowerCase().trim();
            if (userInput.isEmpty()) {
                System.out.println("No input provided. Please enter 'c' (create), 's' (select existing) " +
                        "or 'r' (return).");
            }
            switch (userInput) {
                case "c" -> {
                    return "c";
                }
                case "s" -> {
                    return "s";
                }
                case "r" -> {
                    return "r";
                }
                default -> System.out.println("Invalid input. Please enter 'c' (create), 's' (select existing) " +
                        "or 'r' (return).");

            }
        }

    }

    /**
     * Prompts the user for confirmation with a given message.
     *
     * @param message The message to be displayed for confirmation.
     * @return {@code true} if the user confirms, {@code false} otherwise.
     */
    public static boolean getUserConfirmation(String message) {
        while (true) {
            System.out.println(message);
            String userInput = scanner.nextLine().toLowerCase();

            if (userInput.isEmpty()) {
                System.out.println("No input provided. Please enter 'y' or 'n'.");
            }
            switch (userInput) {
                case "y" -> {
                    return true;
                }
                case "n" -> {
                    return false;
                }
                default -> System.out.println("Invalid input. Please enter 'y' or 'n'.");

            }
        }
    }

    /**
     * Closes the scanner used for reading user input.
     */
    public static void closeScanner() {
        try {
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error when closing scanner: " + e.getMessage());
        }

    }
}
