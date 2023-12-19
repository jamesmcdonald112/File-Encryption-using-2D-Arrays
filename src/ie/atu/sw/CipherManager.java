package ie.atu.sw;

import java.io.File;

/**
 * The {@code CipherManager} class is responsible for managing different components related to ADFGVX cipher operations.
 * It handles various tasks such as validating cipher text, creating appropriately sized matrices for text and keys,
 * and coordinating input and output directories for ciphering processes. This class acts as a central hub
 * to coordinate different components required for ciphering, and it interfaces with other relevant classes
 * like parsers, directory managers, etc.
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #POLYBIUS_SQUARE}</li>
 *     <li>{@link #ADFGVX}</li>
 *     <li>{@link #inputDirectoryManager}</li>
 *     <li>{@link #outputDirectoryManager}</li>
 *     <li>{@link #keyManager}</li>
 *     <li>{@link #parser}</li>
 *     <li>{@link #myFileWriter}</li>
 * </ul>
 *
 * <p><b>Constructor:</b></p>
 * <ul>
 *     <li>{@link #CipherManager(InputDirectoryManager, OutputDirectoryManager, KeyManager)}</li>
 * </ul>
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *      <li>{@link #getInputDirectoryManager()}</li>
 *      <li>{@link #getOutputDirectoryManager()}</li>
 *      <li>{@link #getKeyManager()}</li>
 *      <li>{@link #getParser()}</li>
 *      <li>{@link #getMyFileWriter()}</li>
 *      <li>{@link #areKeysAndDirectoriesSet()}</li>
 *      <li>{@link #validateADFGVXCipherInDirectory(String)}</li>
 *      <li>{@link #fileIsValidADFGVXCipherText(String)}</li>
 *      <li>{@link #characterIsValidADFGVX(char)}</li>
 *      <li>{@link #createSizedMatrixFromTextAndKey(String, char[])}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see InputDirectoryManager
 * @see OutputDirectoryManager
 * @see KeyManager
 * @see Parser
 * @see MyFileWriter
 */
public class CipherManager {

    /**
     * POLYBIUS_SQUARE - A 6 x 6 grid represents the Polybius used in the ADFGVX cipher.
     * This matrix includes all uppercase letters and the numbers 0-9. Each element
     * corresponds to a unique pair of characters from the ADFGVX array.
     */
    public static final char[][] POLYBIUS_SQUARE = {
            {'P', 'H', '0', 'Q', 'G', '6'},
            {'4', 'M', 'E', 'A', '1', 'Y'},
            {'L', '2', 'N', 'O', 'F', 'D'},
            {'X', 'K', 'R', '3', 'C', 'V'},
            {'S', '5', 'Z', 'W', '7', 'B'},
            {'J', '9', 'U', 'T', 'I', '8'}
    };

    /**
     * ADFGVX - This character array serves the purpose of encoding elements in the ADFGVX cipher.
     * Each character in the ADFGVX array serves as a row and column identifier for the Polybius square, effectively
     * encoding elements in the square.
     */
    public static final char[] ADFGVX = {'A', 'D', 'F', 'G', 'V', 'X'};

    /**
     * Manager for handling the input directory paths
     */
    private final InputDirectoryManager inputDirectoryManager;

    /**
     * Manager for handling the output directory paths
     */
    private final OutputDirectoryManager outputDirectoryManager;

    /**
     * Manager for handling the encryption keys
     */
    private final KeyManager keyManager;

    /**
     * Parser for handling text parsing needs.
     */
    private final Parser parser;

    /**
     * Writer for handling file writing needs.
     */
    private final MyFileWriter myFileWriter;

    /**
     * Constructs a CipherManager with given input manager, output manager, and keyManager manager.
     *
     * @param inputDirectoryManager  Manager for handling the input directory.
     * @param outputDirectoryManager Manager for handling the output directory.
     * @param keyManager             Manager for handling the cipher key.
     */
    public CipherManager(InputDirectoryManager inputDirectoryManager, OutputDirectoryManager outputDirectoryManager, KeyManager keyManager) {
        this.parser = new Parser();
        this.myFileWriter = new MyFileWriter();
        this.inputDirectoryManager = inputDirectoryManager;
        this.outputDirectoryManager = outputDirectoryManager;
        this.keyManager = keyManager;

    }

    /**
     * Retrieves the InputDirectoryManager handling the input directory operations.
     *
     * @return The InputDirectoryManager associated with this CipherManager.
     */
    public InputDirectoryManager getInputDirectoryManager() {
        return inputDirectoryManager;
    }

    /**
     * Retrieves the OutputDirectoryManager handling the output directory operations.
     *
     * @return The OutputDirectoryManager associated with this CipherManager.
     */
    public OutputDirectoryManager getOutputDirectoryManager() {
        return outputDirectoryManager;
    }

    /**
     * Retrieves the KeyManager responsible for managing the cipher key.
     *
     * @return The KeyManager associated with this CipherManager.
     */
    public KeyManager getKeyManager() {
        return keyManager;
    }

