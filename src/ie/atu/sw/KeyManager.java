package ie.atu.sw;

import java.util.Arrays;

/**
 * This
 * {@code KeyManager} class represents a cryptographic key management system. It includes
 * functionalities to get and set original and sorted keys, validate keys,
 * handle user interactions for key management, and perform internal key
 * processing, such as sorting keys lexicographically and determining original
 * and sorted key indices.
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *     <li>{@link #originalKey}</li>
 *     <li>{@link #sortedKey}</li>
 *     <li>{@link #originalKeyIndices}</li>
 *     <li>{@link #sortedKeyIndices}</li>
 * </ul>
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #setOriginalKey(String)}</li>
 *     <li>{@link #getOriginalKey()}</li>
 *     <li>{@link #setSortedKey(char[])}</li>
 *     <li>{@link #getSortedKey()}</li>
 *     <li>{@link #setSortedKeyIndices(int[])}</li>
 *     <li>{@link #getSortedKeyIndices()}</li>
 *     <li>{@link #setOriginalKeyIndices(int[])}</li>
 *     <li>{@link #getOriginalKeyIndices()}</li>
 *     <li>{@link #handleKeyManagement()}</li>
 * </ul>
 *
 * <p><b>Private Methods:</b></p>
 * <ul>
 *     <li>{@link #printOriginalKey()}</li>
 *     <li>{@link #handleExistingKey()}</li>
 *     <li>{@link #processKey()}</li>
 *     <li>{@link #isKeyValid(String)}</li>
 *     <li>{@link #isKeyAlphaNumeric(String)}</li>
 *     <li>{@link #hasDuplicateCharacters(String)}</li>
 *     <li>{@link #promptForKeyUntilValid()}</li>
 *     <li>{@link #sortKeyLexicographically(char[])}</li>
 *     <li>{@link #returnIndicesOfSortedKey(char[], char[])}</li>
 *     <li>{@link #returnIndicesOfOriginalKey(int[])}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 */
public class KeyManager {

    /**
     * An array of characters representing the original encryption key.
     * This key is used for both encryption and decryption processed.
     */
    private char[] originalKey;

    /**
     * An array of sorted characters representing the lexicographically ordered key.
     * This sorted version is used in certain encryption and decryption.
     */
    private char[] sortedKey;

    /**
     * An array of integers representing the original indices of the characters in the original key.
     * This may be used to map the characters back to their original order after sorting or other transformations.
     */
    private int[] originalKeyIndices;

    /**
     * An array of integers representing the indices of the sorted key.
     * This is used in algorithms that require a specific ordering of characters based on the original key.
     */
    private int[] sortedKeyIndices;

    /**
     * Sets the key for the cipher.
     * If the given key is null, the original key is set to null.
     * Otherwise, the original key is set to the character array of the input string.
     *
     * @param key The key as a string, may be null.
     */
    public void setOriginalKey(String key) {
        if (key == null) {
            this.originalKey = null;
        } else {
            this.originalKey = key.toCharArray();
        }

    }

    /**
     * Gets the cipher's key.
     *
     * @return The original key as a char array, or null if the key is not set.
     */
    public char[] getOriginalKey() {
        return this.originalKey;
    }

    /**
     * Sets the sorted key array.
     * This method is used to store a sorted version of the original key, used for encryption and decryption algorithms.
     *
     * @param sortedKey An array of characters representing the sorted key.
     */
    public void setSortedKey(char[] sortedKey) {
        this.sortedKey = sortedKey;
    }

    /**
     * Gets the sorted key array.
     * This method is used to retrieve the sorted version of the original key, all it to be accessed by other classes
     * or methods.
     *
     * @return An array of characters representing the sorted key.
     */
    public char[] getSortedKey() {
        return this.sortedKey;
    }

    /**
     * Sets the sorted key indices array.
     * This method is used to store the indices that represent the sorted order of the original key.
     *
     * @param sortedKeyIndices sortedKeyIndices An array of integers representing the sorted kwy indices.
     */
    public void setSortedKeyIndices(int[] sortedKeyIndices) {
        this.sortedKeyIndices = sortedKeyIndices;
    }

