package ie.atu.sw;

import java.io.IOException;

/**
 * This EncryptionManager is responsible for managing the encryption process using the ADFGVX cipher.
 * It provides methods to encode cleaned text, create and reorder matrix using the encryption key,
 * perform columnar transpositions, and manage the overall encryption workflow.
 *
 * <p><b>Constructor:</b></p>
 * <ul>
 *     <li>{@link #EncryptionManager(CipherManager)}</li>
 * </ul>
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #handleEncryptionManagement()}</li>
 * </ul>
 *
 * <p><b>Private Methods:</b></p>
 * <ul>
 *     <li>{@link #encrypt(String, char[])}</li>
 *     <li>{@link #encodeCleanedText(String)}</li>
 *     <li>{@link #encodedCharacter(char)}</li>
 *     <li>{@link #createMatrixUsingKey(String, char[])}</li>
 *     <li>{@link #reorderMatrixColumns(char[][], int[])}</li>
 *     <li>{@link #columnarTransposition(char[][])}</li>
 * </ul>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #cipherManager}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see CipherManager
 * @see ConsoleUI
 */
public class EncryptionManager {

    /**
     * Manager for handling cipher operations
     */
    private final CipherManager cipherManager;

    /**
     * Constructor to initialise the EncryptionManager with a CipherManager.
     *
     * @param cipherManager The manager handling cipher operations
     */
    public EncryptionManager(CipherManager cipherManager) {
        this.cipherManager = cipherManager;

    }

    /**
     * Stores the cleaned text used for encryption
     */

    public void handleEncryptionManagement() {
        try {
            // Check if keys and directories are set, return if not
            if (!cipherManager.areKeysAndDirectoriesSet()) {
                return;
            }

            // Ask the user if they want to change settings and return if they choose to do so
            if (ConsoleUI.askUserToChangeSettings()) {
                return;
            }

            // Display message that encryption has started
            ConsoleUI.displayEncryptionStarted();

            // Parse the directory and get the cleaned text
            String[] cleanedTexts = cipherManager.getParser().parseDirectory(
                    cipherManager.getInputDirectoryManager().getInputDirectory().toString());

            for (String cleanedText : cleanedTexts) {
                String encryptedText = encrypt(cleanedText, cipherManager.getKeyManager().getOriginalKey());

                // Get output directory and write the encrypted text to file
                String outputDirectory = cipherManager.getOutputDirectoryManager().getOutputDirectory().toString();
                cipherManager.getMyFileWriter().writeToFile(outputDirectory, encryptedText, true);
            }

            // Prints progress meter
            ConsoleUI.progressMeter();

        } catch (IllegalArgumentException illegalArgumentException) {
            // Handle error during encryption
            ConsoleUI.displayErrorMessage("Error during encryption: " + illegalArgumentException.getMessage());
        } catch (IOException e) {
            // Handle error during file writing
            ConsoleUI.displayErrorMessage("Error during file writing: " + e.getMessage());
        }
    }

    /**
     * Performs the encryption process on the given cleaned text with the specified original key.
     * <p>
     * If follows these steps:
     * <ol>
     *     <li>Encode the cleaned text</li>
     *     <li>Create a matrix using the original key</li>
     *     <li>Sort the key lexicographically</li>
     *     <li>Return the indices of the sorted key</li>
     *     <li>Reorder the matrix columns based on the sorted key indices</li>
     *     <li>Perform the columnar transposition on the reordered matrix</li>
     * </ol>
     *
     * @param cleanedText The cleaned text to be encrypted
     * @param originalKey The original key used for encryption
     * @return The encryption text as a string
     */
    private String encrypt(String cleanedText, char[] originalKey) {

        // Encode the cleaned text
        String encodedText = encodeCleanedText(cleanedText);

        // Create a matrix using the original key
        char[][] encodedMatrix = createMatrixUsingKey(encodedText, originalKey);

        // Reorder the matrix columns based on the sorted key indices
        char[][] reorderedMatrix = reorderMatrixColumns(encodedMatrix, cipherManager.getKeyManager().getSortedKeyIndices());

        // Perform the columnar transposition on the reordered matrix and return the result
        return columnarTransposition(reorderedMatrix);

    }