    /**
     * Retrieves the Parser responsible for parsing files and other parsing tasks.
     *
     * @return The Parser associated with this CipherManager.
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Retrieves the MyFileWriter responsible for writing data to the files.
     *
     * @return The MyFileWriter associated with this CipherManager.
     */
    public MyFileWriter getMyFileWriter() {
        return myFileWriter;
    }

    /**
     * Verifies the state of the input directory, output directory, and key.
     * <p>
     * This method checks if the input directory, output directory, and key are set. If any of these are not set,
     * an appropriate message is printed, and the method returns false. If all are set, their respective handlers
     * are called, and the method returns true.
     *
     * @return true if all the settings (input directory, output directory, and key) are valid; false otherwise.
     */
    public boolean areKeysAndDirectoriesSet() {
        boolean allSettingsValid = true;

        if (inputDirectoryManager.getInputDirectory() == null) {
            ConsoleUI.displayErrorMessage("The input directory is not set. Please set it before proceeding");
            allSettingsValid = false;
        }

        if (outputDirectoryManager.getOutputDirectory() == null) {
            ConsoleUI.displayErrorMessage("The output directory is not set. Please set it before proceeding");
            allSettingsValid = false;
        }

        if (keyManager.getOriginalKey() == null) {
            ConsoleUI.displayErrorMessage("The key is not set. Please set it before proceeding");
            allSettingsValid = false;
        }
        if (allSettingsValid) {
            ConsoleUI.displayCurrentSetting(
                    inputDirectoryManager.getInputDirectory().getAbsolutePath(),
                    outputDirectoryManager.getOutputDirectory().getAbsolutePath(),
                    keyManager.getOriginalKey()
            );
            return true;
        }

        return false;
    }

    /**
     * Validates the ADFGVX cipher text in the specified directory or file.
     *
     * @param directoryPath The path of the directory of file to validate.
     * @return true if all files contain valid ADFGVX cipher text, false otherwise.
     */
    public boolean validateADFGVXCipherInDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        // Check if the path does not exist or is not a file/directory
        if (!directory.exists()) {
            ConsoleUI.displayErrorMessage("The path does not exist: " + directoryPath);
            return false; // Invalid path
        }

        // If it is a file, just validate it directly
        if (directory.isFile()) {
            return fileIsValidADFGVXCipherText(parser.readFile(directory));
        }

        // If it's a directory, iterate through the files
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!fileIsValidADFGVXCipherText(parser.readFile(file))) {
                        return false; // Invalid file found
                    }
                }
            }
            return true; // All files are valid, or directory is empty
        }
        ConsoleUI.displayErrorMessage("The path is not a file or a directory: " + directoryPath);
        return false; // Not a file or directory
    }

    /**
     * Checks if the given cipher text string is a valid ADFGVX cipher text.
     * A valid ADFGVX cipher text string consists only of characters found within
     * the ADFGVX array. Any other characters are considered invalid.
     *
     * @param cipherText The cipher text string to check.
     * @return true if the cipher text is valid, false otherwise.
     */
    public boolean fileIsValidADFGVXCipherText(String cipherText) {
        int invalidCharacterCount = 0;
        for (char c : cipherText.toCharArray()) {
            if (!characterIsValidADFGVX(c)) {
                invalidCharacterCount++;
            }
        }
        if (invalidCharacterCount > 0) {
            ConsoleUI.displayErrorMessage("Found " + invalidCharacterCount + " invalid characters in the cipher text.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the given character is a valid character in the ADFGVX cipher.
     *
     * @param c The character to check
     * @return true if the character is valid, false otherwise.
     */
    public boolean characterIsValidADFGVX(char c) {
        // Check if the character is in the ADFGVX array
        for (char validChar : ADFGVX) {
            if (c == validChar) {
                return true; // Valid character found
            }
        }
        return false; // Character not found in valid set
    }

    /**
     * Creates a matrix of the correct size based on text and key for use in the ADFGVX cipher.
     * The number of columns is determined by the length of the key, and the number of
     * rows is determined by the length of the text, divided by the number of columns, and rounded up.
     * The first row of the matrix is reserved for the key. If the text length is not evenly divisible by
     * the key length, the remainder is clipped to ensure the text fits evenly into the matrix.
     *
     * @param text The text to be placed in the matrix, starting from the second row.
     * @param key  The key to be placed in the first row of the matrix
     * @return A matrix of the correct size to accommodate the text and key, with the key in the first row.
     */
    public char[][] createSizedMatrixFromTextAndKey(String text, char[] key) {
        int columns = key.length;
        // The text needs to fit evenly into the matrix.
        // The amount to clip from the end of the text equals the remainder of text.length % key.length
        int clip = text.length() % key.length;
        // Remove the clipped characters from the text
        String clippedText = text.substring(0, text.length() - clip);
        // Determine the number of rows required and round up.
        int rows = (int) Math.ceil((double) clippedText.length() / columns);
        // Returns a matrix to the correct size. Rows + 1 leaves room for the key
        return new char[rows + 1][columns];
    }
}
