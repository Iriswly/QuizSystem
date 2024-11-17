package quiz;

import User.UserInfo;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import Appli.Menu;

import java.util.Scanner;
import Appli.Window;

public class TopicReader {
    private Menu menu = new Menu();

    private String topicToSelect;   // 用户选择的主题
    private final String[] difficulties = {"EASY", "MEDIUM", "HARD", "VERY_HARD"}; // 难度级别
    private String difficultyToSelect;   // 用户选择的难度
    private final String[] topicArray = {"mathematics", "psychology", "astronomy", "geography"};
    private final int[] index = {1, 2, 3, 4};
    boolean quitInstantlyTopic = false;
    boolean quitInstantlyDifficulty = false;

    public TopicReader() {
        // 初始化构造函数
    }

    // 显示主题选择
    public void showTopic() {
        Window window = new Window();
        // 确定主题的最大长度
        int maxLength = 0;
        for (String topic : topicArray) {
            if (topic.length() > maxLength) {
                maxLength = topic.length();
            }
        }

        // 上边框
        String upperBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            upperBorder += "-";
        }
        upperBorder += "------";
        window.printContent(upperBorder); // 输出上边框

        for (int i = 0; i < topicArray.length; i++) {
            String line = "      |  " + index[i] + "  |  " + topicArray[i];

            // 输出填充空格使其对齐
            int spacesToFill = maxLength - topicArray[i].length();
            for (int j = 0; j < spacesToFill; j++) {
                line += " "; // 填充空格
            }
            line += "        |"; // 添加结尾的边框
            window.printContent(line);
        }

