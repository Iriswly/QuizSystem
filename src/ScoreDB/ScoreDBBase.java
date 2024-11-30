package ScoreDB;

import java.io.File;

/*
 * ScoreBase 的作用：
 * 提供文件路径，文件是否存在等基本功能。
 */
public class ScoreDBBase {
    protected final String RESOURCES_PATH = "resources";
    protected final String SCORE_FOLDER = RESOURCES_PATH + "/score";
    protected final String SCORE_FILEPATH = SCORE_FOLDER + "/scores.txt";
    protected final String TEMP_SCORE_FILEPATH = SCORE_FOLDER + "/temp_scores.txt";
    protected final String LAST_SCORE_FILEPATH = SCORE_FOLDER + "/last_scores.txt";

    // DEBUG 模式设置成true可以打开用来输出日志
    protected static final boolean DEBUG = false;
    protected static Logger logger = new Logger(DEBUG);

    private File scoreFile;
    private File tempScoreFile;

    /* constructor
     * 检查scores.csv 是否存在，如果不存在就创建，创建失败就报错 Exception
     */
    public ScoreDBBase() throws Exception {
        this.scoreFile = new File(SCORE_FILEPATH);
        if (!isScoresCSVExists()) {
            if (!scoresCSVCreator()) {
                if (DEBUG) logger.log("create scores.csv failed");
                throw new Exception("file not found and create scores.csv failed");
            }
            this.scoreFile = new File(SCORE_FILEPATH);
            if (!this.scoreFile.exists()) {
                if (DEBUG) {
                    logger.log("constructor ScoreBase failed");
                    logger.log("scores.csv do not exist after creating!");
                }
                throw new Exception("scores.csv do not exist after creating!");
            }
        }
    }

    /*
     * 判断scores.csv文件是否存在
     * @return boolean
     */
    public boolean isScoresCSVExists() {
        scoreFile = new File(SCORE_FILEPATH);
        if (scoreFile.exists()) {
            if (DEBUG) logger.log("scores.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * 判断temp_scores.csv文件是否存在
     * @return boolean
     */
    public boolean isTempScoresCSVExists() {
        tempScoreFile = new File(TEMP_SCORE_FILEPATH);
        if (tempScoreFile.exists()) {
            if (DEBUG) logger.log("temp_scores.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * 判断last_scores.csv文件是否存在
     * @return boolean
     */
    public boolean isLastScoresCSVExists() {
        File lastScoreFile = new File(LAST_SCORE_FILEPATH);
        if (lastScoreFile.exists()) {
            if (DEBUG) logger.log("last_scores.csv has been exists");
            return true;
        }
        return false;
    }

    /*
     * 创建路径 resources/score
     */
    private boolean scoreFolderCreator() {
        File path = new File(SCORE_FOLDER);
        if (!path.exists()) {
            if (DEBUG) logger.log("creating path score");
            if (!path.mkdirs()) {
                logger.log("path score creation failed");
                return false;
            }
        } else {
            if (DEBUG) logger.log("path score has existed.");
            return true;
        }
        if (DEBUG) logger.log("path score has been created");
        return true;
    }

    /*
     * 根据传入的路径创建csv文件
     * 支持scores.csv、temp_scores.csv和last_scores.csv
     * 先检查路径是否存在，如果不存在创建，再创建文件
     */
    private boolean CSVCreator(String relevantPath) {
        if (!scoreFolderCreator()) {
            if (DEBUG) logger.log("create score folder Failed");
            return false;
        }
        if (DEBUG) logger.log("Creating %s file...\n", relevantPath);
        File csvFile = new File(relevantPath);
        try {
            if (csvFile.createNewFile()) {
                if (DEBUG) logger.log("%s has been created\n", relevantPath);
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
     * 创建scores.csv文件
     */
    protected boolean scoresCSVCreator() {
        if (!isScoresCSVExists()) return CSVCreator(SCORE_FILEPATH);
        return true;
    }

    /*
     * 创建temp_scores.csv文件
     */
    protected boolean tempScoresCSVCreator() {
        if (!isTempScoresCSVExists()) return CSVCreator(TEMP_SCORE_FILEPATH);
        return true;
    }

    /*
     * 创建last_scores.csv文件
     */
    protected boolean lastScoresCSVCreator() {
        if (!isLastScoresCSVExists()) return CSVCreator(LAST_SCORE_FILEPATH);
        return true;
    }

    /*
     * 测试方法
     */
    public static void main(String[] args) {
        ScoreDBBase base;
        try {
            base = new ScoreDBBase();
        } catch (Exception e) {
            if (DEBUG) e.printStackTrace();
            return;
        }

        // Test for scoresCSVCreator
        if (!base.scoresCSVCreator()) {
            logger.log("create scores.csv failed");
            return;
        }
        logger.log("scores.csv created successfully.");

        // Test for tempScoresCSVCreator
        if (!base.tempScoresCSVCreator()) {
            logger.log("create temp_scores.csv failed");
            return;
        }
        logger.log("temp_scores.csv created successfully.");

        // Test for lastScoresCSVCreator
        if (!base.lastScoresCSVCreator()) {
            logger.log("create last_scores.csv failed");
            return;
        }
        logger.log("last_scores.csv created successfully.");
    }
}
