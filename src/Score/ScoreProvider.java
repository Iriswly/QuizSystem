package Score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScoreProvider {

    private static final String CSV_FILE_PATH = "resources/users.csv";
    private String[][] userScores;
    private int currentCount = 0;

    public ScoreProvider() {
        userScores = new String[10][10]; // Assume a maximum of 10 users
    }

    public String[][] getAllUserScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the array needs to be expanded
                if (currentCount >= userScores.length) {
                    // Expand array capacity
                    String[][] newScores = new String[userScores.length + 10][10]; // Expand by 10 rows each time
                    System.arraycopy(userScores, 0, newScores, 0, userScores.length);
                    userScores = newScores; // Update reference
                }

                // Split the current line and store it in the array
                userScores[currentCount++] = line.split(",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建返回数组，仅包含实际读取的部分
        String[][] result = new String[currentCount][10];
        for (int i = 0; i < currentCount; i++) {
            result[i] = userScores[i]; // 复制实际读取的内容
        }

        return result; // 返回结果数组
    }
}