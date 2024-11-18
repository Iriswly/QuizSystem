package Appli;

import Authenticator.Register;
import Score.Ranking;
import Score.ScoreRecord;
import ScoreDB.ScoreReader;
import User.UserInfo;
import quiz.QuestionProvider;
import quiz.TopicReader;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import Score.ScoreProvider;

import java.util.Scanner;

public class Menu {
    String selectedOption = "";
    Window window = new Window();
    private UserInfo user;

    public Menu(UserInfo newUser) {
        user = newUser;
    }

    public Menu() {

    }

    public void unloggedMenu() {
        window.top();
        try {
            Register register = new Register(user);
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
        String[] interactiveOptions = {"Quiz", "Insert Question", "Ranking", "Account Management", "Logout"};
        int[] index = {1, 2, 3, 4, 5};

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

            // 在特定条件下插入狗头的中间部分
            if (i == 0) { // 在打印第二个选项后插入狗头的第一行
                line += "                    " +"       __/ \\   \\/    / \\__       ";
            } else if (i == 1) { // 在打印第三个选项后插入狗头的第二行
                line += "                    " + "   __/@    )   ||    (    @\\__   ";
            } else if (i == 2) { // 在打印第四个选项后插入狗头的第三行
                line += "                    " + " O         \\  _||_  /         O  ";
            } else if (i == 3) {
                line += "                    " + "  \\_____)   \\/ ||  \\/   (_____/  ";
            } else if (i == 4) {
                line += "                    " + "    U  \\_____\\ /\\ /_____/   U  ";
            }
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

    public void quizMenu() {
        // 做题部分
        QuestionProvider questionProvider = new QuestionProvider();
        ScoreRecord scoreRecord = null;
        try {
            scoreRecord = new ScoreRecord(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        do {
            window.top();

            TopicReader topicReader = new TopicReader();
            topicReader.showTopic();
            topicReader.selectTopic();
            // 如果按X，返回主菜单
            if (topicReader.getQuitInstantlyForTopic()) {
                return;
            }
            topicReader.selectDifficulty();
            // 如果按X，返回主菜单
            if (topicReader.getQuitInstantlyForDifficulty()) {
                return;
            }

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
            String[][] veryHardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "VERY_HARD");
            window.printContent("Number of veryHard questions: " + (veryHardQuestions != null ? veryHardQuestions.length : 0));

            // 根据用户选择的难度等级随机选择题目
            String selectedDifficulty = topicReader.getDifficultyToSelect();
            String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 20, selectedDifficulty);
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

        String topic;
        String questionText;
        Option[] options = new Option[4];    // 固定为四个选项

        // 获取主题
        do {
            window.printContent("Enter the topic of the question: (or 'x' to exit)");
            topic = scanner.nextLine().trim();
            if (topic.equalsIgnoreCase("X")) {
                window.printContent("Exiting quiz and returning to main menu...");
                return; // 结束当前方法，返回主菜单
            }
            // 避免 IllegalArgumentException
            if (topic.isEmpty()) {
                window.printContent("Topic cannot be empty. Please try again.");
                }
        } while (topic.isEmpty());


        // 获取题干
        do {
            window.printContent("Enter the question: (or 'x' to exit)");
            questionText = scanner.nextLine().trim();
            if (questionText.equalsIgnoreCase("X")) {
                window.printContent("Exiting quiz and returning to main menu...");
                return; // 结束当前方法，返回主菜单
            }
            if (questionText.isEmpty()) {
                window.printContent("Question cannot be empty. Please try again.");
            }
        } while (questionText.isEmpty());


        // 获取每个选项及其正确性
        for (int i = 0; i < options.length; i++) {
            String optionText;

            do {
                window.printContent("Enter option " + (i + 1) + ": (or 'x' to exit)");
                optionText = scanner.nextLine().trim();
                if (optionText.equalsIgnoreCase("X")) {
                    window.printContent("Exiting quiz and returning to main menu...");
                    return; // 结束当前方法，返回主菜单
                }
                if (optionText.isEmpty()) {
                    window.printContent("Option " + (i + 1) + " cannot be empty. Please try again.");
                }
            } while (optionText.isEmpty());

            // 提问该选项是否为正确答案
            String correctnessInput;

            do {
                window.printContent("Is this option " + (i + 1) + " correct? (true/false): (or 'x' to exit)");
                correctnessInput = scanner.nextLine().trim();

                if (correctnessInput.equalsIgnoreCase("X")) {
                    window.printContent("Exiting quiz and returning to main menu...");
                    return; // 结束当前方法，返回主菜单
                }
                if (!correctnessInput.equalsIgnoreCase("true") && !correctnessInput.equalsIgnoreCase("false")) {
                    window.printContent("Invalid input for correctness. Please enter true or false.");
                }
            } while (!correctnessInput.equalsIgnoreCase("true") && !correctnessInput.equalsIgnoreCase("false"));

            boolean isCorrect = Boolean.parseBoolean(correctnessInput);

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

    public void rankingMenu() {
        window.top();
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

        window.bottom();
    }

    public void logOutMenu () {
        window.top();
        window.printContent("Are you sure to exit the quiz system? (or 'x' to return to the main menu)");
        window.printContent("Press 'z' to confirm your quit");
        // 空3行
        for (int i = 0; i < 3; i++) {
            window.printContent("");
        }
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        do {
            if (input.equalsIgnoreCase("x")) {
                window.bottom();
                return;
            } else if (input.equalsIgnoreCase("z")) {
                window.bottom();
                System.exit(0);
            }
        } while (true);

    }

    public void initializeMenu() {
        window.top();
        window.printContent("Welcome to the Quiz System!");
        window.printContent("        NOWLOADING...");
        window.printContent("");
        window.printContent("");
        window.printContent("PRESS ANY BUTTON");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        window.bottom();
    }

    public void accountManagementMenu() {
        // show
        window.top();
        String[] interactiveOptions = {"Change Nickname", "Change Password", "Privacy Setting","Cancel Account"};
        int[] index = {1, 2, 3, 4};
        String choiceToSelect = null;

        // 确定主题的最大长度
        int maxLength = 0;
        for (String choice : interactiveOptions) {
            if (choice.length() > maxLength) {
                maxLength = choice.length();
            }
        }

        // 上边框
        String upperBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            upperBorder += "-";
        }
        upperBorder += "------";
        window.printContent(upperBorder); // 输出上边框

        for (int i = 0; i < interactiveOptions.length; i++) {
            String line = "      |  " + index[i] + "  |  " + interactiveOptions[i];

            // 输出填充空格使其对齐
            int spacesToFill = maxLength - interactiveOptions[i].length();
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


        // select
        window.printContent("Please enter how you want to deal with your account: (or 'x' to return to Main Menu)");
        window.printContent("    Either type in an index (Integer) or type in a function name (String):");
        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("X")) {
                window.printContent("Exiting account management and returning to main menu...");
                window.bottom();
                return;
            }

            // 检查输入是否为数字
            boolean isIndex = true;

            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    isIndex = false;
                    break;
                }
            }

            if (isIndex) {
                int inputIndex = Integer.parseInt(input);
                // 检查输入的索引是否有效
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    choiceToSelect = interactiveOptions[inputIndex - 1];
                }
            } else {
                // 检查输入是否为有效的函数名称
                for (String option : interactiveOptions) {
                    if (option.equalsIgnoreCase(input)) {
                        choiceToSelect = option;
                        break;
                    }
                }
            }

            // 检查是否选择了有效的选项
            if (choiceToSelect != null) {
                window.printContent("Selected successfully!");
                window.printContent("You have selected: " + choiceToSelect);
                window.printContent("");

                switch (choiceToSelect) {
                    case "Change Nickname":
                        changeNicknameMenu();
                        break;
                    case "Change Password":
                        changePasswordMenu();
                        break;
                    case "Privacy Setting":
                        privacySettingMenu();
                        break;
                    case "Cancel Account":
                        cancelAccountMenu();
                        break;
                    default:
                        window.printContent("Invalid option selected. Please try again.");
                        break;
                }
                break; // 跳出循环
            } else {
                window.printContent("Please enter a valid index or a function name:");

            }
        }

    }

    public void changeNicknameMenu() {
        // 确认密码即可
    }

    public void changePasswordMenu() {
        // 确认密码，真名
        // 输入新密码（两次）
    }

    public void privacySettingMenu() {
        // 确认密码
        // 是否在排行榜上显示个人昵称
    }

    public void cancelAccountMenu() {
        // 确认密码，真名
        // 注销账号
    }


    public void dog () {
        System.out.println("       __/ \\   \\/    / \\__       ");
        System.out.println("   __/@    )   ||    (    @\\__   ");
        System.out.println(" O         \\  _||_  /         O  ");
        System.out.println("  \\_____)   \\/ ||  \\/   (_____/  ");
        System.out.println("    U  \\_____\\ /\\ /_____/   U  ");
    }

}
