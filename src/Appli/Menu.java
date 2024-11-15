package Appli;

import Authenticator.Register;
import Score.ScoreRecord;
import quiz.QuestionProvider;
import quiz.TopicReader;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;

import java.util.Scanner;

public class Menu {
    String selectedOption = "";
    Window window = new Window();

    public Menu() {}

    public void unloggedMenu() {
        window.top();
        try {
            Register register = new Register();
            register.displayMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 清空之前的选项，准备重新选择
        selectedOption = ""; // 清空选择
    }

    public void mainMenu() {
        // 展示
        window.top();
        String[] interactiveOptions = {"Quiz", "Insert Question", "Ranking", "Logout"};
        int[] index = {1, 2, 3, 4};

        // 确定主题的最大长度
        int maxLength = 0;
        for (String option : interactiveOptions) {
            if (option.length() > maxLength) {
                maxLength = option.length();
            }
        }

        // 上边框
        String upperBorder = "      ------------" + "-".repeat(maxLength) + "------";
        window.printContent(upperBorder); // 输出上边框

        for (int i = 0; i < interactiveOptions.length; i++) {
            String line = "      |  " + index[i] + "  |  " + interactiveOptions[i];
            int spacesToFill = maxLength - interactiveOptions[i].length();
            line += " ".repeat(spacesToFill) + "        |"; // 填充空格并添加结尾的边框
            window.printContent(line);
        }

        // 下边框
        String lowerBorder = "      ------------" + "-".repeat(maxLength) + "------";
        window.printContent(lowerBorder); // 一次性输出下边框
        window.printContent("");

        // 选择
        window.printContent("Waiting for your choice: ...");
        window.printContent("    Either type in an index (Integer) or an option name (String):");
        Scanner sc = new Scanner(System.in);

        // 清空上一次选择
        selectedOption = "";

        while (true) {
            if (sc.hasNextInt()) {
                int inputIndex = sc.nextInt();
                sc.nextLine(); // 清空换行符
                // 检查输入的索引是否有效
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    selectedOption = interactiveOptions[inputIndex - 1]; // 索引从1开始，数组从0开始
                    break;
                } else {
                    window.printContent("Please enter a valid index or an option name:");
                }
            } else if (sc.hasNext()) {
                String inputTopic = sc.next(); // 读取topic
                sc.nextLine(); // 清空换行符
                // 检查topic是否有效
                for (String s : interactiveOptions) {
                    if (s.equalsIgnoreCase(inputTopic)) {
                        selectedOption = inputTopic;
                        break;
                    }
                }
                if (!selectedOption.isEmpty()) {
                    break;
                } else {
                    window.printContent("Please enter a valid index or an option name:");
                }
            }
            // 清除无效输入
            sc.nextLine();
        }
        window.printContent("Selected successfully!");
        window.printContent("Your option is: " + selectedOption);
        window.printContent("");
        window.bottom();
    }

    public String getSelectedOption() {
        return selectedOption; // 返回当前选项
    }

    public void QuizMenu() {
        // 做题部分
        QuestionProvider questionProvider = new QuestionProvider();
        ScoreRecord scoreRecord = new ScoreRecord();

        do {
            window.top();

            TopicReader topicReader = new TopicReader();
            topicReader.showTopic();
            topicReader.selectTopic();
            topicReader.selectDifficulty();

            // 获取选择主题的题目
            String selectedTopic = topicReader.getTopicToSelect();
            window.printContent("Selected Topic: " + selectedTopic);
            String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);
            window.printContent("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

            // 按题目难度分类该主题题目
            String[][] easyQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "EASY");
            window.printContent("Number of easy questions: " + (easyQuestions != null ? easyQuestions.length : 0));
            String[][] mediumQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "MEDIUM");
            window.printContent("Number of medium questions: " + (mediumQuestions != null ? mediumQuestions.length : 0));
            String[][] hardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "HARD");
            window.printContent("Number of hard questions: " + (hardQuestions != null ? hardQuestions.length : 0));
            String[][] veryhardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "VERY_HARD");
            window.printContent("Number of veryhard questions: " + (veryhardQuestions != null ? veryhardQuestions.length : 0));

            // 根据用户选择的难度等级随机选择题目
            String selectedDifficulty = topicReader.getDifficultyToSelect();
            String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 10, selectedDifficulty);
            window.printContent("Number of quiz questions: " + (quizQuestions != null ? quizQuestions.length : 0));

            window.printContent("");
            // 创建 ScoreRecord 实例并开始答题
            scoreRecord.displayQuestionsAndScore(quizQuestions, topicReader);

        } while (scoreRecord.askContinue());

        window.bottom();
    }

    public void insertQuestionMenu() {
        Scanner scanner = new Scanner(System.in);
        QuestionProvider questionProvider = new QuestionProvider();

        // 获取主题
        window.printContent("Enter the topic of the question:");
        String topic = scanner.nextLine().trim();

        // 获取题干
        window.printContent("Enter the question:");
        String questionText = scanner.nextLine().trim();

        // 初始化选项数组
        Option[] options = new Option[4]; // 固定4个选项

        // 获取每个选项及其正确性
        for (int i = 0; i < options.length; i++) {
            window.printContent("Enter option " + (i + 1) + ":");
            String optionText = scanner.nextLine().trim();

            // 提问该选项是否为正确答案
            window.printContent("Is this option " + (i + 1) + " correct? (true/false):");
            String correctnessInput = scanner.nextLine().trim();

            boolean isCorrect;
            try {
                isCorrect = Boolean.parseBoolean(correctnessInput);
            } catch (Exception e) {
                window.printContent("Invalid input for correctness. Defaulting to false.");
                isCorrect = false;
            }

            // 创建选项并存入数组
            options[i] = new Option(optionText, isCorrect);
        }

        // 创建问题
        Question newQuestion = new Question(topic, Difficulty.MEDIUM, questionText, options);

        // 插入问题
        window.printContent("Inserting question...");
        questionProvider.addQuestion(newQuestion);
        window.printContent("Your question has been inserted successfully!");
    }

    public void clearSelectedOption() {
        selectedOption = ""; // 清空选择状态
    }

    public void rankingMenu() {
        // 排名菜单逻辑
    }
}
