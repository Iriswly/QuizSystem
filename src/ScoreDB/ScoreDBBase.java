package ScoreDB;

import java.io.File;

/**
 * ScoreBase:
 * provide the csv resource path...
 */
public class ScoreDBBase {
    protected final String RESOURCES_PATH = "resources";
    protected final String SCORE_FOLDER = RESOURCES_PATH + "/score";
    protected final String SCORE_FILEPATH = SCORE_FOLDER + "/scores.txt";
    protected final String TEMP_SCORE_FILEPATH = SCORE_FOLDER + "/temp_scores.txt";
    protected final String LAST_SCORE_FILEPATH = SCORE_FOLDER + "/last_scores.txt";

    protected static final boolean DEBUG = false;
    protected static Logger logger = new Logger(DEBUG);

    private File scoreFile;
    private File tempScoreFile;

    /**
     * constructor
     * check whether the scores.txt exists, if not, create it,
     * and if creating failed, throw an Exception
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

    /**
     * tell whether the scores.txt has been existing.
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

    /**
     * tell whether the temp_scores.txt has been existing.
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

    /**
     * tell whether the last_scores.txt has been existing.
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

    /**
     * 创建路径 resources/score, if failed, return false
     * creating path resource/score, if failed, return false
     * @return boolean: whether the creating operation has been successful.
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

    /**
     * creating the txt file according the argument path
     * check whether the path to the score txt has been exist
     * if not, create it, then create the scores.txt file
     *
     * @param relevantPath: the path of the txt file
     * @return boolean: whether the creating csv operation has been successful.
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

    /**
     * 创建scores.txt文件
     * check whether the scores.csv has been existing.
     * @return boolean: whether the creating csv operation has been successful.
     */
    protected boolean scoresCSVCreator() {
        if (!isScoresCSVExists()) return CSVCreator(SCORE_FILEPATH);
        return true;
    }

    /**
     * 创建 temp_scores.txt 文件
     * creating the temp_scores.csv file
     * @return boolean: whether the creating csv operation has been successful.
     */
    protected boolean tempScoresCSVCreator() {
        if (!isTempScoresCSVExists()) return CSVCreator(TEMP_SCORE_FILEPATH);
        return true;
    }

    /**
     * creating the last_scores.txt file
     * @return boolean: whether the creating csv operation has been successful.
     */
    protected boolean lastScoresCSVCreator() {
        if (!isLastScoresCSVExists()) return CSVCreator(LAST_SCORE_FILEPATH);
        return true;
    }

    /**
     * 测试方法
     * testing methods above
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
