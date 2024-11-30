package ScoreDB;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * ScoreReader class
 * This class provides methods for reading and managing score data from the scores.csv file
 */
public class ScoreReader extends ScoreDBBase {
    protected List<String> currentLines = new ArrayList<>();
    private final int MAX_COL_NUM = 5; // id, nickname, topic, difficulty, score

    public ScoreReader() throws Exception {
        super();
        readAll();
    }

    /*
     * 读取所有数据
     * Reads all data from the scores.csv file into the currentLines list
     * @return Returns true if the read is successful, otherwise returns false
     */
    protected boolean readAll() {
        currentLines = new ArrayList<>();
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
     * 获取个人成绩信息
     * Gets an individual's score information based on conditions such as nickname, topic, and difficulty
     * @param nickname The nickname of the player
     * @param topicFilter The topic filter, null for no filter
     * @param difficultyFilter The difficulty filter, null for no filter
     * @param ascending Whether the scores are sorted in ascending order
     * @return Returns a list of score information that meets the conditions
     */
    public List<String> getPersonalScores(
            String nickname,
            String topicFilter,
            Integer difficultyFilter,
            boolean ascending) {
        List<String> results = new ArrayList<>();

        for (String line : currentLines) {
            String[] fields = line.split(",");
            if (fields.length < MAX_COL_NUM) continue;

            String nicknameField = fields[1].trim();
            String topicField = fields[2].trim();
            int difficultyField = Integer.parseInt(fields[3].trim());
            int scoreField = Integer.parseInt(fields[4].trim());

            boolean matches = nicknameField.equals(nickname) &&
                    (topicFilter == null || topicField.equals(topicFilter)) &&
                    (difficultyFilter == null || difficultyField == difficultyFilter);

            if (matches) {
                results.add(line);
            }
        }

        results.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split(",")[4].trim());
            int scoreB = Integer.parseInt(b.split(",")[4].trim());
            return ascending ? Integer.compare(scoreA, scoreB) : Integer.compare(scoreB, scoreA);
        });

//        return results.subList(0, Math.min(limit, results.size()));
        return results;
    }

    /**
     * 获取公共成绩信息
     * Gets public score information based on conditions such as topic and difficulty
     * @param topic The topic filter, null for no filter
     * @param difficulty The difficulty filter, null for no filter
     * @param ascending Whether the scores are sorted in ascending order
     * @return Returns a list of score information that meets the conditions
     */
    // sorting and return according to the topic and difficulty, you can specify the limit, ascending rule
    public List<String> getPublicScores(
            String topic,
            Integer difficulty,
            boolean ascending) {
        List<String> results = new ArrayList<>();

        for (String line : currentLines) {
            String[] fields = line.split(",");
            if (fields.length < MAX_COL_NUM) continue;

            String topicField = fields[2].trim();
            int difficultyField = Integer.parseInt(fields[3].trim());
            int scoreField = Integer.parseInt(fields[4].trim());

            boolean matches = (topic == null || topicField.equals(topic)) &&
                    (difficulty == null || difficultyField == difficulty);

            if (matches) {
                results.add(line);
            }
        }

        // sorting
        results.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split(",")[4].trim());
            int scoreB = Integer.parseInt(b.split(",")[4].trim());
            return ascending ? Integer.compare(scoreA, scoreB) : Integer.compare(scoreB, scoreA);
        });

//        return results.subList(0, Math.min(limit, results.size()));
        return results;
    }

/**
 * 获取所有成绩信息
 * Gets all score information
 * @return Returns a list of all score information
 */
public static void main(String[] args) {
    try {
        // 初始化 ScoreReader
        ScoreReader reader = new ScoreReader();

        // 模拟数据加载
        reader.currentLines = Arrays.asList(
                "1, Alice, Math, 1,89",
                "2, Bob, Math, 2,90",
                "3, Alice, Science, 1, 95",
                "4, Charlie, Math, 1, 80",
                "5, Alice, Math, 1, 88",
                "6, David, Math, 1, 70",
                "7, Bob, Science, 2, 92",
                "8, Alice, Science, 2, 85",
                "9, Charlie, Science, 1, 78"
        );

        // 示例 1：个人成绩查询 - 查找 Alice 的所有 Math 科目成绩，按分数从高到低排列，最多返回 3 条
        System.out.println("示例 1: Alice 的 Math 成绩（按分数从高到低）");
        List<String> personalScores1 = reader.getPersonalScores("Alice", "Math", null, false);
        personalScores1.forEach(System.out::println);

        // 示例 2：个人成绩查询 - 查找 Bob 的所有成绩，按分数从低到高排列，最多返回 5 条
        System.out.println("\n示例 2: Bob 的所有成绩（按分数从低到高）");
        List<String> personalScores2 = reader.getPersonalScores("Bob", null, null, true);
        personalScores2.forEach(System.out::println);

        // 示例 3：个人成绩查询 - 查找 Alice 在 Science 科目、难度 2 下的成绩，按分数从高到低，最多返回 2 条
        System.out.println("\n示例 3: Alice 在 Science（难度 2）的成绩");
        List<String> personalScores3 = reader.getPersonalScores("Alice", "Science", 2, false);
        personalScores3.forEach(System.out::println);

        // 示例 4：公共成绩查询 - 查找 Math 科目的所有成绩，按分数从高到低，最多返回 4 条
        System.out.println("\n示例 4: Math 科目的公共成绩（按分数从高到低）");
        List<String> publicScores1 = reader.getPublicScores("Math", null, false);
        publicScores1.forEach(System.out::println);

        // 示例 5：公共成绩查询 - 查找 Science 科目、难度为 1 的成绩，按分数从低到高，最多返回 3 条
        System.out.println("\n示例 5: Science（难度 1）的公共成绩（按分数从低到高）");
        List<String> publicScores2 = reader.getPublicScores("Science", 1, true);
        publicScores2.forEach(System.out::println);

        // 示例 6：个人成绩查询 - 查找 Charlie 的成绩，按分数从低到高，最多返回所有记录
        System.out.println("\n示例 6: Charlie 的所有成绩（按分数从低到高）");
        List<String> personalScores4 = reader.getPersonalScores("Charlie", null, null, true);
        personalScores4.forEach(System.out::println);

        // 示例 7：公共成绩查询 - 查找 Math 科目、难度为 1 的成绩，按分数从低到高，最多返回 2 条
        System.out.println("\n示例 7: Math（难度 1）的公共成绩（按分数从低到高）");
        List<String> publicScores3 = reader.getPublicScores("Math", 1, true);
        publicScores3.forEach(System.out::println);

        // 示例 8：个人成绩查询 - 查找 David 的成绩，无筛选条件，按分数从高到低，最多返回 1 条
        System.out.println("\n示例 8: David 的成绩（按分数从高到低）");
        List<String> personalScores5 = reader.getPersonalScores("David", null, null, false);
        personalScores5.forEach(System.out::println);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
