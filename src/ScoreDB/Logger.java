package ScoreDB;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class provide an app to print log with timestamp
 */
public class Logger {
    protected final String name = "Score DB";
    private boolean ENABLE = false;

    /**
     * Constructor, initialize
     *
     * @param enable boolean value to enable or disable logging
     */
    public Logger(boolean enable) {
        ENABLE = enable;
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return the string of current timestamp, if the logger has not been enabled, return empty string
     */
    private String getTimestamp() {
        if (!ENABLE) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * format and print the log info
     *
     * @param message print the formatted string of log info
     * @param args    arguments for formatting the message
     */
    public void log(String message, Object... args) {
        if (!ENABLE) return;
        String timestamp = getTimestamp();
        String formattedMessage = String.format(message, args);
        System.out.println("[" + timestamp + "] [" + name + "] " + formattedMessage);
    }

    /**
     * testing the logger class
     */
    public static void main(String[] args) {
        Logger logger = new Logger(true);
        logger.log("This is a test message with a number: %d", 42);
        logger.log("Another message: %s", "Hello, World!");
    }
}
