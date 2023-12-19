package ie.atu.sw;

import java.io.*;

/**
 * The {@code MyFileWriter} class is responsible for writing content to the files within the specified directory.
 * It provides methods to write encrypted or decrypted content to files, ensuring that each file has a unique name.
 * The file names are generated based on whether the content is encrypted or decrypted, and a counter is used to
 * avoid naming conflicts.
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #writeToFile(String, String, boolean)}</li>
 * </ul>
 *
 * <p><b>Private Methods:</b></p>
 * <ul>
 *     <li>{@link #getDefaultFileName(String, String)}</li>
 * </ul>
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see File
 * @see FileWriter
 */
public class MyFileWriter {

    /**
     * Writes the specified content to a file in the given directory. This file name is generated based on whether
     * the content is encrypted or decrypted. If a file with the same name already exists, a new name is generated.
     *
     * @param directory The directory where the file should be created.
     * @param content   The content to be written to the file.
     * @param encrypted A boolean flag indicating whether the content is encrypted (true) or decrypted (false).
     * @throws IOException If there is an error writing to the file.
     */
    public void writeToFile(String directory, String content, boolean encrypted) throws IOException {
        String defaultFileName;
        // Determine the default name based on whether the content is encrypted or decrypted.
        if (encrypted) {
            defaultFileName = getDefaultFileName(directory, "encrypted");
        } else {
            defaultFileName = getDefaultFileName(directory, "decrypted");
        }
        File file = new File(directory, defaultFileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException ioException) {
            throw new IOException("Failed to write to file: " + file + ": " + ioException.getMessage());
        }
    }

    /**
     * Generates a default file name with the specific basename. The method ensures that the filename is unique
     * by appending a counter value to the basename.
     *
     * @param directory The directory where the file will be created.
     * @param basename  The base name for the file(e.g., "encrypted" or "decrypted").
     * @return A unique file name based on the basename.
     */
    private String getDefaultFileName(String directory, String basename) {
        int counter = 1;
        File file;
        // Keep incrementing the counter until a unique filename is found
        do {
            file = new File(directory, basename + counter + ".txt");
            counter++;
        } while (file.exists());

        return file.getName();
    }

}
