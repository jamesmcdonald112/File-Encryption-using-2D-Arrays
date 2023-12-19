package ie.atu.sw;

import java.io.File;

/**
 * This {@code InputDirectoryManager} is responsible for managing the selection and handling of input directories.
 * It provides methods to check, set, and reset the input directory, print the contents of the directories,
 * validate input paths, and handle various scenarios related to input directory selection.
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #getInputDirectory()}</li>
 *     <li>{@link #setInputDirectory(File)}</li>
 *     <li>{@link #handleInputDirectoryManager()}</li>
 *     <li>{@link #isValidInputDirectory(File)}</li>
 *     <li>{@link #printFilesInDirectory(File)}</li>
 *     <li>{@link #printInputDirectory()}</li>
 * </ul>
 *
 * <p><b>Private Methods:</b></p>
 * <ul>
 *     <li>{@link #inputDirectoryIsSelected()}</li>
 *     <li>{@link #resetInputDirectory()}</li>
 *     <li>{@link #handleInputDirectoryIsSelected()}</li>
 *     <li>{@link #handleInputDirectoryNotSelected()}</li>
 * </ul>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #inputDirectory}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see ConsoleUI
 */
public class InputDirectoryManager {

    /**
     * Represents the input directory for handling files.
     */
    private File inputDirectory;

    /**
     * Retrieves the currently selected input directory.
     *
     * @return The file object representing the current input directory.
     */
    public File getInputDirectory() {
        return this.inputDirectory;
    }

    /**
     * Sets the input directory based on the provided File object.
     *
     * @param inputDirectory A File object representing the input directory to be set.
     */
    public void setInputDirectory(File inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    /**
     * Manages the selection and display of the input directory.
     * If an input directory has been selected, provides the option to change or view it.
     * If no input directory has been selected, prompts the user to select one.
     */
    public void handleInputDirectoryManager() {
        if (inputDirectoryIsSelected()) {
            handleInputDirectoryIsSelected();
        }
        if (!inputDirectoryIsSelected()) {
            handleInputDirectoryNotSelected();
        }

    }

    /**
     * Checks if the input path represents a valid file or directory.
     *
     * @param potentialInputDirectory The file object representing the potential input directory.
     * @return true if the input path is a file or a directory, false otherwise.
     */
    public boolean isValidInputDirectory(File potentialInputDirectory) {
        return potentialInputDirectory.isFile() || potentialInputDirectory.isDirectory();
    }

    /**
     * Handles the case when an input directory has been selected.
     * Displays the currently selected directory and asks the user if they want to change it.
     */
    private void handleInputDirectoryIsSelected() {
        // Print the currently selected output directory
        printInputDirectory();
        // Ask the user to confirm if they want to change te directory
        if (ConsoleUI.performActionIfUserConfirms(ConsoleUI.CHANGE_DIRECTORY_PROMPT)) {
            resetInputDirectory();
            printInputDirectory();
        }
    }

    /**
     * Handles the scenario when an input directory is not selected.
     * Continuously prompts the user to enter a valid directory.
     * If a valid directory is selected, it sets the input directory and prints its path.
     */
    private void handleInputDirectoryNotSelected() {
        // Loop until a valid input directory is selected
        while (!inputDirectoryIsSelected()) {
            // Prompt the user to enter the path for the input directory
            String userDirectory = ConsoleUI.getDirectoryName();
            // Return if user selects 'q'
            if (ConsoleUI.shouldUserQuit(userDirectory)) {
                return;
            }
            File potentialInputDirectory = new File(userDirectory);
            // If the directory is valid, set it as the input directory and print its path
            if (isValidInputDirectory(potentialInputDirectory)) {
                setInputDirectory(potentialInputDirectory);
                printInputDirectory();
            } else {
                ConsoleUI.displayErrorMessage("No valid input selected. Please try again");
            }
        }
    }

    /**
     * Checks if the input directory has been selected.
     *
     * @return true if the input directory is not null, false otherwise.
     */
    private boolean inputDirectoryIsSelected() {
        return inputDirectory != null;
    }

    /**
     * Restores the input directory to null.
     */
    private void resetInputDirectory() {
        setInputDirectory(null);
    }

    /**
     * Prints the list of file in the specified directory.
     * Handles potential security exceptions and checks if they directory is empty.
     *
     * @param directory The directory whose files are to be listed
     */
    public void printFilesInDirectory(File directory) {
        try {
            File[] listOfFiles = directory.listFiles();

            if (listOfFiles == null) {
                throw new SecurityException("An error occurred while trying to list files in this directory");
            } else if (listOfFiles.length == 0) {
                ConsoleUI.displayErrorMessage("No files in this directory");
            } else {
                for (File file : listOfFiles) {
                    System.out.println(file);
                }
            }
        } catch (SecurityException securityException) {
            ConsoleUI.displayErrorMessage("A security error occurred while trying to access the " +
                    "directory: " + securityException.getMessage());
        }
    }

    /**
     * Prints information about the selected input directory to the console.
     * If no directory has been selected, an error message is displayed
     */
    public void printInputDirectory() {
        if (inputDirectoryIsSelected()) {
            ConsoleUI.displaySuccessMessage("The current input directory selected is " + inputDirectory.getAbsolutePath());
        } else {
            ConsoleUI.displayErrorMessage("No input directory selected.");
        }
    }

}
