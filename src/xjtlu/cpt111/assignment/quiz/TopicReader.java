package xjtlu.cpt111.assignment.quiz;

import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import java.util.Scanner;

public class TopicReader {

    private String topicToSelect;

    public TopicReader() {

    }

    private final String[] topicArray = {"philosophy", "psychology", "astronomy", "geography"};
    private final int[] index = {1, 2, 3, 4};

    public void showTopic() {
        // 确定主题的最大长度
        int maxLength = 0;
        for (int i = 0; i < topicArray.length; i++) {
            String s = topicArray[i];
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }

        // 上边框
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < topicArray.length; i++) {
            System.out.print("|  "); // 左侧边框
            System.out.print(index[i]); // 显示索引
            System.out.print("  |  "); // 列分隔

            // 输出主题并填充空格使其对齐
            System.out.print(topicArray[i]);

            // 计算需要填充的空格数量
            int spacesToFill = maxLength - topicArray[i].length();
            for (int j = 0; j < spacesToFill; j++) {
                System.out.print(" "); // 填充空格
            }

            System.out.print("  |");
            System.out.println();
        }

        // 下边框
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) {
            System.out.print("-");
        }
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

    public String getTopicToSelect() {
        return this.topicToSelect;
    }

    public static void main(String[] args) {
        Option option1 = new Option("Mars", false);
        Option option2 = new Option("Venus", false);
        Option option3 = new Option("Mercury", true);
        Option option4 = new Option("Jupiter", false);

        Option[] options = {option1, option2, option3, option4};
        Question question = new Question("astronomy", Difficulty.MEDIUM, "Which planet is the closest to the Sun?", options);

        QuestionProvider questionProvider = new QuestionProvider(question);
        String topicToCheck = "astronomy";

        if (questionProvider.passValidation(topicToCheck)) {
            System.out.println("Valid Question.");
        } else {
            System.out.println("Invalid Question.");
        }

        TopicReader topicReader = new TopicReader();
        topicReader.showTopic();
        topicReader.selectTopic();

        String[][] selectedQuestions = questionProvider.selectedQuestions(topicReader);
        if (selectedQuestions != null) {
            for (int i = 0; i < selectedQuestions.length; i++) {
                System.out.println("Index: " + selectedQuestions[i][0]);
                System.out.println("Topic: " + selectedQuestions[i][1]);
                System.out.println("Difficulty: " + selectedQuestions[i][2]);
                System.out.println("Question: " + selectedQuestions[i][3]);
                System.out.println("Options and Correctness:");

                for (int j = 4; j < selectedQuestions[i].length; j += 2) {
                    System.out.printf("%-15s %s\n", selectedQuestions[i][j], selectedQuestions[i][j + 1]);
                }
            }
        } else {
            System.out.println("No questions found for the selected topic.");
        }

        /* 返回的数组如下：
         索引  0    1            2         3                                          4       5        6        7       8           9       10         11
         值   "0"  "astronomy"  "MEDIUM"  "Which planet is the closest to the Sun?"  "Mars"  "false"  "Venus"  "false"  "Mercury"  "true"  "Jupiter"  false
        */
    }
}