    /**
     * Gets the sorted key indices array.
     * This method is used to retrieve the indices representing the sorted order of the original key, so it can be
     * accessed by other classes and methods
     *
     * @return An array of integers representing the sorted key indices.
     */
    public int[] getSortedKeyIndices() {
        return this.sortedKeyIndices;
    }

    /**
     * Sets the original key indices.
     * This method allows the original key indices to be changed to a new value.
     *
     * @param originalKeyIndices The new original key indices as an integer array.
     */
    public void setOriginalKeyIndices(int[] originalKeyIndices) {
        this.originalKeyIndices = originalKeyIndices;
    }

    /**
     * Gets the original indices of the key characters.
     * This might represent a specific ordering or mapping used within the cipher.
     *
     * @return The original indices of the key characters as an integer array.
     */
    public int[] getOriginalKeyIndices() {
        return originalKeyIndices;
    }

    /**
     * Manages the cryptographic key for the system.
     * <p>
     * This method is responsible for handling the entire key management process, including dealing with an
     * existing key, prompting the user for a valid key if none is set, and processing the key for further use.
     */
    public void handleKeyManagement() {
        // Check if an original key is already set.
        if (originalKey != null) {
            // If an original key is set, handle the existing key, prompting the user to change it.
            handleExistingKey();
        } else {
            // If no original key is set, prompt the user for a key until a valid one is provided.
            promptForKeyUntilValid();
        }
        // Process the key, including sorting it and determining original and sorted key indices.
        processKey();
    }

    /**
     * Prints the current key if set, or a message indicating that no key is selected.
     */
    private void printOriginalKey() {
        ConsoleUI.displaySuccessMessage(originalKey != null ?
                "The current key selected is " + Arrays.toString(getOriginalKey()) :
                "No key is selected.");
    }

    /**
     * Handles the scenario where an original key already exists.
     * If the original key is set, it will print the key and then ask the user if they
     * wish to change it. If the user confirms, the original key is set to null and the key is printed again,
     * notifying the user it is now set to null.
     */
    private void handleExistingKey() {
        printOriginalKey();
        // Prompt the user to confirm if they want to change the key. If confirmed, reset the original key print it.
        if (ConsoleUI.performActionIfUserConfirms(ConsoleUI.CHANGE_KEY_PROMPT)) {
            setOriginalKey(null);
            printOriginalKey();
            promptForKeyUntilValid();
        }

    }

    /**
     * Processes the original key by sorting it lexicographically and setting  the sorted keys.
     * It also determines and sets the sorted key indices. If the user is decrypting, it will calculate and set the
     * original key indices as well.
     */
    private void processKey() {
        char[] sortedKey = sortKeyLexicographically(originalKey);
        setSortedKey(sortedKey);

        int[] sortedKeyIndices = returnIndicesOfSortedKey(originalKey, sortedKey);
        setSortedKeyIndices(sortedKeyIndices);

        // If decryption is required, calculate and set original key indices based on the sorted key indices.

        int[] originalKeyIndices = returnIndicesOfOriginalKey(sortedKeyIndices);
        setOriginalKeyIndices(originalKeyIndices);
    }

