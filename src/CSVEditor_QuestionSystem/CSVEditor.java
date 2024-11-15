package CSVEditor_QuestionSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVEditor extends CSVReader {
    private final String TEMP_FILEPATH = RESOURCES_PATH + "/temp_questions.csv";
    protected final String LAST_FILEPATH = RESOURCES_PATH + "/last_questions.csv"; // 新增的 LAST_FILEPATH

    public CSVEditor() throws Exception {
        super();
    }

    private boolean dumpAllToTemp() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEMP_FILEPATH))) {
            if (currentLines == null) {
                if (DEBUG) System.out.println("currentLines is null");
                return false;
            }
            for (String line : currentLines) {
                bw.write(line + '\n');
            }
            System.out.println("Dump all lines to temp csv done");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean editLine(int lineIndex, String newLine) {
        if (currentLines == null) throw new IndexOutOfBoundsException("Invalid currentLines");
        try {
            currentLines.set(lineIndex, newLine);
        } catch (IndexOutOfBoundsException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean addNewLine(String newLine) {
        if (currentLines == null) throw new IllegalStateException("Invalid currentLines");
        currentLines.add(newLine);
        return true;
    }

    protected boolean deleteLine(int lineIndex) {
        if (currentLines == null) throw new IllegalStateException("Invalid currentLines");
        if (lineIndex < 0 || lineIndex >= currentLines.size()) return false;
        currentLines.remove(lineIndex);
        return true;
    }

    public boolean operationsDB(int mode, ArrayList<String> newLine, int lineIndex) {
        switch (mode) {
            case 1: // ADD
                return executeAddOperation(newLine);
            case 2: // EDIT
                return executeEditOperation(newLine, lineIndex);
            case 3: // DELETE
                return executeDeleteOperation(lineIndex);
            default:
                if (DEBUG) System.out.println("Invalid mode");
                return false;
        }
    }

    private boolean executeAddOperation(ArrayList<String> newLine) {
        String tempLine = inputLineFormatter(newLine);
        if (tempLine == null) return false;
        try {
            if (addNewLine(tempLine)) {
                if (DEBUG) System.out.println("addNewLine done");
                return overcast();
            }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
        }
        return false;
    }

    private boolean executeEditOperation(ArrayList<String> newLine, int lineIndex) {
        String tempLine = inputLineFormatter(newLine);
        if (tempLine == null) return false;
        try {
            if (editLine(lineIndex, tempLine)) {
                if (DEBUG) System.out.println("editLine done");
                return overcast();
            }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
        }
        return false;
    }

    private boolean executeDeleteOperation(int lineIndex) {
        try {
            if (deleteLine(lineIndex)) {
                if (DEBUG) System.out.println("deleteLine done");
                return overcast();
            }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
        }
        return false;
    }

    protected boolean overcast() {
        if (currentLines == null) {
            if (DEBUG) System.out.println("currentLines is null");
            readAll();
        }

        dumpAllToTemp();

        File userCSV = new File(FILEPATH_MATHEMATICS);
        File lastCSV = new File(LAST_FILEPATH);
        File tempCSV = new File(TEMP_FILEPATH);

        lastCSV.delete();
        if (lastCSV.exists()) {
            if (DEBUG) System.out.println("Deleted last.csv FAILED");
            return false;
        }
        if (DEBUG) System.out.println("Deleted last.csv");
        if (!userCSV.renameTo(lastCSV)) return false;
        if (DEBUG) System.out.println("Renamed users.csv to last.csv");
        if (!tempCSV.renameTo(userCSV)) return false;
        if (DEBUG) System.out.println("Renamed temp.csv to user.csv");
        return true;
    }

    public String inputLineFormatter(ArrayList<String> line) {
        if (currentLines == null) readAll();
        if (line.size() >= 1) {
            return String.join(",", line);
        }
        return null;
    }

    public static void main(String[] args) {
        CSVEditor editor;

        try {
            editor = new CSVEditor();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Reading all lines...");
        editor.readAll();
        editor.showLines();

        System.out.println("Adding a new line...");
        try {
            editor.addNewLine("new question, answer1, answer2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Editing line 0...");
        try {
            editor.editLine(0, "edited question, answer1, answer2");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Line index out of bounds");
        }
        editor.showLines();

        System.out.println("Deleting line 1...");
        try {
            editor.deleteLine(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.showLines();

        if (!editor.overcast()) {
            System.out.println("Overcast failed");
        }
    }
}
