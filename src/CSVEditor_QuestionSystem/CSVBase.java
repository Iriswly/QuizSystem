package CSVEditor_QuestionSystem;

import java.io.File;

public class CSVBase {
    protected final static String RESOURCES_PATH = "resources/questionsBank";
    public final static String FILEPATH_NEW = RESOURCES_PATH + "/subfolder";
    public final static String FILEPATH_MATHEMATICS = RESOURCES_PATH + "/question_mathematics.csv";
    public final static String FILEPATH_PSYCHOLOGY = RESOURCES_PATH + "/question_psychology.csv";
    public final static String FILEPATH_ASTRONOMY = RESOURCES_PATH + "/question_astronomy.csv";
    public final static String FILEPATH_GEOGRAPHY = RESOURCES_PATH + "/question_geography.csv";
    protected final int MAX_COL_NUM = 12;   // remember to -1 when using it
    protected static final boolean DEBUG = true;

    private File file;

    public CSVBase() throws Exception {
        createInitialFiles();
    }

    private void createInitialFiles() throws Exception {
        if (!isQuestionCSVExists(FILEPATH_MATHEMATICS)) {
            CSVCreator(FILEPATH_MATHEMATICS);
        }
        if (!isQuestionCSVExists(FILEPATH_PSYCHOLOGY)) {
            CSVCreator(FILEPATH_PSYCHOLOGY);
        }
        if (!isQuestionCSVExists(FILEPATH_ASTRONOMY)) {
            CSVCreator(FILEPATH_ASTRONOMY);
        }
        if (!isQuestionCSVExists(FILEPATH_GEOGRAPHY)) {
            CSVCreator(FILEPATH_GEOGRAPHY);
        }
    }

    public boolean isQuestionCSVExists(String filePath) {
        file = new File(filePath);
        if (file.exists()) {
            if (DEBUG) System.out.printf("%s already exists\n", filePath);
            return true;
        }
        return false;
    }

    private boolean pathCreator() {
        File path = new File(RESOURCES_PATH);
        if (!path.exists()) {
            if (DEBUG) System.out.println("Creating resources path");
            if (!path.mkdirs()) {
                System.out.println("Failed to create resources path");
                return false;
            }
        }
        if (DEBUG) System.out.println("Resources path exists");
        return true;
    }

    public boolean CSVCreator(String relevantPath) {
        if (!pathCreator()) return false;

        if (DEBUG) System.out.printf("Creating %s file...\n", relevantPath);
        File questionCSV = new File(relevantPath);
        try {
            if (questionCSV.createNewFile()) {
                if (DEBUG) System.out.printf("%s has been created\n", relevantPath);
                return true;
            } else {
                if (DEBUG) System.out.printf("%s already exists\n", relevantPath);
                return false;
            }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
    }

    public boolean newCSVCreator(String newQuestionTopic) {
        String newFilePath = FILEPATH_NEW + "/" + newQuestionTopic + ".csv";
        return CSVCreator(newFilePath);
    }

    public static void main(String[] args) {
        CSVBase base;
        try {
            base = new CSVBase();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Test creating new question CSV file
        String newQuestionFile = "question_history"; // Example
        if (!base.newCSVCreator(newQuestionFile)) {
            System.out.println("Failed to create new question CSV file");
        } else {
            System.out.println("Successfully created: " + newQuestionFile + ".csv");
        }
    }
}
