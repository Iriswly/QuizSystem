package CSVEditor_QuestionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader extends CSVBase {
    protected List<String> currentLines = new ArrayList<>();
    protected int currentLineIndex;

    public CSVReader() throws Exception {
        super();
    }

    public int getLineCount() {
        return currentLines.size();
    }

    protected boolean readAll() {
        currentLines.clear();
        String[] filePaths = {FILEPATH_MATHEMATICS, FILEPATH_PSYCHOLOGY, FILEPATH_ASTRONOMY, FILEPATH_GEOGRAPHY};

        for (String filePath : filePaths) {
            if (isQuestionCSVExists(filePath)) {
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        currentLines.add(line);
                    }
                    System.out.println("Read all lines from " + filePath + " done");
                } catch (IOException e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            } else {
                System.out.println("No question CSV exists at: " + filePath);
            }
        }
        return true;
    }

    private String readLine(int index) throws Exception {
        if (currentLines.isEmpty()) {
            if (DEBUG) System.out.println("Invalid currentLines");
            throw new Exception("Invalid currentLines");
        }
        if (index < 0 || index >= currentLines.size()) {
            if (DEBUG) System.out.println("Invalid index of line");
            throw new Exception("Invalid index of line");
        }
        return currentLines.get(index);
    }

    public String nextLine() throws Exception {
        if (currentLineIndex >= currentLines.size()) {
            currentLineIndex = 0;
            throw new IndexOutOfBoundsException("End of file");
        }
        return readLine(currentLineIndex++);
    }

    protected void showLines() {
        if (currentLines.isEmpty()) {
            if (DEBUG) System.out.println("The lines are empty");
            return;
        }
        for (String line : currentLines) {
            System.out.println(line);
        }
    }

    public ArrayList<Integer> matchLine(String matchWord, int mode) {
        if (matchWord.contains(",")) {
            if (DEBUG) System.out.println("The searching word should not contain any comma");
            return null;
        }
        return switch (mode) {
            case 1 -> matchLinesExactlyAll(matchWord);
            case 2 -> matchLinesFuzzyAll(matchWord);
            case 3 -> matchLineExactlyUnitFirst(matchWord);
            case 4 -> matchLinesExactlyUnitAll(matchWord);
            default -> {
                if (DEBUG) System.out.println("Invalid mode");
                yield null;
            }
        };
    }

    private ArrayList<Integer> matchLinesExactlyAll(String matchWord) {
        ArrayList<Integer> matchResult = new ArrayList<>();
        for (int i = 0; i < currentLines.size(); i++) {
            String line = currentLines.get(i);
            if (line.contains(matchWord)) {
                matchResult.add(i);
            }
        }
        return matchResult;
    }

    private ArrayList<Integer> matchLinesFuzzyAll(String matchWord) {
        return matchLinesExactlyAll(matchWord.toLowerCase());
    }

    private ArrayList<Integer> matchLineExactlyUnitFirst(String matchWord) {
        for (int i = 0; i < currentLines.size(); i++) {
            String line = currentLines.get(i);
            if (line.contains("," + matchWord + ",")) {
                return new ArrayList<>(Arrays.asList(i));
            }
        }
        return new ArrayList<>();
    }

    private ArrayList<Integer> matchLinesExactlyUnitAll(String matchWord) {
        ArrayList<Integer> matchResult = new ArrayList<>();
        for (int i = 0; i < currentLines.size(); i++) {
            String line = currentLines.get(i);
            if (line.contains("," + matchWord + ",")) {
                matchResult.add(i);
            }
        }
        return matchResult;
    }

    public ArrayList<Integer> matchCol(String matchWord, int colIndex, int mode) {
        if (colIndex < 0 || colIndex >= MAX_COL_NUM) return null;

        return switch (mode) {
            case 1 -> matchColFuzzyUnitAll(matchWord, colIndex);
            case 2 -> matchColExactlyUnitAll(matchWord, colIndex);
            case 3 -> matchColFuzzyUnitFirst(matchWord, colIndex);
            case 4 -> matchColExactlyUnitFirst(matchWord, colIndex);
            default -> {
                if (DEBUG) System.out.println("Invalid mode");
                yield null;
            }
        };
    }

    private ArrayList<Integer> matchColUnitAll(String matchWord, int colIndex, boolean fuzzy) {
        ArrayList<Integer> matchResults = new ArrayList<>();
        ArrayList<ArrayList<String>> data2D = convertTo2D();
        for (int i = 0; i < currentLines.size(); i++) {
            String item = fuzzy ? data2D.get(i).get(colIndex).toLowerCase() : data2D.get(i).get(colIndex);
            if (item.equals(matchWord)) {
                matchResults.add(i);
            }
        }
        return matchResults;
    }

    private ArrayList<Integer> matchColFuzzyUnitAll(String matchWord, int colIndex) {
        return matchColUnitAll(matchWord, colIndex, true);
    }

    private ArrayList<Integer> matchColExactlyUnitAll(String matchWord, int colIndex) {
        return matchColUnitAll(matchWord, colIndex, false);
    }

    private ArrayList<Integer> matchColFuzzyUnitFirst(String matchWord, int colIndex) {
        ArrayList<Integer> allMatches = matchColFuzzyUnitAll(matchWord, colIndex);
        return allMatches.isEmpty() ? new ArrayList<>() : new ArrayList<>(allMatches.subList(0, 1));
    }

    private ArrayList<Integer> matchColExactlyUnitFirst(String matchWord, int colIndex) {
        ArrayList<Integer> allMatches = matchColExactlyUnitAll(matchWord, colIndex);
        return allMatches.isEmpty() ? new ArrayList<>() : new ArrayList<>(allMatches.subList(0, 1));
    }

    private ArrayList<ArrayList<String>> convertTo2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        for (String line : currentLines) {
            data.add(new ArrayList<>(Arrays.asList(line.split(","))));
        }
        return data;
    }

    public static void main(String[] args) {
        try {
            CSVReader reader = new CSVReader();
            reader.readAll(); // Read all lines from the file
            reader.showLines(); // Show all lines
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
