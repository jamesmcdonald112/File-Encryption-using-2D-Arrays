package ie.atu.sw;

import java.io.*;

/**
 * The {@code Parser} class is responsible for reading and parsing text files and directories,
 * including specific handling for the ADFGVX cipher. It offers methods to read a single file,
 * an entire directory, or specialised parsing for ADFGVX formatting files. This class streamlines
 * the process of reading files and applying specific cleaning rules to the content.
 *
 * <p><b>Public Methods:</b></p>
 * <ul>
 *     <li>{@link #parse(String)}</li>
 *     <li>{@link #parseDirectory(String)}</li>
 *     <li>{@link #parseADFGVX(String)}</li>
 *     <li>{@link #parseADFGVXDirectory(String)}</li>
 *     <li>{@link #readFile(File)}</li>
 * </ul>
 *
 * <p><b>Note:</b></p> This parser assumes that the input text files and directories are well-formed and accessible.
 * It returns cleaned text by removing non-alphanumeric characters and transforming the text to uppercase.
 * Specialised methods for ADFGVX handle the specific characters of the cipher.
 *
 * @author jamesMcDonald
 * @version 1.0
 * @see File
 * @see BufferedReader
 */
public class Parser {

    /**
     * Parses a given file and returns the cleaned text.
     *
     * @param file The path to the file to be parsed.
     * @return The cleaned text from the file.
     */
    public String parse(String file) {

        StringBuilder cleanedText = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream((file))))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim().replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
                cleanedText.append(line);
            }
        } catch (IOException ioException) {
            System.err.println("Error reading file " + file + ": " + ioException.getMessage());
            return null;
        }

        return cleanedText.toString();
    }

    /**
     * Parses a given directory and returns the concatenated cleaned text of all files within.
     *
     * @param directoryPath The path to the directory to be parsed.
     * @return The concatenated cleaned text of all files within the directory.
     */
    public String[] parseDirectory(String directoryPath) {
        File path = new File(directoryPath);
        File[] listOfFiles = path.listFiles();

        if (listOfFiles == null) {
            ConsoleUI.displayErrorMessage("Error: The specified directory does not exist or is not a directory");
            return null;
        }
        String[] parsedFiles = new String[listOfFiles.length];
        int index = 0;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    String parsedText = parse(file.getPath());
                    if (parsedText == null) {
                        ConsoleUI.displayErrorMessage("Error parsing file: " + file.getPath());
                    } else {
                        parsedFiles[index] = parsedText;
                        index++;
                    }
                } catch (Exception exception) {
                    ConsoleUI.displayErrorMessage("Error parsing file " + file.getPath() + ": " + exception.getMessage());
                }
            }
        }
        return parsedFiles;
    }

    /**
     * Parses a given file conforming to the ADFGVX cipher and returns the cleaned text.
     * The cleaned text consists of the characters ADFGVX, and all other characters are removed.
     * In the case of an error reading the file, an error message is printed to the console, and the method returns null.
     *
     * @param file The path to the file to be parsed.
     * @return The cleaned text from the ADFGVX file.
     */
    public String parseADFGVX(String file) {
        StringBuilder cleanedText = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().replaceAll("[^ADFGVXadfgvx]", "").toUpperCase();
                cleanedText.append(line);
            }
        } catch (IOException ioException) {
            System.out.println("Error reading ADFGVX file " + file + ": " + ioException.getMessage());
            return null;
        }
        return cleanedText.toString();
    }

    /**
     * Parses a given directory of ADFGVX files and returns the concatenated cleaned text of all files within.
     *
     * @param directoryPath The path to the directory to be parsed.
     * @return The concatenated cleaned text of all ADFGVX files within the directory.
     */
    public String[] parseADFGVXDirectory(String directoryPath) {
        if (directoryPath == null) {
            ConsoleUI.displayErrorMessage("Error: The directory path is null");
            return null; // Return null if the directory or path is null.
        }
        File path = new File(directoryPath);

        if (path.isFile()) {
            return new String[]{parseADFGVX(directoryPath)}; // If it is a file, parse it directly.
        }
        File[] listOfFiles = path.listFiles();

        if (listOfFiles == null) {
            ConsoleUI.displayErrorMessage("Error: The specified directory does not exist or is not a directory");
            return null; // Return null if they directory does not exist, or it is not a directory
        }

        int fileCount = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileCount++; // Count the number of files in the directory in order to create an array to size.
            }
        }

        String[] parsedTexts = new String[fileCount]; // Correctly sized array.
        int index = 0;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    String parsedText = parseADFGVX(file.getPath());
                    if (parsedText == null) {
                        ConsoleUI.displayErrorMessage("Error parsing ADFGVX file: " + file.getPath());
                    } else {
                        parsedTexts[index++] = parsedText; // Add parsed text to the parsedTexts array
                    }
                } catch (Exception exception) {
                    ConsoleUI.displayErrorMessage("Error parsing ADFGVX file " + file.getPath() + ": " + exception.getMessage());
                    // Handle any exceptions that may occur
                }
            }
        }
        return parsedTexts; // Return the array of parsed texts
    }

    /**
     * Reads a given file and returns its content. If an error occurs while reading the file,
     * the exception's stack trace is printed, and the method returns whatever content has been read up to that point,
     * which may be an empty string if the error occurred at the beginning.
     *
     * @param file The {@code File} object representing the file to be read.
     * @return The content of the file as a string.
     */
    public String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }


}