    /**
     * Validates the given key according to specific criteria.
     * The must be:
     * - Between 5 and 16 characters in length
     * - Must contain only alphanumeric characters
     * - Must not contain any duplicate characters.
     *
     * @param key The key to be validated
     * @return true if the key is valid according to the defined criteria, false otherwise.
     * If the key is invalid, an error message is printed to the console describing the reason.
     */
    private boolean isKeyValid(String key) {
        if (key.length() < 5) {
            ConsoleUI.displayErrorMessage("The key " + key + " length is too small");
            return false;
        } else if (key.length() > 16) {
            ConsoleUI.displayErrorMessage("The key " + key + " is too large.");
            return false;
        } else if (!isKeyAlphaNumeric(key)) {
            ConsoleUI.displayErrorMessage("The key " + key + " contains invalid characters. Please enter alphanumeric characters only.");
            return false;
        } else if (hasDuplicateCharacters(key)) {
            ConsoleUI.displayErrorMessage("The key " + key + " contains duplicate characters.  " +
                    "Please enter a key without duplicate characters.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the key contains only alphanumeric characters.
     *
     * @param key The key to be checked.
     * @return true if the key is alphanumeric, false otherwise.
     */
    private boolean isKeyAlphaNumeric(String key) {
        return key.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Checks if the key contains duplicate characters.
     *
     * @param key The key to be checked
     * @return true if the key contains duplicate characters, false otherwise.
     */
    private boolean hasDuplicateCharacters(String key) {
        // Iterate through the characters of the key using a nested loop
        for (int i = 0; i < key.length(); i++) { // Outer loop iterates through each character of the key
            for (int j = i + 1; j < key.length(); j++) { // Inner loop iterates from the next character to the end.
                // Compare the character at position 'i' with the character at position 'j'
                if (key.charAt(i) == key.charAt(j)) {
                    // If a match is found, then the key contains duplicate characters
                    return true; // Return true to indicate that a duplicate has been found
                }
            }
        }
        // If no duplicates are found, return false
        return false;
    }

    /**
     * Repeatedly prompts the user for a key until a valid key is provided.
     * Uses the ConsoleUI to obtain the key and validate it using the setKey().
     */
    private void promptForKeyUntilValid() {
        while (originalKey == null) {
            String userKey = ConsoleUI.getKeyName();
            if (isKeyValid(userKey)) {
                setOriginalKey(userKey);
                printOriginalKey();
            }

        }
    }

    /**
     * Sorts the given key lexicographically and returns the sorted key.
     *
     * @param key The key to be sorted
     * @return The sorted key
     */
    private char[] sortKeyLexicographically(char[] key) {
        // Clone the original key to create a new copy. This ensures the original will not be changed
        char[] sortedKey = key.clone();
        // Cloned key is sorted lexicographically (numbers first)
        Arrays.sort(sortedKey);
        // The sorted key is set in the class
        return sortedKey;
    }

    /**
     * Returns an array representing the original indices of the characters from the original key after they are stored.
     * <p>
     * The method takes two char arrays, originalKey and sortedKey. The originalKey array represents the
     * original ordering of the characters. The sortedKey array represents the desired sorted ordering. The returned
     * integer array maps each character from the sorted key to its original position in the original key, taking into
     * account the possibility of duplicate characters.
     *
     * @param originalKey The array representing the original ordering of characters.
     * @param sortedKey   The array representing the sorted ordering of characters.
     * @return An integer array containing the original positions of the characters from the originalKey array, after
     * they have been sorted  .
     */
    private int[] returnIndicesOfSortedKey(char[] originalKey, char[] sortedKey) {
        // Create an array to store the order of the sorted indices
        int[] indicesOfSortedKey = new int[originalKey.length];
        // Boolean array to mark used characters in the original key
        boolean[] used = new boolean[originalKey.length];
        for (int i = 0; i < sortedKey.length; i++) {
            // Look for the position of originalKey[i] in sortedKey
            for (int j = 0; j < originalKey.length; j++) {
                // If the character has not been used and matches the character in the sorted key
                if (!used[j] && sortedKey[i] == originalKey[j]) {
                    // Store the index
                    indicesOfSortedKey[i] = j;
                    // Mark the character as used
                    used[j] = true;
                    break;
                }
            }
        }
        return indicesOfSortedKey;
    }

    /**
     * Returns an array representing the original order of characters from the sorted key
     * <p>
     * The method takes an array of integers, sortedIndices, that represent a permutation
     * of indices. It returns a new array that represents the inverse of that permutation.
     * If you apply the original permutation and then its inverse, you will get back the original order.
     *
     * @param sortedKeyIndices The array representing the sorted ordering of indices
     * @return An integer array containing the inverse of the permutation.
     */
    private int[] returnIndicesOfOriginalKey(int[] sortedKeyIndices) {
        // Initialise an array to store the inverse of the sorted key indices.
        int[] originalOrderIndices = new int[sortedKeyIndices.length];
        // Iterate through the sorted key indices.
        for (int i = 0; i < sortedKeyIndices.length; i++) {
            //
            originalOrderIndices[sortedKeyIndices[i]] = i;
        }
        return originalOrderIndices;
    }

}
