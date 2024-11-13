package quiz;

import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import java.util.Scanner;

public class TopicReader {

    private String topicToSelect; // 用户选择的主题
    private final String[] difficulties = {"EASY", "MEDIUM", "HARD", "VERY_HARD"}; // 难度级别
    private String difficultyToSelect; // 用户选择的难度
    private final String[] topicArray = {"philosophy", "psychology", "astronomy", "geography"}; // 可选主题
    private final int[] index = {1, 2, 3, 4}; // 主题的索引

    public TopicReader() { }

    // 显示主题选择
    public void showTopic() {
        int maxLength = 0;
        for (String s : topicArray) {
            if (s.length() > maxLength) maxLength = s.length();
        }
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) System.out.print("-");
        System.out.println();
        for (int i = 0; i < topicArray.length; i++) {
            System.out.print("|  " + index[i] + "  |  " + topicArray[i]);
            for (int j = topicArray[i].length(); j < maxLength; j++) System.out.print(" ");
            System.out.println("  |");
        }
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) System.out.print("-");
        System.out.println();
    }

    public void selectTopic() {
        System.out.println("Please enter the topic you would like to choose:");
        System.out.println("    Either type in an index (Integer) or type in a topic name (String):");
        Scanner sc = new Scanner(System.in);

        while (topicToSelect == null) {
            if (sc.hasNextInt()) {
                int inputIndex = sc.nextInt();
                // 检查输入的索引是否有效
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    topicToSelect = topicArray[inputIndex - 1]; // 索引从1开始，数组从0开始
                } else {
                    System.out.println("Please enter a valid index or topic name:");
                }
            } else if (sc.hasNext()) {
                String inputTopic = sc.next(); // 读取topic
                // 检查topic是否有效
                for (int i = 0; i < topicArray.length; i++) {
                    String s = topicArray[i];
                    if (s.equals(inputTopic)) {
                        topicToSelect = inputTopic;
                        break;
                    }
                }
                if (topicToSelect == null) {
                    System.out.println("Please enter a valid index or topic name:");
                }
            }
            // 清除无效输入
            sc.nextLine();
        }

        System.out.println("Selected successfully!");
        System.out.println("Your topic is: " + topicToSelect);
        System.out.println();
        System.out.println();
    }

    // 选择难度
    public void selectDifficulty() {
        System.out.println("Please select a difficulty level:");
        for (int i = 0; i < difficulties.length; i++) {
            System.out.println((i + 1) + ". " + difficulties[i]);
        }

        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.print("Enter difficulty index (1-4): ");
            choice = sc.nextInt();
        } while (choice < 1 || choice > difficulties.length);

        difficultyToSelect = difficulties[choice - 1];
    }

    // 获取选择的难度
    public String getDifficultyToSelect() {
        return difficultyToSelect;
    }

    // 获取选择的主题
    public String getTopicToSelect() {
        return this.topicToSelect;
    }

    // 根据选择的主题筛选难度题目
    public String[][] filterQuestionsByDifficulty(String[][] questions, String selectedDifficulty) {
        int count = 0;
        // 遍历一遍问题数组，计算符合条件的题目数量
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(selectedDifficulty)) {
                count++;
            }
        }

        // 创建符合条件的题目数组
        String[][] filteredQuestions = new String[count][12];
        int index = 0;

        // 再次遍历题目，填充符合条件的题目
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(selectedDifficulty)) {
                filteredQuestions[index++] = questions[i];
            }
        }

        return filteredQuestions;
    }

    // 从筛选出的题目中随机选择指定数量的题目
    public  String[][] randomlySelectQuestions(String[][] questions, int numQuestions) {
        if (questions.length == 0) {
            System.out.println("Error: No questions available to select.");
            return new String[0][0]; // 返回空数组
        }

        String[][] finalQuestions = new String[Math.min(numQuestions, questions.length)][];
        boolean[] selectedIndices = new boolean[questions.length];

        for (int i = 0; i < numQuestions; i++) {
            int randomIndex;

            do {
                randomIndex = (int) (Math.random() * questions.length); // 使用 Math.random() 生成随机数
            } while (selectedIndices[randomIndex]);

            finalQuestions[i] = questions[randomIndex];
            selectedIndices[randomIndex] = true; // 标记该题目为已选择
        }

        return finalQuestions;
    }

}
