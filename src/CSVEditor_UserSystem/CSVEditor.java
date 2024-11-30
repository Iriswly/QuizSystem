package CSVEditor_UserSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVEditor extends CSVReader {
    public CSVEditor() throws Exception {
        super();
        readAll();
    }

    /*
    This method writes the contents of currentLines to a temporary CSV file (TEMP_FILEPATH).
     If the CSV file doesn't exist, it is created.
      Each line from currentLines is written to the file, and a newline is added after each line.
    * @return boolean - true if the operation is successful, false otherwise.
    * @throws: IOException if there are issues while writing to the file.
     */
    private boolean dumpAllToTemp() {
        if (!isTempUserCSVExists()) tempUsersCSVCreator();
        if (currentLines == null) {
            if (DEBUG) logger.log("currentLines is null");
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEMP_FILEPATH))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine(); // make sure no new line here
            }
            logger.log("dump all lines to temp csv Done");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Edits a specific line in the list of lines (`currentLines`), replacing it with the given new line.
     *
     * @param lineIndex The index of the line to be edited.
     * @param newLine   The new line content that will replace the old line.
     * @return `true` if the line was successfully edited, `false` if there was an error or if the lineIndex is invalid.
     * @throws IndexOutOfBoundsException if the provided lineIndex is out of bounds.
     */
    protected boolean editLine(int lineIndex, String newLine) throws IndexOutOfBoundsException {
        if (currentLines == null) throw new IndexOutOfBoundsException("invalid currentLines");
        try {
            currentLines.set(lineIndex, newLine);
            // throw IndexOutOfBoundsException
        } catch (IndexOutOfBoundsException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Adds a new line to the `currentLines` list. The input `newLine` should be pre-formatted.
     *
     * @param newLine The line to be added.
     * @return `true` if the new line was successfully added, `false` otherwise.
     * @throws Exception if `currentLines` is null.
     */
    protected boolean addNewLine(String newLine) throws Exception {
        if (currentLines == null) throw new Exception("invalid currentLines");
        logger.log("========= TEMP DEBUG =========");
        logger.log(currentLines.toString());
        logger.log("========= TEMP DEBUG =========");
        currentLines.add(newLine);
//        currentLines.add(newLine);
//        currentLines.add(newLine);
        logger.log("========= TEMP DEBUG =========");
        logger.log(currentLines.toString());
        logger.log("========= TEMP DEBUG =========");
        return true;
    }

    /**
     * Deletes a specific line at the given index in `currentLines`.
     *
     * @param lineIndex The index of the line to be deleted.
     * @return `true` if the line was successfully deleted, `false` if the index is out of bounds or `currentLines` is null.
     * @throws Exception if `currentLines` is null.
     */
    protected boolean deleteLine(int lineIndex) throws Exception {
        if (currentLines == null) throw new Exception("invalid currentLines");
        if (lineIndex < 0 || lineIndex > currentLines.size() - 1) return false;
        currentLines.remove(lineIndex);
        return true;
    }

    /**
     * Executes operations on the database (add, edit, or delete) based on the specified mode.
     *
     * @param mode The operation mode:
     *             1 for adding a new line,
     *             2 for editing an existing line,
     *             3 for deleting a line.
     * @param newLine The new line content (required for add/edit operations).
     * @param lineIndex The index of the line to edit or delete (required for edit and delete operations).
     * @return `true` if the operation was successful, `false` if there was an error.
     */
    public boolean operationsDB(int mode, ArrayList<String> newLine, int lineIndex) {
        switch (mode) {
            case 1: // ADD
            {
                String tempLine = inputLineFormatter(newLine);
                if (tempLine == null) return false;
                try {
                    if (addNewLine(tempLine)) {
                        if (DEBUG) logger.log("addNewLine Done");
                        if (overcast()) {
                            if (DEBUG) logger.log("overcast Done");
                            return true;
                        }
                    } else {
                        if (DEBUG) logger.log("addNewLine failed");
                    }
                    return false;
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            }
            case 2: // EDIT
            {
                String tempLine = inputLineFormatter(newLine);
                if (tempLine == null) {
                    if (DEBUG) logger.log("EDIT failed: newLine is null");
                    return false;
                }
                if (lineIndex == -1) {
                    if (DEBUG) logger.log("user not found");
                    return false;
                }
                try {
                    if (editLine(lineIndex, tempLine)) {
                        if (overcast()) {
                            if (DEBUG) logger.log("overcast Done");
                            return true;
                        }
                        return false;
                    } else {
                        if (DEBUG) logger.log("editLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            }

            case 3: // DELETE
                try {
                    if (lineIndex == -1) {
                        if (DEBUG) logger.log("user not found");
                        return false;
                    }

                    if (deleteLine(lineIndex)) {
                        if (overcast()) {
                            if (DEBUG) logger.log("overcast Done");
                            return true;
                        }
                        return false;
                    } else {
                        if (DEBUG) logger.log("deleteLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }

            default:
                if (DEBUG) logger.log("invalid mode");
                return false;
        }
    }

    /**
     * Commits the changes made in `currentLines` to the database. First, it backs up the current `users.csv` to `last.csv` as a backup.
     * Then it updates the `users.csv` with the current data.
     *
     * @return `true` if the commit process is successful, `false` if there was an error.
     */
    protected boolean overcast() {
        if (currentLines == null) {
            if (DEBUG) logger.log("currentLines is null");
            readAll();  // read all the content to the currentLines
        }

        // debug there
        if (DEBUG) {
            logger.log("currentLines content:");
            for (String line : currentLines) {
                logger.log(line);
            }
        }

        if (!isUserCSVExists()) usersCSVCreator();
        if (!isLastCSVExists()) lastCSVCreator();

        File userCSV = new File(FILEPATH);
        File lastCSV = new File(LAST_FILEPATH);

        // copy the content in the users.csv to the last.csv to backup
        try (FileInputStream fis = new FileInputStream(userCSV);
             FileOutputStream fos = new FileOutputStream(lastCSV)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            if (DEBUG) logger.log("copy users.csv to last.csv done");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        // write the currentLines into the users.csv to make sure the users.csv is always the newest version
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userCSV))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine();
            }
            if (DEBUG) logger.log("writing the newest content to users.csv done");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        if (DEBUG) logger.log("commit changed DONE");
        return true;
    }


    /**
     * This method formats the output line from the database. It splits the line at commas and returns an ArrayList
     * containing the elements of the line. It ensures that the line index is valid before performing the operation.
     *
     * @param lineIndex the index of the line to be formatted.
     * @return an ArrayList<String> containing the formatted line, or null if the index is invalid or the line has fewer than 10 elements.
     */
    public ArrayList<String> outputLineFormatter(int lineIndex) {
        if (currentLines == null) readAll();
        if (lineIndex < 0 || lineIndex > currentLines.size() - 1) return null;
        ArrayList<String> line = new ArrayList<>(List.of(currentLines.get(lineIndex).split(",")));
        if (line.size() >= 10) return line;
        return null;
    }

    /**
     * This method saves the current changes and updates the data. It attempts to perform an overcast operation,
     * then reads all lines again to ensure the data is updated.
     *
     * @return true if the save and update operation is successful, false otherwise.
     */
    protected boolean saveAndUpdate() {
        if (currentLines == null) {
            if (DEBUG) logger.log("currentLines is null");
            return false;
        }
        if (overcast()) {
            if (DEBUG) logger.log("overcast Done");
        } else {
            if (DEBUG) logger.log("overcast failed");
            return false;
        }
        readAll();
        return true;
    }

    /**
     * Validates whether the given string is non-null and not empty.
     *
     * @param str the string to be validated.
     * @return true if the string is valid (non-null and not empty), false otherwise.
     */
    private boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Validates whether the given string can be parsed as a valid integer.
     *
     * @param str the string to be validated.
     * @return true if the string can be parsed as an integer, false otherwise.
     */
    private boolean isValidDigit(String str) {
        if (isValidString(str)) {
            try {
                Integer.parseInt(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Validates the format of the external input. Checks if the input line contains the correct number of elements
     * and whether each element has the expected format (string or digit).
     *
     * @param line the input line to be validated.
     * @return true if the line format is valid, false otherwise.
     */
    protected boolean isFormatValid(ArrayList<String> line) {
        if (line.size() < 10) return false;
        return (isValidString(line.get(0))) && (isValidString(line.get(1))) && (isValidString(line.get(2)))
                && (isValidDigit(line.get(3)))
                && (isValidString(line.get(4))) && (isValidDigit(line.get(5)))
                && (isValidString(line.get(6))) && (isValidDigit(line.get(7)))
                && (isValidString(line.get(8))) && (isValidDigit(line.get(9)));
    }

    /**
     * Formats the input line by checking its validity and then joining the elements with commas.
     *
     * @param line the input line to be formatted.
     * @return a string representing the formatted line, or null if the line is invalid.
     */
    public String inputLineFormatter(ArrayList<String> line) {
        if (currentLines == null) readAll();
        if (isFormatValid(line)) {
            return String.join(",", line);
        }
        return null;
    }

    /**
     * The main method to demonstrate the functionality of the CSVEditor. It performs several operations
     * such as reading lines, adding new lines, editing existing lines, and dumping data to a temporary file.
     *
     * @param args command-line arguments (not used in this example).
     */
    public static void main(String[] args) {
        CSVEditor writer;

        try {
            writer = new CSVEditor();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        logger.log("1");
        logger.log("");

        writer.readAll();

        logger.log("2");
        logger.log("");

        writer.showLines();

        logger.log("3");
        logger.log("");

        if (!writer.dumpAllToTemp()) {
            logger.log("failed to dump");
            return;
        }

        logger.log("4");
        logger.log("");

        try {
            writer.addNewLine("new line");
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.log("5");
        logger.log("");

        try {
            for (int i = 0; i < writer.currentLines.size(); i++) logger.log(writer.nextLine());
        } catch (Exception e) {
            logger.log("out now");
        }

        logger.log("6");
        logger.log("");

        try {
            writer.editLine(1, "edited line");
        } catch (IndexOutOfBoundsException e) {
            logger.log("die there");
        }
        writer.showLines();

        logger.log("7");
        logger.log("");

        if (!writer.overcast()) {
            logger.log("overcast failed");
        }
    }

}