        // 下边框
        String lowerBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            lowerBorder += "-";
        }
        lowerBorder += "------";
        window.printContent(lowerBorder); // 一次性输出下边框
        window.printContent("");
    }

    // 选择主题
    public void selectTopic() {
        Window window = new Window();
        window.printContent("Please enter the topic you would like to choose: (or 'x' to exit)");
        window.printContent("    Either type in an index (Integer) or type in a topic name (String):");
        Scanner sc = new Scanner(System.in);

        while (topicToSelect == null) {
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("X")) {
                quitInstantlyTopic = true;
                window.bottom();
                return;
            }

            // 检查输入是否是有效的整数索引
            int inputIndex = -1;
            boolean isIndex = true;

            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    isIndex = false;
                    break;
                }
            }

            if (isIndex) {
                inputIndex = Integer.parseInt(input);
                // 检查输入的索引是否有效
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    topicToSelect = topicArray[inputIndex - 1]; // 索引从1开始，数组从0开始
                } else {
                    window.printContent("Please enter a valid index or topic name:");
                }
            } else {
                // 检查topic是否有效
                boolean foundTopic = false;
                for (String topic : topicArray) {
                    if (topic.equalsIgnoreCase(input)) {
                        topicToSelect = input;
                        foundTopic = true;
                        break;
                    }
                }
                if (!foundTopic) {
                    window.printContent("Please enter a valid index or topic name:");
                }
            }

        }

        window.printContent("Selected successfully!");
        window.printContent("Your topic is: " + topicToSelect);
        window.printContent("");
    }

    public boolean getQuitInstantlyForTopic() {
        return quitInstantlyTopic;
    }

    // 选择难度
    public void selectDifficulty() {
        Window window = new Window();
        window.printContent("Please select a difficulty level:");
        for (int i = 0; i < difficulties.length; i++) {
            window.printContent((i + 1) + ". " + difficulties[i]);
        }

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (difficultyToSelect == null) {
            window.printContent("Enter difficulty index (1-4): (or 'x' to return to Main Menu)");

            String input = sc.nextLine();

            if (input.equalsIgnoreCase("X")) {
                quitInstantlyDifficulty = true;
                window.bottom();
                return;
            }

            boolean isIndex = true;
            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    isIndex = false;
                    break;
                }
            }

            if (isIndex) {
                choice = Integer.parseInt(input); // 如果是数字，转为整数

                // 检查输入的数字是否在有效范围内
                if (choice >= 1 && choice <= difficulties.length) {
                    difficultyToSelect = difficulties[choice - 1]; // 设置选定的难度
                    window.printContent("Selected successfully!");
                    window.printContent("Your difficulty is: " + difficultyToSelect);
                    window.printContent("");

                } else {
                    window.printContent("Invalid choice. Please enter a number between 1 and " + difficulties.length + ".");
                }
            } else {
                window.printContent("Invalid input. Please enter a valid number.");
            }
        }
    }

    public boolean getQuitInstantlyForDifficulty() {
        return quitInstantlyDifficulty;
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
    public static String[][] QuestionsByDifficulty(String[][] questions, String Difficulty) {
        // 确保问题数组非空
        if (questions == null || questions.length == 0) {
            return new String[0][0]; // 返回空数组
        }

        int count = 0;
        // 遍历一遍问题数组，计算符合条件的题目数量
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(Difficulty)) {
                count++;
            }
        }

        // 创建符合条件的题目数组
        String[][] Questionsbydifficulty = new String[count][];
        int index = 0;

        // 再次遍历题目，填充符合条件的题目
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(Difficulty)) {
                Questionsbydifficulty[index++] = questions[i];
            }
        }

        return Questionsbydifficulty;
    }
    // 从某个难度的题库中随机选择题目
    private static String[][] selectRandomlyFromCategory(String[][] questions, int numQuestions, String[][] selectedQuestions, int startIndex) {
        for (int i = 0; i < numQuestions; i++) {
            if (questions.length == 0) break;  // 防止题目数不足

            // 使用 Math.random() 来生成随机数
            int randomIndex = (int) (Math.random() * questions.length);

            // 确保选择的题目没有重复
            while (questions[randomIndex] == null) {
                randomIndex = (int) (Math.random() * questions.length);
            }

            selectedQuestions[startIndex + i] = questions[randomIndex];
            questions[randomIndex] = null; // 标记该题目已选择，防止重复
        }

        return selectedQuestions;
    }

    // 根据用户选择的难度，按比例随机选择题目
    public static String[][] QuizQuestion(String[][] questions, int numQuestions, String difficulty) {
        // 根据难度决定比例
        double easyPercentage = 0, mediumPercentage = 0, hardPercentage = 0, veryHardPercentage = 0;

        switch (difficulty) {
            case "EASY":
                easyPercentage = 0.6;
                mediumPercentage = 0.3;
                hardPercentage = 0.1;
                break;
            case "MEDIUM":
                easyPercentage = 0.4;
                mediumPercentage = 0.4;
                hardPercentage = 0.2;
                break;
            case "HARD":
                easyPercentage = 0.3;
                mediumPercentage = 0.3;
                hardPercentage = 0.4;
                break;
            case "VERY_HARD":
                mediumPercentage = 0.3;
                hardPercentage = 0.3;
                veryHardPercentage = 0.4;
                break;
        }

        // 计算每个难度需要选择的题目数量
        int easyCount = (int) (numQuestions * easyPercentage);
        int mediumCount = (int) (numQuestions * mediumPercentage);
        int hardCount = (int) (numQuestions * hardPercentage);
        int veryHardCount = (int) (numQuestions * veryHardPercentage);

        // 筛选每个难度的题目
        String[][] easyQuestions = QuestionsByDifficulty(questions, "EASY");
        String[][] mediumQuestions = QuestionsByDifficulty(questions, "MEDIUM");
        String[][] hardQuestions = QuestionsByDifficulty(questions, "HARD");
        String[][] veryHardQuestions = QuestionsByDifficulty(questions, "VERY_HARD");

        // 按比例从每个难度中选择题目
        String[][] quizquestions = new String[numQuestions][12];
        int index = 0;
        quizquestions  = selectRandomlyFromCategory(easyQuestions, easyCount, quizquestions, index);
        index += easyCount;
        quizquestions  = selectRandomlyFromCategory(mediumQuestions, mediumCount, quizquestions, index);
        index += mediumCount;
        quizquestions  = selectRandomlyFromCategory(hardQuestions, hardCount, quizquestions, index);
        index += hardCount;
        quizquestions  = selectRandomlyFromCategory(veryHardQuestions, veryHardCount, quizquestions, index);

        return quizquestions ;
    }

    public static void main(String[] args) {
        // 创建 QuestionProvider 实例
        QuestionProvider questionProvider = new QuestionProvider();

        // 显示主题选择
        TopicReader topicReader = new TopicReader();
        topicReader.showTopic();
        topicReader.selectTopic();
        topicReader.selectDifficulty();

        // 根据选择的主题获取问题
        String selectedTopic = topicReader.getTopicToSelect();
        String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);

        System.out.println("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

        if (selectedQuestions != null && selectedQuestions.length > 0) {
            // 显示所有问题及其选项
            for (int row = 0; row < selectedQuestions.length; row++) {
                if (selectedQuestions[row] != null) { // 检查非空行
                    System.out.println("Question " + (row + 1) + ": " + selectedQuestions[row][3]); // 显示问题内容
                    for (int col = 0; col < selectedQuestions[row].length; col++) {
                        System.out.println("selectedQuestions[" + row + "][" + col + "]: " + selectedQuestions[row][col]);
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("No questions found for the selected topic.");
        }
    }
}
