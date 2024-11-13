package CSVEditor;

import java.io.File;

/*
 * CSVBase 的作用：
 * 提供文件路径，文件是否存在等基本功能
 */
public class CSVBase {
    protected final String RESOURCES_PATH = "resources";
    protected final String FILEPATH = RESOURCES_PATH + "/users.csv";
    protected final String TEMP_FILEPATH = RESOURCES_PATH + "/temp_users.csv";
    protected final String LAST_FILEPATH = RESOURCES_PATH + "/last.csv";
    protected final int MAX_COL_NUM = 10; // remember to -1 when using it

    // DEBUG 模式默认打开用来输出日志
    protected static final boolean DEBUG = true;

    private File file;
    private File temp_file;


    /* constructor
     * 检查users.csv 是否存在，如果不存在就创建，创建失败就报错 Exception
     */
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

    /*
     * 判断users.csv文件是否存在
     * @return boolean
     */
    public boolean isUserCSVExists() {
        file = new File(FILEPATH);
        if (file.exists()){
            if (DEBUG) System.out.println("users.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * 判断temp_users.csv文件是否存在
     * @return boolean
     */
    public boolean isTempUserCSVExists() {
        temp_file  = new File(TEMP_FILEPATH);
        if (temp_file.exists()){
            if (DEBUG) System.out.println("temp_users.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * 判断last.csv文件是否存在
     * @return boolean
     */
    public boolean isLastCSVExists() {
        File lastFile = new File(LAST_FILEPATH);
        if (file.exists()) {
            if (DEBUG) System.out.println("last.csv has been exists");
            return true;
        }
        return false;
    }

    /*
    创建resources路径，多用于resources不存在时的修补
     */
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

    /*
    根据传入的路径创建csv文件
    即 既可以创建temp_users.csv 也可以创建users.csv
    先检查resources路径存在与否，如果不存在创建一个，再创建文件
    创建失败就报错或者返回false
     */
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

    /*
    创建users.csv文件
     */
    protected boolean usersCSVCreator() {
        if (!isUserCSVExists()) return CSVCreator(FILEPATH);
        return true;
    }

    /*
    创建temp_users.csv文件
     */
    protected boolean tempUsersCSVCreator() {
        if (!isTempUserCSVExists()) return CSVCreator(TEMP_FILEPATH);
        else return true;
    }

    /*
     * 创建last.csv文件
     */
    protected boolean lastCSVCreator() {
        if (!isLastCSVExists()) return CSVCreator(LAST_FILEPATH);
        else return true;
    }

    /*
     * 只是测试
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
