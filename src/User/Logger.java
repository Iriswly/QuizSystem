package User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger 类用于记录带有时间戳的日志信息
 * Logger class provide an app to print log with timestamp
 */
public class Logger {
    protected final String name = "User API";
    private boolean ENABLE = false;

    /**
     * 构造函数，初始化日志记录的启用状态
     * Constructor, initialize
     *
     * @param enable 布尔值，决定是否启用日志记录功能
     *               boolean value to enable or disable logging
     */
    public Logger(boolean enable) {
        ENABLE = enable;
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return 当前时间的字符串表示，如果日志记录未启用则返回空字符串
     *         the string of current timestamp, if the logger has not been enabled, return empty string
     */
    private String getTimestamp() {
        if (!ENABLE) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 格式化并打印日志信息
     * format and print the log info
     *
     * @param message 日志消息的格式字符串
     *                print the formatted string of log info
     * @param args    用于格式化消息的参数数组
     *                arguments for formatting the message
     */
    public void log(String message, Object... args) {
        if (!ENABLE) return;
        String timestamp = getTimestamp();
        String formattedMessage = String.format(message, args);
        System.out.println("[" + timestamp + "] [" + name + "] " + formattedMessage);
    }

    /**
     * 主函数，用于测试 Logger 类
     * testing the logger class
     */
    public static void main(String[] args) {
        Logger logger = new Logger(true);
        logger.log("This is a test message with a number: %d", 42);
        logger.log("Another message: %s", "Hello, World!");
    }
}
