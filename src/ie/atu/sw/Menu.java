package ie.atu.sw;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The {@code Menu} class represents a menu interface for interacting with an ADFGVX cipher-based encryption
 * and decryption system. It provides options to specify input/output directories, manage encryption keys,
 * encrypt/decrypt files, and more.
 *
 * <p><b>Constructors:</b></p>
 * <ul>
 *     <li>{@link #Menu()}</li>
 * </ul>
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #startMenu()}</li>
 *     <li>{@link #closeScanner()}</li>
 * </ul>
 *
 * <p><b>Private Methods:</b></p>
 * <ul>
 *     <li>{@link #displayMainMenu()}</li>
 *     <li>{@link #userMenuChoice()}</li>
 *     <li>{@link #returnToMenu()}</li>
 *     <li>{@link #handleInputDirectory()}</li>
 *     <li>{@link #handleOutputDirectory()}</li>
 *     <li>{@link #handleSetKey()}</li>
 *     <li>{@link #handleEncrypt()}</li>
 *     <li>{@link #handleDecrypt()}</li>
 *     <li>{@link #handleOptions()}</li>
 *     <li>{@link #optionsMenuChoice()}</li>
 *     <li>{@link #displayOptionsMenu()}</li>
 *     <li>{@link #viewSettings()}</li>
 *     <li>{@link #clearSettings()}</li>
 *     <li>{@link #returnToOptionsMenu()}</li>
 *     <li>{@link #returnToMenu()}</li>
 * </ul>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #inputDirectoryManager}</li>
 *     <li>{@link #outputDirectoryManager}</li>
 *     <li>{@link #keyManager}</li>
 *     <li>{@link #encryptionManager}</li>
 *     <li>{@link #decryptionManager}</li>
 *     <li>{@link #scanner}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see InputDirectoryManager
 * @see OutputDirectoryManager
 * @see KeyManager
 * @see EncryptionManager
 * @see DecryptionManager
 * @see CipherManager
 * @see Scanner
 */
public class Menu {

    /**
     * Manages the directory where input files are located for encryption or decryption.
     */
    private final InputDirectoryManager inputDirectoryManager;

    /**
     * Manages the directory where output files are saved after encryption or decryption.
     */
    private final OutputDirectoryManager outputDirectoryManager;

    /**
     * Handles the management and storage of encryption and decryption keys.
     */
    private final KeyManager keyManager;

    /**
     * Responsible for handling the encryption process, including file reading, encoding, and saving.
     */
    private final EncryptionManager encryptionManager;

    /**
     * Responsible for handling the decryption process, including file reading, decoding, and saving.
     */
    private final DecryptionManager decryptionManager;

    /**
     * Scanner used to read user input from the console.
     */
    private final Scanner scanner;

    /**
     * Constructor initialising all the required components of the menu.
     */
    public Menu() {
        // Handles input directory operations
        this.inputDirectoryManager = new InputDirectoryManager();
        // Handles output directory operations
        this.outputDirectoryManager = new OutputDirectoryManager();
        // Manages keys for encryption/ decryption
        this.keyManager = new KeyManager();
        // Handles cipher operations
        // Instance Variables
        CipherManager cipherManager = new CipherManager(inputDirectoryManager, outputDirectoryManager, keyManager);
        this.encryptionManager = new EncryptionManager(cipherManager);
        this.decryptionManager = new DecryptionManager(cipherManager);

        // Scanner for user input
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the menu by displaying options and handling user choice.
     */
    public void startMenu() {
        displayMainMenu();
        userMenuChoice(); // Handle user choice and take appropriate actions

    }

    /**
     * Closes the scanner object, releasing the associated resources
     */
    public void closeScanner() {
        scanner.close();
    }

    /**
     * Displays the main menu to the user, showing available options.
     */
    private void displayMainMenu() {
        System.out.println(ConsoleColour.WHITE);
        System.out.println("************************************************************");
        System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
        System.out.println("*                                                          *");
        System.out.println("*                   ADFGVX File Encryption                 *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Specify Input File Directory");
        System.out.println("(2) Specify Output File Directory");
        System.out.println("(3) Set Key");
        System.out.println("(4) Encrypt");
        System.out.println("(5) Decrypt");
        System.out.println("(6) Options"); //Add as many menu items as you like.
        System.out.println("(7) Quit");

        //Output a menu of options and solicit text from the user
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Select Option [1-7]>");
        System.out.println();

        ConsoleUI.progressMeter();
    }

    /**
     * Handles user input for selecting an option from the main menu.
     * Allows user to interact with different features like handling input/output directories,
     * encryption, decryption, and other options.
     */
    private void userMenuChoice() {
        boolean running = true;

        while (running) {
            try {
                int menuChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left over

                switch (menuChoice) {
                    case 1 -> handleInputDirectory(); // Handle specifying the input directory
                    case 2 -> handleOutputDirectory(); // Handle specifying the output directory
                    case 3 -> handleSetKey(); // Handle setting the encryption key
                    case 4 -> handleEncrypt(); // Handle encrypting the files
                    case 5 -> handleDecrypt(); // Handle decrypting the files
                    case 6 -> handleOptions(); // Handle additional options
                    case 7 -> {
                        closeScanner(); // Close the scanner when quiting
                        ConsoleUI.displaySuccessMessage("Quiting...Goodbye!");
                        running = false; // Exit the loop to terminate the program
                    }
                    default -> ConsoleUI.displayErrorMessage("Invalid choice. Please select an option from the Menu");
                }
            } catch (InputMismatchException inputMismatchException) {
                ConsoleUI.displayErrorMessage("Invalid input. Please enter an integer value between 1-7");
                scanner.nextLine(); //consume token
            }
        }

    }

    /**
     * Handles the action for input file directory option from the menu.
     * Delegates the task to the input directory manager and then returns to the main menu.
     */
    private void handleInputDirectory() {
        inputDirectoryManager.handleInputDirectoryManager();
        returnToMenu();
    }

    /**
     * Handles the action for output file directory option from the menu.
     * * Delegates the task to the output directory manager and then returns to the main menu.
     */
    private void handleOutputDirectory() {
        outputDirectoryManager.handleOutputDirectoryManager();
        returnToMenu();
    }

    /**
     * Handles the action for setting the encryption/decryption key.
     * Delegates the task to the key manager and then returns to the main menu.
     */
    private void handleSetKey() {
        keyManager.handleKeyManagement();
        returnToMenu();
    }

    /**
     * Handles the action for encrypting the content based on the specified key and input directory.
     * Delegates the task to the cipher manager and then returns to the main menu.
     */
    private void handleEncrypt() {
        encryptionManager.handleEncryptionManagement();
        returnToMenu();
    }

    /**
     * Handles the action for decrypting the content based on the specified key and input directory.
     * Delegates the task to the cipher manager and then returns to the main menu.
     */
    private void handleDecrypt() {
        decryptionManager.handleDecryptionManagement();
        returnToMenu();
    }

    /**
     * Handles the display and interactions with the options menu.
     * Displays the options menu and then waits for the user to make a selection.
     */
    private void handleOptions() {
        displayOptionsMenu();
        optionsMenuChoice();
        returnToMenu();
    }

    /**
     * Handles the user's choice in the options' menu.
     * Allows the user to view settings, clear settings, or exit the options' menu.
     */
    private void optionsMenuChoice() {
        boolean running = true;
        while (running)
            try {
                int userChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline left over
                switch (userChoice) {
                    case 1 -> viewSettings();
                    case 2 -> viewInputDirectoryFiles();
                    case 3 -> viewOutputDirectoryFiles();
                    case 4 -> clearSettings();
                    case 5 -> running = false;
                    default -> System.out.println("Invalid choice. Please select an option from 1-5");
                }
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Invalid input. Please enter an integer value between 1-5");
                scanner.nextLine(); //consume token
            }
    }

    /**
     * Displays the options menu to the user.
     * Provides the user with three choices: viewing the settings, clearing the settings, or returning to the main menu.
     * Also includes an animated progress meter for visual feedback.
     */
    private void displayOptionsMenu() {
        System.out.println(ConsoleColour.WHITE);
        System.out.println("(1) View Settings");
        System.out.println("(2) View Input Directory Files");
        System.out.println("(3) View Output Directory Files");
        System.out.println("(4) Clear Settings");
        System.out.println("(5) Return to Main Menu");

        //Output a menu of options and obtain text from the user
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Select Option [1-5]>");
        System.out.println();


        // Include a progress meter for visual feedback
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
    }

    /**
     * Displays the current settings to the user.
     * Shows the current input directory, output directory, and key if they are set.
     * If any of these settings are not set, an appropriate message is displayed.
     */
    private void viewSettings() {
        // Check if the input directory is set and display the appropriate message
        if (inputDirectoryManager.getInputDirectory() != null) {
            ConsoleUI.displaySuccessMessage("The current input directory selected is: " + inputDirectoryManager.getInputDirectory());
        } else {
            System.out.println("The input directory is not set");
        }

        // Check if the output directory is set and display the appropriate message
        if (outputDirectoryManager.getOutputDirectory() != null) {
            ConsoleUI.displaySuccessMessage("The current output directory selected is: " + outputDirectoryManager.getOutputDirectory());
        } else {
            System.out.println("The output directory is not set.");
        }

        // Check if the key is set and display the appropriate message
        if (keyManager.getOriginalKey() != null) {
            ConsoleUI.displaySuccessMessage("The current key selected is: " + Arrays.toString(keyManager.getOriginalKey()));
        } else {
            System.out.println("The key is not set");
        }

        returnToOptionsMenu();
    }

    private void viewInputDirectoryFiles() {
        if (inputDirectoryManager.getInputDirectory() != null) {
            inputDirectoryManager.printFilesInDirectory(inputDirectoryManager.getInputDirectory());
        } else {
            ConsoleUI.displayErrorMessage("No input directory set.");
        }
        returnToOptionsMenu();
    }

    private void viewOutputDirectoryFiles() {
        if (outputDirectoryManager.getOutputDirectory() != null) {
            outputDirectoryManager.printFilesInDirectory(outputDirectoryManager.getOutputDirectory());
        } else {
            ConsoleUI.displayErrorMessage("No output directory set");
        }
        returnToOptionsMenu();
    }

    /**
     * Clears the current settings if the user confirms the action.
     * Asks the user for confirmation, sets the input directory, output directory, and the key to null.
     * Then displays the updated settings to the user.
     */
    private void clearSettings() {
        // Ask the user for confirmation before clearing settings
        if (ConsoleUI.performActionIfUserConfirms("Would you like to clear all setting? (y/n)")) {
            // Clear the input directory
            inputDirectoryManager.setInputDirectory(null);
            // Clear the output directory
            outputDirectoryManager.setOutputDirectory(null);
            // Clear the key
            keyManager.setOriginalKey(null);
            // Show the updated settings to the user
            viewSettings();
        } else {
            // Return to the options menu without making changes
            returnToOptionsMenu();
        }
    }

    /**
     * Returns the user to the options menu after they enter 'y'.
     * Keeps prompting the user until they enter 'y'
     */
    private void returnToOptionsMenu() {
        System.out.println("Press 'y' to return to the options menu");
        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("y")) {
                break; // Exit the loop if the correct input is received
            } else {
                System.out.println("Invalid input. Please select 'y' to return to the options menu");
            }
        }
        displayOptionsMenu(); // Display the options menu
    }

    /**
     * Returns the user to the main menu after they enter 'y'.
     * Keeps prompting the user until they enter 'y'.
     */
    private void returnToMenu() {
        System.out.println("Select 'y' to return to the menu");

        while (true) {
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("y")) {
                break; // Exit the loop if the correct input is received
            } else {
                System.out.println("Invalid input. Please select 'y' to return to the menu");
            }
        }
        displayMainMenu(); // Display the main menu
    }
}
