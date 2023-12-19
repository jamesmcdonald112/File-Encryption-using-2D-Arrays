package ie.atu.sw;

import java.io.File;

/**
 * The {@code OutputDirectoryManager} class is responsible for handling the selection,
 * creation, and validation of an output directory. It provides methods to set the output
 * directory, verify its validity, create a new directory if required, print the contents of the directory,
 * and manage the overall workflow related to the output directories.
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #setOutputDirectory(File)}</li>
 *     <li>{@link #getOutputDirectory()}</li>
 *     <li>{@link #handleOutputDirectoryManager()}</li>
 *     <li>{@link #printFilesInDirectory(File)}</li>
 * </ul>
 *
 * <p><b>Private Methods:</b></p>
 * <ul>
 *     <li>{@link #handleOutputDirectoryIsSelected()}</li>
 *     <li>{@link #handleOutputDirectoryNotSelected()}</li>
 *     <li>{@link #outputDirectoryIsSelected()}</li>
 *     <li>{@link #createDirectory(File)}</li>
 *     <li>{@link #isValidDirectory(File)}</li>
 *     <li>{@link #resetOutputDirectory()}</li>
 *     <li>{@link #printOutputDirectory()}</li>
 * </ul>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #outputDirectory}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see ConsoleUI
 */
public class OutputDirectoryManager {

    /**
     * The selected output directory. Null if not set.
     */
    private File outputDirectory;

    /**
     * Sets the output directory to the provided file.
     *
     * @param outputDirectory The output directory is set
     */
    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Gets the currently selected output directory. Prints a message and returns null if not set.
     *
     * @return the File representing the selected output directory, or null if not set
     */
    public File getOutputDirectory() {
        return this.outputDirectory;
    }

    /**
     * Manages the handling of output directory selection based on its current state.
     * If the output directory is already selected, a method is called to handle that scenario.
     * If not, calls the method to handle when no output directory is selected.
     */
    public void handleOutputDirectoryManager() {
        if (outputDirectoryIsSelected()) {
            handleOutputDirectoryIsSelected();
        }
        /*
        If the user wants to change output directory (an option in the previous method), output directory will
        be set to null and handleOutputDirectoryNotSelected() will run
         */
        if (!outputDirectoryIsSelected()) {
            handleOutputDirectoryNotSelected();
        }

        printOutputDirectory();
    }

    /**
     * Handles the scenario where the output directory is already selected.
     * If the user confirms to the change the directory, the output directory is reset and the updated state is printed.
     * The ConsoleUi.performActionIfUserConfirms method is expected to handle user input
     * and return true if the user confirms, false otherwise.
     */
    private void handleOutputDirectoryIsSelected() {
        // Print the currently selected output directory
        printOutputDirectory();
        // Ask the user to confirm if they want to change the directory
        if (ConsoleUI.performActionIfUserConfirms(ConsoleUI.CHANGE_DIRECTORY_PROMPT)) {
            resetOutputDirectory();
            printOutputDirectory();
        }
    }

    /**
     * Handles the scenario when an output directory is not selected.
     * Allows the user to either create a new directory or select an existing one.
     * If the user chooses to return, the method exits without making any changes.
     */
    private void handleOutputDirectoryNotSelected() {
        // Loop until a valid output directory is selected or the user chooses to return
        while (!outputDirectoryIsSelected()) {
            // Prompt the user to either create, select, or return
            String userChoice = ConsoleUI.createOrSelectDirectory(ConsoleUI.CREATE_OR_SELECT_DIRECTORY_PROMPT);
            switch (userChoice) {
                // Create a new directory
                case "c" -> {
                    String userDirectory = ConsoleUI.getDirectoryName();
                    // Return if user selects 'q'
                    if (ConsoleUI.shouldUserQuit(userDirectory)) {
                        return;
                    }
                    File potentialDirectory = new File(userDirectory);
                    // If the directory is created successfully, set it as the output directory
                    if (createDirectory(potentialDirectory)) {
                        setOutputDirectory(potentialDirectory);
                    }
                }
                // Select an existing directory
                case "s" -> {
                    String userDirectory = ConsoleUI.getDirectoryName();
                    // Return if user selects 'q'
                    if (ConsoleUI.shouldUserQuit(userDirectory)) {
                        return;
                    }
                    File potentialDirectory = new File(userDirectory);
                    // If the directory is valid, set it as the output directory
                    if (isValidDirectory(potentialDirectory)) {
                        setOutputDirectory(potentialDirectory);
                    }
                }
                // Return without making any changes
                case "r" -> {
                    return;
                }
            }
        }
    }

    /**
     * Checks whether the output directory has been selected.
     *
     * @return true if the output directory has been selected, false otherwise.
     */
    private boolean outputDirectoryIsSelected() {
        return outputDirectory != null;
    }

    /**
     * Attempts to create a new directory at the specified location.
     *
     * @param potentialOutputDirectory The file object representing the path where the directory should be created.
     * @return true if the directory already exists or was successfully created; false if the directory could
     * not be created or the input is null.
     */
    private boolean createDirectory(File potentialOutputDirectory) {
        // Check if the input is null and print a message if it is
        if (potentialOutputDirectory == null) {
            ConsoleUI.displayErrorMessage("Potential output directory cannot be null.");
            return false;
        }
        // Check if the directory already exists
        if (potentialOutputDirectory.isDirectory() && potentialOutputDirectory.exists()) {
            ConsoleUI.displaySuccessMessage(potentialOutputDirectory + " already exists.");
            return true;
            // Attempt to create the directory at the specified location
        } else if (potentialOutputDirectory.mkdirs()) {
            ConsoleUI.displaySuccessMessage(potentialOutputDirectory + " successfully created!");
            return true;
        } else {
            // If the directory creation fails, print a message and return false
            ConsoleUI.displayErrorMessage(potentialOutputDirectory + " cannot be created");
            return false;
        }
    }

    /**
     * Verifies if the specified directory is valid, meaning that it:
     * - Exists
     * - Is a directory
     * - Is writable
     *
     * @param directory The directory to be validated
     * @return true if the directory is valid; false otherwise
     */
    private boolean isValidDirectory(File directory) {
        // Check if the directory parameter is null
        if (directory == null) {
            ConsoleUI.displayErrorMessage("Directory cannot be null.");
            return false;
        }

        // Check if the directory exists
        if (!directory.exists()) {
            ConsoleUI.displayErrorMessage(directory + " cannot be found");
            return false;
        }

        // Check if the directory is a directory
        if (!directory.isDirectory()) {
            ConsoleUI.displayErrorMessage(directory + " exists but is not a directory");
            return false;
        }

        // Check if the directory can write
        if (!directory.canWrite()) {
            ConsoleUI.displayErrorMessage("Cannot write to " + directory + " directory");
            return false;
        }
        return true;
    }

    /**
     * Resets the output directory to null
     */
    private void resetOutputDirectory() {
        setOutputDirectory(null);
    }

    /**
     * Prints the current output directory if one is selected; otherwise, notifies the user if no directory is selected
     */
    private void printOutputDirectory() {
        if (outputDirectoryIsSelected()) {
            ConsoleUI.displaySuccessMessage("The current output directory selected is " + outputDirectory.getAbsolutePath());
        } else {
            ConsoleUI.displayMessage("No output directory selected.");
        }
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


}
