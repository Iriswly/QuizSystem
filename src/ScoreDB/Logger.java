package ScoreDB;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    protected final String name = "Score DB";
    private boolean ENABLE = false;

    public Logger(boolean enable) {
        ENABLE = enable;
    }

    private String getTimestamp() {
        if (!ENABLE) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public void log(String message, Object... args) {
        if (!ENABLE) return;
        String timestamp = getTimestamp();
        String formattedMessage = String.format(message, args);
        System.out.println("[" + timestamp + "] [" + name + "] " + formattedMessage);
    }

    public static void main(String[] args) {
        Logger logger = new Logger(true);
        logger.log("This is a test message with a number: %d", 42);
        logger.log("Another message: %s", "Hello, World!");
    }
}
