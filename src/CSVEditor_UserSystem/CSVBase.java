package CSVEditor_UserSystem;

import java.io.File;

/*
 * CSVBase:
 * Provide file path, file existence check etc.
 */
public class CSVBase {
    protected final String RESOURCES_PATH = "resources";
    protected final String FILEPATH = RESOURCES_PATH + "/users.csv";
    protected final String TEMP_FILEPATH = RESOURCES_PATH + "/temp_users.csv";
    protected final String LAST_FILEPATH = RESOURCES_PATH + "/last.csv";
    protected final int MAX_COL_NUM = 10; // remember to -1 when using it

    // define DEBUG true to print the log to console if you want
    protected static final boolean DEBUG = false;
    protected static Logger logger = new Logger(DEBUG);


    private File file;
    private File temp_file;


    /* constructor
     * check whether the users.csv has been existing.
     * if the csv has not been existing, create one.
     * @return boolean: whether the creating csv operation has been successful.
     * @throws: Exception if the creating failed
     */
    public CSVBase() throws Exception {
        this.file = new File(FILEPATH);
        if (!isUserCSVExists()) {
            if (!usersCSVCreator()) {
                if (DEBUG) logger.log("create users.csv");
                throw new Exception("file not found and create users.csv failed");
            }
            this.file = new File(FILEPATH);
            if (!this.file.exists()) if (DEBUG) {
                logger.log("constructor CSVBase constructor failed");
                logger.log("users.csv do not E after creating!");
                throw new Exception("users.csv do not E after creating!");
            }
        }
    }

    /*
     * tell whether the user.csv has been existed
     * @return boolean: whether the user.csv has been existed
     */
    public boolean isUserCSVExists() {
        file = new File(FILEPATH);
        if (file.exists()) {
            if (DEBUG) logger.log("users.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * tell whether the temp_users.csv has been existed
     * @return boolean
     */
    public boolean isTempUserCSVExists() {
        temp_file = new File(TEMP_FILEPATH);
        if (temp_file.exists()) {
            if (DEBUG) logger.log("temp_users.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * tell whether the last.csv has been existed
     * @return boolean
     */
    public boolean isLastCSVExists() {
        File lastFile = new File(LAST_FILEPATH);
        if (file.exists()) {
            if (DEBUG) logger.log("last.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * create the resource path,
     * called when the resource is found not exist
     */
    private boolean pathCreator() {
        File path = new File(RESOURCES_PATH);
        if (!path.exists()) {
            if (DEBUG) logger.log("creating path resources");
            if (!path.mkdir()) {
                logger.log("path resources creation failed");
                return false;
            }
        } else {
            if (DEBUG) logger.log("path resources has existed.");
            return true;
        }
        if (DEBUG) logger.log("path resources has been created");
        return true;
    }

    /*
     * @param relevantPath: the path to create the csv file
     * @return boolean: whether the creating csv operation has been successful.
     *
     *  create the csv file according to the path String in the parameter
     * called to create the temp_user.csv and users.csv in the context
     * it will check whether the resources path exist, if not, create the path, then the csv file
     */
    private boolean CSVCreator(String relevantPath) {
        if (!pathCreator()) {
            if (DEBUG) logger.log("create resource folder Failed");
            return false;
        }
        if (DEBUG) logger.log("Creating %s file...\n", relevantPath);
        File user_csv = new File(relevantPath);
        try {
            if (user_csv.createNewFile()) {
                if (DEBUG) logger.log("%s has been create\n", relevantPath);
                // 关闭文件
                return true;
            } else {
                if (DEBUG) logger.log("%s already exists\n", relevantPath);
                return false;
            }
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
    }

    /*
     * @return boolean: whether the creating csv operation has been successful.x
     * create the users.csv file
     */
    protected boolean usersCSVCreator() {
        if (!isUserCSVExists()) return CSVCreator(FILEPATH);
        return true;
    }

    /*
     * @return boolean: whether the creating csv operation has been successful.
     * create the temp_users.csv file
     */
    protected boolean tempUsersCSVCreator() {
        if (!isTempUserCSVExists()) return CSVCreator(TEMP_FILEPATH);
        else return true;
    }

    /*
     * @return boolean: whether the creating csv operation has been successful.
     * create the last.csv file
     */
    protected boolean lastCSVCreator() {
        if (!isLastCSVExists()) return CSVCreator(LAST_FILEPATH);
        else return true;
    }

    /*
     * test function for the upper apis
     */
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
        logger.log("1");
        logger.log("");
        // test for usersCSVCreator there
        if (!base.usersCSVCreator()) {
            logger.log("create csv failed");
            return;
        }
        logger.log("2");
        logger.log("");
        // test for tempUsersCSVCreator
        if (!base.tempUsersCSVCreator()) {
            logger.log("create temp csv failed");
            return;
        }
        logger.log("3");
        logger.log("");
    }
}