    /**
     * Encodes a cleaned text using the ADFGVX cipher
     *
     * @param cleanedText The text that has been prepared for encoding (all non-alphanumeric characters and spaces
     *                    removed. All characters to uppercase)
     * @return The encoded text as a string, where each character is replaced by two encoded characters
     * from the ADFGVX array
     */
    private String encodeCleanedText(String cleanedText) {
        StringBuilder encodedText = new StringBuilder();
        // Iterate through each character in the cleanedText
        for (char c : cleanedText.toCharArray()) {
            // Encode each character using the ADFGVX array and append it to encodedText
            encodedText.append(encodedCharacter(c));
        }

        return encodedText.toString();
    }

    /**
     * Encodes a single character using the ADFGVX cipher.
     *
     * @param character The character to be encoded
     * @return The encoded character as a string, represented by two characters from the ADFGVX array.
     * @throws IllegalArgumentException If the character is not found in the Polybius Square.
     */
    private String encodedCharacter(char character) {
        // Iterate through the rows of the ADFGVX array
        for (int row = 0; row < CipherManager.ADFGVX.length; row++) {
            // Iterate through the columns of the ADFGVX array
            for (int col = 0; col < CipherManager.ADFGVX.length; col++) {
                // If the character matches the row and column of the Polybius square
                if (CipherManager.POLYBIUS_SQUARE[row][col] == character) {
                    // Return the encoded character, first reading the row and then the column of the ADFGVX array
                    return Character.toString(CipherManager.ADFGVX[row]) + CipherManager.ADFGVX[col];
                }
            }
        }
        // Throw an exception if the character is not found in the polybius square
        throw new IllegalArgumentException("Character not in Polybius square: " + character);
    }

    /**
     * Creates a matrix using the given key, placing the key in the first row
     * and filling the remaining rows with characters from the given text.
     *
     * @param text The text to be placed in the matrix.
     * @param key  The key to be placed in the first row of the matrix.
     * @return The matrix with the key in the first row and the text in the remaining rows.
     */
    private char[][] createMatrixUsingKey(String text, char[] key) {
        // Create matrix of the correct size based on the text and key
        char[][] matrix = cipherManager.createSizedMatrixFromTextAndKey(text, key);

        // Fill in the first row of the matrix with the key
        for (int i = 0; i < key.length; i++) {
            matrix[0][i] = key[i];
        }

        // Track the text index
        int textIndex = 0;

        // Fill in the remaining rows of the matrix with the characters from the text
        for (int row = 1; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                if (textIndex < text.length()) {
                    matrix[row][col] = text.charAt(textIndex);
                    textIndex++;
                }
            }
        }
        return matrix;
    }

    /**
     * Reorders the columns of the given matrix according to the indices provided by the keyIndices.
     *
     * @param matrix     The 2D character matrix that needs to be reordered
     * @param keyIndices The array of indices that defines the new order of the columns
     * @return The reordered 2D character matrix.
     */
    private char[][] reorderMatrixColumns(char[][] matrix, int[] keyIndices) {
        // Clone the original matrix to create a new matrix with the same structure.
        char[][] reorderedMatrix = new char[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            reorderedMatrix[i] = matrix[i].clone();
        }

        // Loop through each row of the matrix.
        for (int row = 0; row < reorderedMatrix.length; row++) {
            // Loop through each column of the matrix
            for (int column = 0; column < reorderedMatrix[0].length; column++) {
                // For each cell in the reordered matrix, look up the corresponding cell
                // from the original matrix using the keyIndices
                reorderedMatrix[row][column] = matrix[row][keyIndices[column]];
            }
        }
        return reorderedMatrix;
    }

    /**
     * Performs a columnar transposition on the given matrix.
     * <p>
     * This method reads the given character matrix by columns and constructs a string. It loops through each column
     * and then iterates over the rows within that column, appending each character to a StringBuilder. The resulting
     * string is returned, excluding the first row which contains the key
     *
     * @param matrix The 2D character matrix to be transposed.
     * @return The transposed string constructed from the columnar read of the matrix, excluding the first row which
     * contains the key.
     */
    private String columnarTransposition(char[][] matrix) {
        StringBuilder transposedMatrix = new StringBuilder();
        // Loop through the columns of the matrix
        for (int col = 0; col < matrix[0].length; col++) {
            // Start from the second row (skip the key) and loop through the rows of the current column
            for (int row = 1; row < matrix.length; row++) {
                // Append each character to the transposed matrix
                transposedMatrix.append(matrix[row][col]);
            }
        }
        return transposedMatrix.toString();
    }
}
