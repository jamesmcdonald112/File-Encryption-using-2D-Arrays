package ie.atu.sw;

/**
 * The {@code Runner} class serves as the entry point for the application. It's responsible for initiating
 * the interaction with the user by creating an instance of the Menu class and managing the application's flow.
 *
 * <p><b>Primary Responsibilities:</b></p>
 * <ul>
 *     <li>Creating an instance of the Menu class.</li>
 *     <li>Starting the main menu to interact with the user</li>
 *     <li>Closing the resources used for scanning input when finished</li>
 * </ul>
 *
 * <p><b>Note:</b></p> This class handles the top-level interactions with the user and integrates the various components
 * of the application. If any exceptions are thrown during the execution, they are caught, and an error message
 * is displayed.
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see Menu
 * @see ConsoleUI
 */
public class Runner {

    /**
     * The main method which serves as the entry point for the application.
     * <p>
     * It creates an instance of the Menu class, starts the menu to interact with the user,
     * and closes any resources used for scanning input when finished.
     */
    public static void main(String[] args) {

        try {
            // Create a menu object to manage user interactions
            Menu menu = new Menu();

            // Start the main menu to allow the user to make selections
            menu.startMenu();

            // Close resources used by the Menu object
            menu.closeScanner();

            // Close resources used by the Menu object
            ConsoleUI.closeScanner();

        } catch (Exception e) {
            ConsoleUI.displayErrorMessage("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
