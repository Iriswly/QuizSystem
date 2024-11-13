package CSVEditor_UserSystem;

import java.io.File;

public class CSVBase {
    protected final String RESOURCES_PATH = "resources";
    protected final String FILEPATH = RESOURCES_PATH + "/users.csv";
    protected final String TEMP_FILEPATH = RESOURCES_PATH + "/temp_users.csv";
    protected final String LAST_FILEPATH = RESOURCES_PATH + "/last.csv";
    protected final int MAX_COL_NUM = 10; // remember to -1 when using it
    protected static final boolean DEBUG = true;

    private File file;
    private File temp_file;

    public CSVBase() throws Exception{
        this.file = new File(FILEPATH);
        if (!isUserCSVExists()){
            if (!usersCSVCreator()) {
                if (DEBUG) System.out.println("create users.csv");
                throw new Exception("file not found and create users.csv failed");
            }
            this.file = new File(FILEPATH);
            if (!this.file.exists()) if (DEBUG) {
                System.out.println("constructor CSVBase constructor failed");
                System.out.println("users.csv do not E after creating!");
                throw new Exception("users.csv do not E after creating!");
            }
        }
    }

    public boolean isUserCSVExists() {
        file = new File(FILEPATH);
        if (file.exists()){
            if (DEBUG) System.out.println("users.csv has been exists");
            return true;
        }
        return false;
    }

    public boolean isTempUserCSVExists() {
        temp_file  = new File(TEMP_FILEPATH);
        if (temp_file.exists()){
            if (DEBUG) System.out.println("temp_users.csv has been exists");
            return true;
        }
        return false;
    }

    public boolean isLastCSVExists() {
        File lastFile = new File(LAST_FILEPATH);
        if (file.exists()) {
            if (DEBUG) System.out.println("last.csv has been exists");
            return true;
        }
        return false;
    }

    private boolean pathCreator() {
        File path = new File(RESOURCES_PATH);
        if (!path.exists()) {
            if (DEBUG) System.out.println("creating path resources");
            if (!path.mkdir()) {
                System.out.println("path resources creation failed");
                return false;
            }
        } else {
            if (DEBUG) System.out.println("path resources has existed.");
            return true;
        }
        if (DEBUG) System.out.println("path resources has been created");
        return true;
    }

    private boolean CSVCreator(String relevantPath) {
        if (!pathCreator()) {
            if (DEBUG) System.out.println("create resource folder Failed");
            return false;
        }
        if (DEBUG) System.out.printf("Creating %s file...\n", relevantPath);
        File user_csv = new File(relevantPath);
        try {
            if (user_csv.createNewFile()) {
                if (DEBUG) System.out.printf("%s has been create\n", relevantPath);
                // 关闭文件
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

    protected boolean usersCSVCreator() {
        if (!isUserCSVExists()) return CSVCreator(FILEPATH);
        return true;
    }

    protected boolean tempUsersCSVCreator() {
        if (!isTempUserCSVExists()) return CSVCreator(TEMP_FILEPATH);
        else return true;
    }

    protected boolean lastCSVCreator() {
        if (!isLastCSVExists()) return CSVCreator(LAST_FILEPATH);
        else return true;
    }

    public static void main(String[] args) {
        // TODO: MAY BE A GENERAL LOGGER WILL BE IMPLEMENTED
        // LIKE Logger DEBUGGER_LOGGER = Logger.getLogger(Debugger.class.getName());
        CSVBase base;
        try {
            base = new CSVBase();
        } catch (Exception e) {
            if (CSVBase.DEBUG) e.printStackTrace();
            return;
        }
        System.out.println(1);
        System.out.println();
        // test for usersCSVCreator there
        if (!base.usersCSVCreator()) {
            System.out.println("create csv failed");
            return;
        }
        System.out.println(2);
        System.out.println();
        // test for tempUsersCSVCreator
        if (!base.tempUsersCSVCreator()) {
            System.out.println("create temp csv failed");
            return;
        }
        System.out.println(3);
        System.out.println();
    }
}
