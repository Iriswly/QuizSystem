package ScoreDB;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreEditor extends ScoreReader {
    private List<String> currentLines = new ArrayList<>();

    public ScoreEditor() throws Exception {
        super();
        readAll();
    }

    public boolean readAll() {
        if (currentLines == null) currentLines = new ArrayList<>();
        if (isScoresCSVExists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILEPATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    currentLines.add(line);
                }
                logger.log("Read all lines from scores.csv");
            } catch (IOException e) {
                if (DEBUG) e.printStackTrace();
                return false;
            }
        } else {
            logger.log("Scores.csv does not exist.");
            return false;
        }
        return true;
    }

    // 插入新行
    public boolean insertRow(String nickname, String topic, int difficulty, int score) {
        // 参数合法性检测
        readAll();
        if (!isValidInput(nickname) || !isValidInput(topic)) {
            logger.log("Invalid input: nickname or topic contains illegal characters.");
            return false;
        }
        if (difficulty < 1 || difficulty > 4) { // DIFF 1-4
            logger.log("Invalid difficulty: must be between 1 and 10.");
            return false;
        }
        if (score < 0 || score > 100) { // SCORE 0-100
            logger.log("Invalid score: must be between 0 and 100.");
            return false;
        }

        int newId = currentLines.size() + 1;

        String newRow = String.format("%d, %s, %s, %d, %d", newId, nickname, topic, difficulty, score);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILEPATH, true))) {
            bw.write(newRow);
            bw.newLine();
            currentLines.add(newRow);
            logger.log("Inserted new row: " + newRow);
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }

    // when someone update his / her nickname, you'd better use this method to update the nickname in the database
    public boolean updateNickname(String oldNickname, String newNickname) {
        readAll();
        // 参数合法性检测
        if (!isValidInput(oldNickname) || !isValidInput(newNickname)) {
            logger.log("Invalid input: nickname contains illegal characters.");
            return false;
        }

        boolean updated = false;
        for (int i = 0; i < currentLines.size(); i++) {
            String[] fields = currentLines.get(i).split(",");
            if (fields[1].trim().equals(oldNickname)) {
                fields[1] = newNickname;
                currentLines.set(i, String.join(", ", fields));
                updated = true;
            }
        }

        if (!updated) {
            logger.log("No rows found for old nickname: " + oldNickname);
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILEPATH))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine();
            }
            logger.log("Updated nickname from " + oldNickname + " to " + newNickname);
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteByNickname(String nickname) {
        readAll();
        // 参数合法性检测
        if (!isValidInput(nickname)) {
            logger.log("Invalid input: nickname contains illegal characters.");
            return false;
        }

        boolean deleted = false;
        for (int i = 0; i < currentLines.size(); i++) {
            String[] fields = currentLines.get(i).split(",");
            if (fields[1].trim().equals(nickname)) {
                currentLines.remove(i);
                i--; // 调整索引，以便继续检查下一个元素
                deleted = true;
            }
        }

        if (!deleted) {
            logger.log("No rows found for nickname: " + nickname);
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILEPATH))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine();
            }
            logger.log("Deleted rows with nickname: " + nickname);
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }


    private boolean isValidInput(String input) {
        if (input == null || input.isEmpty()) return false;
        return !input.contains(",") && !input.contains("\n");
    }

    public static void main(String[] args) {
        try {
            ScoreEditor editor = new ScoreEditor();

            // 示例 1：插入新行
            System.out.println("插入新行:");
            if (editor.insertRow("Alice", "Math", 2, 95)) {
                System.out.println("成功插入新行!");
            } else {
                System.out.println("插入失败!");
            }

            // 示例 2：更新昵称
            System.out.println("更新昵称:");
            if (editor.updateNickname("Alice", "Alicia")) {
                System.out.println("昵称更新成功!");
            } else {
                System.out.println("昵称更新失败!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
