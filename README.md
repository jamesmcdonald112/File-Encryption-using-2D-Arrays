## ADFGVX Cipher Project
**Author**: @JamesMcDonald  
**Version**: Java 19.0.2  

### Description
This Java application encrypts and decrypts text using the ADFGVX cipher. It was developed for the H.Dip. in Science (Software Development) - Object-Oriented Software Development program, academic year 2023.

## Final Module Grade: 94%
### To Run
1. **Java Installation**: Ensure Java 19.0.2 or higher is installed.
2. **Download and Extract**: Download the .zip file and extract it to a suitable location.
3. **Navigate to Directory**: Open a command-line interface and navigate to the project's Java files.
4. **Compile the Project**: Execute `javac ie/atu/sw/*.java`.
5. **Run the Application**: Start the app with `java ie.atu.sw.Runner`.
6. **Application Guidance**: Follow on-screen instructions for encryption/decryption processes.
7. **Input Directory**: Provide the full path to the folder of text files to be encrypted or decrypted.

### Features
- **Customizable Cipher Key**: Set your cipher key for encryption/decryption.
- **File Parsing**: Reads and processes text files for encryption/decryption.
- **Encryption/Decryption**: Implements the ADFGVX cipher method.
- **Output Handling**: Saves encrypted/decrypted text in a specified output directory.
- **User-Friendly CLI**: Easy-to-use Command-Line Interface.
- **Options Menu**: Conveniently view or clear current settings.
- **Error Handling**: Manages file and input-related errors efficiently.

### Extras
- **Individual File Processing**: Handles each encrypted/decrypted file individually.
- **Output File Naming**: Automatically names output files (e.g., `encrypted1.txt`, `decrypted1.txt`).
- **Pre-Encryption/Decryption Checks**: Verifies the proper format of text and cipher, checks input/output directories, and confirms critical settings.
- **Separation of Concerns**: The ConsoleUI module separates business logic from the user interface.
- **Change Settings**: Option to modify input/output directories and cipher key after initial setup.
- **Directory View Option**: Browse files in the selected directory via the Options Menu.
- **Output Directory Creation**: Automatically generates a directory for output files if not existing.
