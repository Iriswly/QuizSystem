package Score;

import ScoreDB.ScoreReader;

import java.util.List;
import java.util.Scanner;

import Appli.Window;

public class Ranking {

    private final ScoreReader scoreReader;
    Window window = new Window();

    public Ranking(ScoreReader scoreReader) {
        this.scoreReader = scoreReader;
    }

    public void displayRanking() {
        Scanner sc = new Scanner(System.in);

        // 定义话题和难度选项
        String[] topics = {"mathematics", "psychology", "astronomy", "geography"};
        String[] difficulties = {"easy", "medium", "hard", "very_hard"};

        // 用户选择话题
        String topic = null;
        while (topic == null) {
            window.printContent("Select a topic:");
            for (int i = 0; i < topics.length; i++) {
                window.printContent((i + 1) + ". " + topics[i]);
            }
            window.printContent("Enter the topic name or number: ");

            if (sc.hasNextInt()) {
                int topicIndex = sc.nextInt();
                sc.nextLine(); // 清空换行符
                if (topicIndex >= 1 && topicIndex <= topics.length) {
                    topic = topics[topicIndex - 1];
                } else {
                    window.printContent("Invalid number. Please choose a valid option.");
                }
            } else {
                String inputTopic = sc.nextLine();
                for (String t : topics) {
                    if (t.equalsIgnoreCase(inputTopic)) {
                        topic = t;
                        break;
                    }
                }
                if (topic == null) {
                    window.printContent("Invalid input. Please enter a valid topic name or number.");
                }
            }
        }

        // 用户选择难度
        int difficulty = 0;
        while (difficulty == 0) {
            window.printContent("Select a difficulty:");
            for (int i = 0; i < difficulties.length; i++) {
                window.printContent((i + 1) + ". " + difficulties[i]);
            }
            window.printContent("Enter the difficulty name or number: ");

            if (sc.hasNextInt()) {
                int difficultyIndex = sc.nextInt();
                sc.nextLine(); // 清空换行符
                if (difficultyIndex >= 1 && difficultyIndex <= difficulties.length) {
                    difficulty = difficultyIndex;
                } else {
                    window.printContent("Invalid number. Please choose a valid option.");
                }
            } else {
                String inputDifficulty = sc.nextLine().toLowerCase();
                for (int i = 0; i < difficulties.length; i++) {
                    if (difficulties[i].equalsIgnoreCase(inputDifficulty)) {
                        difficulty = i + 1;
                        break;
                    }
                }
                if (difficulty == 0) {
                    window.printContent("Invalid input. Please enter a valid difficulty name or number.");
                }
            }
        }


        // 调用 getPublicScore 获取排行榜数据
        List<String> rankchart = scoreReader.getPublicScores(topic, difficulty, false);

        // 显示排行榜
        window.printContent("");
        window.printContent("       --- Ranking for Topic: " + topic + ", Difficulty: " + difficulties[difficulty - 1] + " ---");

        if (rankchart == null || rankchart.isEmpty()) {
            window.printContent("No scores available for the selected topic and difficulty.");

        } else {

            // 空1行
            window.printContent("");


            String row_head = "          ";
            row_head += String.format("%-20s", "Rank");
            row_head += String.format("%-18s", "User");
            row_head += String.format("%-17s", "Score");
            window.printContent(row_head);

            int rankIndex = 1;
            int lastScore = Integer.parseInt(rankchart.get(0).split(",")[4].trim());

            for (String line : rankchart) {
                // 将line按逗号分割成数组
                String[] fields = line.split(",");

                // 获取第2、3、5个元素
                String name = fields[1].trim(); // 第3个元素（索引为2）
                String score = fields[4].trim(); // 第5个元素（索引为4）
                int _score = Integer.parseInt(score);
                rankIndex = (lastScore == _score) ? rankIndex : rankIndex + 1;
                lastScore = _score;


                // 输出元素
                String row_content = "          ";

                row_content += String.format("%-20s", rankIndex);
                row_content += String.format("%-18s", name);
                row_content += String.format("%-17s", score);
                window.printContent(row_content);
            }
            window.printContent("");
            window.printContent("");
            window.printContent("Enter 'x' to return to Main Menu");
            while (true) {
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("x")) {break;}
            }
        }
    }

    public static void main(String[] args) {
        try {
            // 创建 ScoreReader 实例，读取数据
            ScoreReader scoreReader = new ScoreReader();

            // 创建 Ranking 实例
            Ranking ranking = new Ranking(scoreReader);

            // 调用 displayRanking 方法显
            // 示排名
            ranking.displayRanking();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





