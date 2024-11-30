package ScoreDB;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ScoreEditor 类继承自 ScoreReader，用于编辑分数记录
 * 它提供了读取所有分数记录、插入新记录和更新玩家昵称的功能
 *
 * ScoreEditor class extends ScoreReader, provides functions to read all scores,
 * insert new records, and update player nicknames ...
 */
public class ScoreEditor extends ScoreReader {
    private List<String> currentLines = new ArrayList<>();

    /**
     * 构造函数，调用父类构造函数并读取所有分数记录
     * constructor, call super constructor and read all scores
     * @throws Exception 当读取分数记录时发生错误
     *          throw Exception when reading scores
     */
    public ScoreEditor() throws Exception {
        super();
        readAll();
    }

    /**
     * 读取 scores.txt 文件中的所有分数记录
     * 如果文件不存在或读取过程中发生错误，返回 false
     * read all the scores data in the scores.txt
     * if the scores.txt do no exists or error happends during the reading process
     *
     * @return boolean 表示读取操作是否成功
     *          whether the reading operation has been successful
     */
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

    /**
     * 插入新分数记录行
     * 在执行插入操作之前，会验证输入数据的合法性
     * @param nickname 玩家昵称
     *                 player nickname (unique)
     * @param topic 主题 topic
     * @param difficulty 难度等级 level of difficulty
     * @param score 分数 score
     * @return boolean 表示插入操作是否成功 whether the insert operation has been successful
     */
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

    /**
     * 更新数据库中的昵称
     * 当用户更改昵称时，使用此方法来更新数据库中的相应信息
     * change the someone's nick name in the database
     * when the user changes his nickname, please use this method to update the database
     *
     * @param oldNickname 旧昵称，用于定位要更新的记录
     *                    old nick name, used to locate the record to be updated
     * @param newNickname 新昵称，用于替换旧昵称
     *                    new nick name, used to replace the old nick name
     * @return 如果更新成功返回true，否则返回false
     *          return true if updated successfully, otherwise return false
     */
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

    /**
     * 根据昵称删除记录
     * 该方法首先读取所有记录，然后遍历这些记录，寻找匹配给定昵称的项并将其删除
     * 如果昵称不合法或没有找到匹配的记录，方法将返回false
     * delete the record in the database according to the nickname
     * this function will read all the record in the database,
     * and then traverse these records to find the matching item according to the given nickname and delete it
     * if the nickname is invalid or no matching record is found, the method will return false.
     *
     * @param nickname 要删除的昵称
     *                 the nick name to be deleted
     * @return 如果删除成功返回true，否则返回false
     *          if deleted successfully, return true, otherwise return false
     */
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

    /**
     * 检查输入字符串是否有效
     * 有效的输入字符串不能为空、不包含逗号、不包含换行符
     * check if the input string is valid
     * the input string is valid if it is not empty, does not contain commas, and does not contain newlines
     *
     * @param input 待检查的输入字符串
     *              the input string to be checked
     * @return 如果输入字符串有效返回true，否则返回false
     *          return true if the input string is valid, otherwise return false
     */
    private boolean isValidInput(String input) {
        if (input == null || input.isEmpty()) return false;
        return !input.contains(",") && !input.contains("\n");
    }

    /**
     * 主函数，演示ScoreEditor的使用
     * simple example of using ScoreEditor
     *
     * @param args 命令行参数
     *             command line arguments
     */
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
