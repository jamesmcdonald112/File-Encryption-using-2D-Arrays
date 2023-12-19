package ie.atu.sw;

import java.io.IOException;

/**
 * The {@code DecryptionManager} class is responsible for handling the decryption
 * process for the ADFGVX cipher. It provides methods to decode encrypted text, create and reorder matrix using the
 * decryption key, perform columnar transposition, and manage the overall decryption workflow.
 *
 * <p><b>Constructor:</b></p>
 * <ul>
 *     <li>{@link #DecryptionManager(CipherManager)}</li>
 * </ul>
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #getCipherText()}</li>
 *     <li>{@link #setCipherText(String)}</li>
 *     <li>{@link #handleDecryptionManagement()}</li>
 * </ul>
 *
 * <p><b>Private Methods</b></p>
 * <ul>
 *     <li>{@link #decrypt(String, char[])}</li>
 *     <li>{@link #reverseTranspositionToMatrix(String, char[])}</li>
 *     <li>{@link #reorderMatrixColumns(char[][], int[])}</li>
 *     <li>{@link #readMatrixByRows(char[][])}</li>
 *     <li>{@link #decryptCipherText(String)}</li>
 *     <li>{@link #decryptedCharacter(char, char)}</li>
 * </ul>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #cipherManager}</li>
 *     <li>{@link #cipherText}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see CipherManager
 * @see ConsoleUI
 */
public class DecryptionManager {

    /**
     * Manager for handling cipher operations
     */
    private final CipherManager cipherManager;

    /**
     * Holds the cipher text to be decrypted
     */
    private String cipherText;

    /**
     * Constructor to initialise the DecryptionManager with a CipherManager.
     *
     * @param cipherManager The manager handling cipher operations
     */
    public DecryptionManager(CipherManager cipherManager) {
        this.cipherManager = cipherManager;
    }

    /**
     * Getter for the cipherText field.
     *
     * @return The cipher text to be decrypted as a String
     */
    public String getCipherText() {
        return cipherText;
    }

    /**
     * Setter for the cipherText field.
     *
     * @param cipherText The cipher text to be decrypted.
     */
    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    /**
     * Manages the process of decrypting ADFGVX cipher text from an input directory.
     * <p>
     * The method performs several steps:
     * <ol>
     *     <li>Validates that the keys and directories are set.</li>
     *     <li>Prompts the user to confirm or change settings</li>
     *     <li>Validates the cipher text in the input directory</li>
     *     <li>Decrypts the cipher text and prints the result if the user requests to view it.</li>
     *     <li>Writes the decrypted text to the output directory in individual files.</li>
     * </ol>
     * If any of the validations fail, or if an exception occurs during decryption or file writing,
     * an appropriate error message is printed and the method exits early.
     *
     * @throws RuntimeException if an IOException occurs while writing the decrypted text to a file.
     */
    public void handleDecryptionManagement() {
        try {
            // Check if keys and directories are set
            if (!cipherManager.areKeysAndDirectoriesSet()) {
                return; // Keys and directories are not set, exiting
            }

            // Ask the user if they want to change settings and return if they choose to do so
            if (ConsoleUI.askUserToChangeSettings()) {
                return; // User chose to change settings, exiting
            }

            // Get the input directory path
            String inputDirectoryPath = cipherManager.getInputDirectoryManager().getInputDirectory().toString();

            // Validate the cipher text in the directory
            if (!cipherManager.validateADFGVXCipherInDirectory(inputDirectoryPath)) {
                ConsoleUI.displayErrorMessage("Failed: The input directory contains invalid ADFGVX cipher text");
                return; // Validation failed, exiting
            }

            // Display message that decryption has started
            ConsoleUI.displayDecryptionStarted();

            String[] cipherTexts = cipherManager.getParser().parseADFGVXDirectory(inputDirectoryPath);

            for (String cipherText : cipherTexts) {
                // Decrypt the cipher text using the original key
                String decryptedText = decrypt(cipherText, cipherManager.getKeyManager().getOriginalKey());
                // Get output directory path and write the decrypted text to a file
                String outputDirectoryPath = cipherManager.getOutputDirectoryManager().getOutputDirectory().toString();
                cipherManager.getMyFileWriter().writeToFile(outputDirectoryPath, decryptedText, false);
            }
            // Prints progress meter
            ConsoleUI.progressMeter();

        } catch (IllegalArgumentException illegalArgumentException) {
            // Handle illegal argument during decryption
            ConsoleUI.displayErrorMessage("Error during decryption: " + illegalArgumentException.getMessage());
        } catch (IOException e) {
            // Throw IOException as a runtime exception
            throw new RuntimeException(e); // Propagate IOException as a runtime exception
        }
    }

    /**
     * Decrypts a given cipher text using the original key
     * <p>
     * The method performs a series of operations to decrypt the cipher text. These steps include:
     * 1. Lexicographically ordering the key
     * 2. Reversing the columnar transposition using the ordered key to recreate the original matrix
     * 3. Reordering the matrix back to the key's original order
     * 4. Reading the rows of the matrix to form an encoded string
     * 5. Using the ADFGVX array and Polybius square to decipher the encoded string
     *
     * @param cipherText The encrypted text to be decrypted.
     * @return The deciphered text
     */
    private String decrypt(String cipherText, char[] originalKey) {
        // Remake the reordered matrix by reversing the columnar transposition using the lexicographically ordered key
        char[][] remadeMatrix = reverseTranspositionToMatrix(cipherText, originalKey);

        int[] originalKeyIndices = cipherManager.getKeyManager().getOriginalKeyIndices();
        char[][] reorderedMatrix = reorderMatrixColumns(remadeMatrix, originalKeyIndices);

        // Read rows of the matrix to from an encoded string
        String encodedText = readMatrixByRows(reorderedMatrix);

        // Use the ADFGVX array and polybius square to decipher the encoded text

        return decryptCipherText(encodedText);

    }

