package Score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScoreProvider {

    private static final String CSV_FILE_PATH = "resources/users.csv";
    private String[][] userScores;
    private int currentCount = 0;

    public ScoreProvider() {
        userScores = new String[10][10]; // 假设最多有10个用户
    }

    public String[][] getAllUserScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 检查是否需要扩展数组
                if (currentCount >= userScores.length) {
                    // 扩展数组容量
                    String[][] newScores = new String[userScores.length + 10][10]; // 每次扩展10行
                    System.arraycopy(userScores, 0, newScores, 0, userScores.length);
                    userScores = newScores; // 更新引用
                }

                // 将当前行分割并存入数组
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