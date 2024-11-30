package CSVEditor_UserSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSVReader
 * providing the reading function to put all the data to the temp,
 * finding data, giving data info api, and overall info about the database
 */
public class CSVReader extends CSVBase {
    protected List<String> currentLines = new ArrayList<>();
    protected int currentLineIndex;

    public CSVReader() throws Exception {
        super(); // 简单初始化一下CSVBase
        readAll();
    }

    /*
     *all the user data will be recorded in the attribute currentLines
     * @return true if the reading is successful, false otherwise
     */
    protected boolean readAll() {
        currentLines.clear();
        if (isUserCSVExists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    currentLines.add(line);
                }
                logger.log("read all lines Done");
            } catch (IOException e) {
                if (DEBUG) e.printStackTrace();
                return false;
            }
        } else {
            logger.log("Temp user csv does not exists");
            return false;
        }
        return true;
    }

    /*
     * @return the line count
     * simple count of how many lines (users count) in the database
     */
    public int getLineCount() {
        if (currentLines == null) return 0;
        else return currentLines.size();
    }

    /*
     * @param index the index of the line
     * @return the line (String)
     * simple reading data as string
     */
    private String readLine(int index) throws Exception {
        if (currentLines == null) {
            if (DEBUG) logger.log("invalid currentLines");
            throw new Exception("invalid currentLines");
        }
        if (index < 0 || index > currentLines.size() - 1) {
            if (DEBUG) logger.log("invalid index of line");
            throw new Exception("invalid index of line");
        }
        return currentLines.get(index);
    }

    /*
     * @return the line (String)
     * reading the nextline from the currentLines
     */
    public String nextLine() throws Exception {
        if (currentLineIndex > currentLines.size() - 1) {
            currentLineIndex = 0;
            throw new IndexOutOfBoundsException("end of file");
        }
        if (currentLineIndex < 0) currentLineIndex = 0; // just in case
        currentLineIndex++;
        return readLine(currentLineIndex);
    }

    /*
     * show all the lines in the database (just for debug)
     */
    protected void showLines() {
        if (currentLines == null) {
            if (DEBUG) logger.log("the lines are empty");
            return;
        }
        for (String line : currentLines) {
            logger.log(line);
        }
    }

    /*
     * @return the line index list that matches the search word
     * @param mode:
     * mode 1 = search all lines with exact match ("Abc" != "ABC" != "abc")
     * mode 2 = search all lines with fuzzy match ("Abc" = "ABC" = "abc")
     * mode 3 = search the first line with unit matches (case-sensitive)
     * mode 4 = search all lines that contain the unit that matches (RECOMMENDED!)
     * mode 1 : Case-sensitive search for lines and return all matching lines
     * mode 2 : Fuzzy search for lines and return all matching lines
     * mode 3 : Case-sensitive search for the unit within the first matching line
     * mode 4 : Case-sensitive search for the unit within all matching lines
     */
    public ArrayList<Integer> matchLine(String matchWord, int mode) {
        // the invalid hint would be provided as the null value
        // when the search result is zero, the returnedValue.size should be 0
        // and when there are lines that matches, this function will return the list that contains all the matched line's index

        // the matchWord should not contain any commas
        if (matchWord.contains(",")) {
            if (DEBUG) logger.log("the searching word should not contains any comma");
            return null;
        }
    /*
    mode 1 = search all lines with exact match ("Abc" != "ABC" != "abc")
    mode 2 = search all lines with fuzzy match ("Abc" = "ABC" = "abc")
    mode 3 = search the first line with unit matches (case-sensitive)
    mode 4 = search all lines that contain the unit that matches (RECOMMENDED!)
    mode 1 : Case-sensitive search for lines and return all matching lines
    mode 2 : Fuzzy search for lines and return all matching lines
    mode 3 : Case-sensitive search for the unit within the first matching line
    mode 4 : Case-sensitive search for the unit within all matching lines
     */
        return switch (mode) {
            case 1 -> matchLines_exactly_all(matchWord, false); // Exact case-sensitive search for all matching lines
            case 2 -> matchLines_fuzzy_all(matchWord); // Fuzzy search for all matching lines
            case 3 ->
                    matchLine_exactly_unit_first(matchWord); // Exact case-sensitive search for the first matching line
            case 4 ->
                    matchLines_exactly_unit_all(matchWord); // Exact case-sensitive search for all matching lines with unit
            default -> {
                if (DEBUG) logger.log("invalid mode");
                yield null; // Return null if an invalid mode is provided
            }
        };
    }


    /**
     * Searches all lines for an exact or fuzzy match of the given word.
     *
     * @param matchWord the word to match in the lines
     * @param fuzzy     if true, performs a case-insensitive (fuzzy) match, otherwise exact match
     * @return an ArrayList of line indices where the match is found, or null if any error occurs during adding
     */
    public ArrayList<Integer> matchLines_exactly_all(String matchWord, boolean fuzzy) {
        ArrayList<Integer> matchResult = new ArrayList<>();
        for (int i = 0; i < currentLines.size(); i++) {
            String sentence = fuzzy ? currentLines.get(i).toLowerCase() : currentLines.get(i);
            if (sentence.contains(matchWord)) {
                if (DEBUG) logger.log(
                        "fuzzy: {%b}, matching word: %s in sentence %s\n",
                        fuzzy,
                        matchWord,
                        sentence);
                if (!matchResult.add(i)) {
                    if (DEBUG) logger.log("add failed");
                    return null;
                }
                if (DEBUG) logger.log("match result now: " + matchResult);
            }
        }
        return matchResult;
    }

    /**
     * Performs a fuzzy search across all lines for a given match word.
     *
     * @param matchWord the word to search for
     * @return an ArrayList of line indices where the match is found (fuzzy search)
     */
    public ArrayList<Integer> matchLines_fuzzy_all(String matchWord) {
        return matchLines_exactly_all(matchWord.toLowerCase(), true);
    }

    /**
     * Searches the first line that exactly matches a unit (word) within a sentence.
     *
     * @param matchWord the unit (word) to search for
     * @return an ArrayList containing the index of the first matching line
     */
    public ArrayList<Integer> matchLine_exactly_unit_first(String matchWord) {
        for (int i = 0; i < currentLines.size(); i++) {
            String sentence = currentLines.get(i);
            if (sentence.contains("," + matchWord + ",") || sentence.contains("," + matchWord + '\n') || sentence.contains('\n' + matchWord + ",")) {
                int finalI = i;
                return new ArrayList<>() {{
                    add(finalI);
                }};
            }
        }
        return new ArrayList<>() {{
            add(0);
        }};
    }

    /**
     * Searches all lines for an exact match of a unit (word) within a sentence.
     *
     * @param matchWord the unit (word) to search for
     * @return an ArrayList of indices of lines where the match is found
     */
    public ArrayList<Integer> matchLines_exactly_unit_all(String matchWord) {
        ArrayList<Integer> matchResult = new ArrayList<>();
        for (int i = 0; i < currentLines.size(); i++) {
            String sentence = currentLines.get(i);
            if (sentence.contains("," + matchWord + ",") || sentence.contains("," + matchWord + '\n') || sentence.contains('\n' + matchWord + ",")) {
                matchResult.add(i);
            }
        }
        return matchResult;
    }

    /**
     * Searches for a specific unit in a particular column of a dataset, with different modes of search.
     *
     * @param matchWord the word to search for
     * @param colIndex  the column index to search in
     * @param mode      the search mode (1 for fuzzy all, 2 for exact all, 3 for fuzzy first, 4 for exact first)
     * @return an ArrayList of indices of lines that match the search criteria
     */
    public ArrayList<Integer> matchCol(String matchWord, int colIndex, int mode) {
        // I recommend using this function to search the exact unit
        // because there is checking step in the function
        if (colIndex < 0 || colIndex > MAX_COL_NUM - 1) return null;

        return switch (mode) {
            // Fuzzy search, returns all matching line indices
            case 1 -> matchCol_fuzzy_unit_all(matchWord, colIndex);
            // Exact search, returns all matching line indices
            case 2 -> matchCol_exactly_unit_all(matchWord, colIndex);
            // Fuzzy search, returns the first matching line index
            case 3 -> matchCol_fuzzy_unit_first(matchWord, colIndex);
            // Exact search, returns the first matching line index
            case 4 -> matchCol_exactly_unit_first(matchWord, colIndex);
            default -> {
                if (DEBUG) logger.log("invalid mode");
                yield null;
            }
        };
    }

    /**
     * Converts the dataset of lines into a 2D array where each cell is separated by commas.
     *
     * @return a 2D ArrayList representing the dataset
     */
    private ArrayList<ArrayList<String>> convert2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        for (String line : currentLines) {
            data.add(new ArrayList<>(Arrays.asList(line.split(","))));
        }
        return data;
    }

    /**
     * Searches for a unit in a particular column of a dataset, and returns all lines where the unit is found.
     *
     * @param matchWord the word to search for
     * @param colIndex  the column index to search in
     * @param fuzzy     if true, performs a fuzzy (case-insensitive) search
     * @return an ArrayList of line indices where the unit is found
     */
    public ArrayList<Integer> matchCol_unit_all(String matchWord, int colIndex, boolean fuzzy) {
        // convert the line into the 2 dimension array
        ArrayList<Integer> matchResults = new ArrayList<>();
        matchWord = fuzzy ? matchWord.toLowerCase() : matchWord;
        ArrayList<ArrayList<String>> data2D = convert2D();
        for (int i = 0; i < currentLines.size(); i++) {
            String item = fuzzy ? data2D.get(i).get(colIndex).toLowerCase() : data2D.get(i).get(colIndex);
            if (item.equals(matchWord)) {
                if (DEBUG)
                    logger.log("matching word: " + matchWord + " in column " + colIndex + " in line " + i);
                matchResults.add(i);
            }
        }
        return matchResults;
    }


    /**
     * Performs a fuzzy (case-insensitive) search in the specified column and returns all matching line indices.
     *
     * @param matchWord the word to search for
     * @param colIndex  the column index to search in
     * @return an ArrayList of line indices where the match is found (fuzzy search)
     */
    public ArrayList<Integer> matchCol_fuzzy_unit_all(String matchWord, int colIndex) {
        return matchCol_unit_all(matchWord, colIndex, true);
    }

    /**
     * Performs an exact search in the specified column and returns all matching line indices.
     *
     * @param matchWord the word to search for
     * @param colIndex  the column index to search in
     * @return an ArrayList of line indices where the match is found (exact search)
     */
    public ArrayList<Integer> matchCol_exactly_unit_all(String matchWord, int colIndex) {
        return matchCol_unit_all(matchWord, colIndex, false);
    }

    /**
     * Performs a fuzzy (case-insensitive) search in the specified column and returns the first matching line index.
     *
     * @param matchWord the word to search for
     * @param colIndex  the column index to search in
     * @return an ArrayList containing the index of the first line where the match is found (fuzzy search)
     */
    public ArrayList<Integer> matchCol_fuzzy_unit_first(String matchWord, int colIndex) {
        ArrayList<Integer> allMatches = matchCol_fuzzy_unit_all(matchWord, colIndex);
        if (allMatches.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allMatches.subList(0, Math.min(1, allMatches.size()))); // return the first matching item
    }

    /**
     * Performs an exact search in the specified column and returns the first matching line index.
     *
     * @param matchWord the word to search for
     * @param colIndex  the column index to search in
     * @return an ArrayList containing the index of the first line where the match is found (exact search)
     */
    public ArrayList<Integer> matchCol_exactly_unit_first(String matchWord, int colIndex) {
        ArrayList<Integer> allMatches = matchCol_exactly_unit_all(matchWord, colIndex);
        if (allMatches.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allMatches.subList(0, Math.min(1, allMatches.size()))); // return the first matching item
    }

    public static void main(String[] args) {
        try {
            // Create CSVReader instance
            CSVReader reader = new CSVReader();

            // Simulate reading a file
            reader.currentLines = Arrays.asList(
                    "apple,banana,cherry",
                    "banana,orange,grape",
                    "apple,kiwi,banana",
                    "grape,apple,banana"
            );

            // Example 1: Match all lines containing "banana" (Exact match, mode = 1)
            logger.log("1");
            ArrayList<Integer> result1 = reader.matchLine("banana", 1);
            logger.log("Match all lines containing 'banana' (Exact match, mode = 1): " + result1);

            // Example 2: Match all lines containing "APPLE" (Fuzzy match, mode = 2)
            logger.log("2");
            ArrayList<Integer> result2 = reader.matchLine("APPLE", 2);
            logger.log("Match all lines containing 'APPLE' (Fuzzy match, mode = 2): " + result2);

            // Example 3: Match the first line containing the cell "kiwi" (Exact cell match, mode = 3)
            logger.log("3");
            ArrayList<Integer> result3 = reader.matchLine("kiwi", 3);
            logger.log("Match the first line containing the cell 'kiwi' (Exact cell match, mode = 3): " + result3);

            // Example 4: Match all lines containing the cell "banana" (Exact cell match, mode = 4)
            logger.log("4");
            ArrayList<Integer> result4 = reader.matchLine("banana", 4);
            logger.log("Match all lines containing the cell 'banana' (Exact cell match, mode = 4): " + result4);

            // Example 5: Fuzzy match "APPLE" in column 1 (mode = 1)
            logger.log("5");
            ArrayList<Integer> result5 = reader.matchCol("APPLE", 1, 1);
            logger.log("Fuzzy match 'APPLE' in column 1 (mode = 1): " + result5);

            // Example 6: Exact match "grape" in column 2 (mode = 2)
            logger.log("6");
            ArrayList<Integer> result6 = reader.matchCol("grape", 2, 2);
            logger.log("Exact match 'grape' in column 2 (mode = 2): " + result6);

            // Example 7: Fuzzy match the first occurrence of "apple" in column 0 (mode = 3)
            logger.log("7");
            ArrayList<Integer> result7 = reader.matchCol("apple", 0, 3);
            logger.log("Fuzzy match the first occurrence of 'apple' in column 0 (mode = 3): " + result7);

            // Example 8: Exact match all "banana" cells in column 2 (mode = 4)
            logger.log("8");
            ArrayList<Integer> result8 = reader.matchCol("banana", 2, 4);
            logger.log("Exact match all 'banana' cells in column 2 (mode = 4): " + result8);

            // End
            logger.log("");
            logger.log("DONE!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