    /**
     * Creates a matrix by reversing the columnar transposition using a lexicographically sorted sortedKey.
     * The method reconstructs the 2D matrix used in the ADFGVX cipher before columnar transposition.
     *
     * @param cipherText The encrypted text to be organised into the matrix
     * @param sortedKey  The sorted key used in the encryption, sorted lexicographically.
     * @return A 2D char array representing the pairs of characters after reversing the transposition.
     */
    private char[][] reverseTranspositionToMatrix(String cipherText, char[] sortedKey) {
        // Creates a matrix of the correct size of the sortedKey and text
        char[][] matrix = cipherManager.createSizedMatrixFromTextAndKey(cipherText, sortedKey);
        // Fill in the first row with the sortedKey
        for (int i = 0; i < sortedKey.length; i++) {
            matrix[0][i] = sortedKey[i];
        }

        // Initialise int variable to track the positions in the cipherText
        int cipherTextIndex = 0;
        // Iterate through the matrix columns, skipping the first row,which contains the key.
        for (int col = 0; col < matrix[0].length; col++) {
            // int row = 1 skips the first row.
            for (int row = 1; row < matrix.length; row++) {
                if (cipherTextIndex < cipherText.length()) {
                    // Place characters from the ciphertext into the corresponding positions in the matrix
                    matrix[row][col] = cipherText.charAt(cipherTextIndex);
                    cipherTextIndex++;
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
     * Reads the given matrix row-by-row and constructs a string from the characters,
     * excluding the first row, which is for the key.
     * <p>
     * This method is used to convert the characters from the matrix back into a string.
     * This first row is skipped as it is assumed to contain the key, and the remaining
     * characters are concatenated into a string in by reading each row.
     *
     * @param matrix The char matrix to be read row-by-row
     * @return A string constructed from the characters in the matrix, excluding the first row.
     */
    private String readMatrixByRows(char[][] matrix) {
        // StringBuilder to concatenate characters to a string
        StringBuilder rowsToString = new StringBuilder();
        // Skip the first row, as it contains the key
        for (int row = 1; row < matrix.length; row++) {
            // Iterate through the columns in the current row
            for (int col = 0; col < matrix[0].length; col++) {
                // Append each character to the StringBuilder
                rowsToString.append(matrix[row][col]);
            }
        }
        return rowsToString.toString();
    }

    /**
     * Decrypts the given cipher text using the ADFGVX cipher
     *
     * @param cipherText The cipher text to be decrypted
     * @return The decrypted text as a string.
     */
    private String decryptCipherText(String cipherText) {
        StringBuilder decryptedText = new StringBuilder();
        // Iterate through each pair of characters of the cipher text
        for (int i = 0; i < cipherText.length(); i += 2) {
            // Row character from ADFGVX array
            char rowCharacter = cipherText.charAt(i);

            // Avoid index out of bounds exception
            if (i + 1 < cipherText.length()) {
                // Column character from ADFGVX array
                char columnCharacter = cipherText.charAt(i + 1);

                // Decrypt the row and column characters using the decryptedCharacter method and assign to decryptedCharacter
                char decryptedCharacter = decryptedCharacter(rowCharacter, columnCharacter);

                // Append the decrypted character to the decryptedText
                decryptedText.append(decryptedCharacter);
            }
        }
        // Return the decrypted text as a string
        return decryptedText.toString();
    }

    /**
     * Decrypts a character pair in the ADFGVX cipher by finding the corresponding row and column
     * in the ADFGVX array and then looking up the original character in the Polybius square.
     *
     * @param rowCharacter    The character representing the row in the ADFGVX array.
     * @param columnCharacter The character representing the column in the ADFGVX array.
     * @return The decrypted character found ar the corresponding location in the Polybius square
     * @throws IllegalArgumentException If the characters are not found in the ADFGVX array.
     */
    private char decryptedCharacter(char rowCharacter, char columnCharacter) {
        int rowIndex = -1;
        int columnIndex = -1;

        // Iterate though the ADFGVX array
        for (int i = 0; i < CipherManager.ADFGVX.length; i++) {
            // If the rowCharacter matches one of the ADFGVX array characters
            if (CipherManager.ADFGVX[i] == rowCharacter) {
                rowIndex = i; // Set the index to the row
                break; // Exit the loop once found
            }
        }
        // Iterate though the ADFGVX array
        for (int i = 0; i < CipherManager.ADFGVX.length; i++) {
            // If the columnCharacter matches one of the ADFGVX array characters
            if (CipherManager.ADFGVX[i] == columnCharacter) {
                columnIndex = i; // Set the index to the column
                break; // Exit the loop once found
            }
        }
        // If the row or index are -1 (can't find a match)
        if (rowIndex == -1 || columnIndex == -1) {
            throw new IllegalArgumentException("Character not in the ADFGVX array");
        }
        // Return the character found at the location of the rowIndex and colIndex
        return CipherManager.POLYBIUS_SQUARE[rowIndex][columnIndex];

    }
}
