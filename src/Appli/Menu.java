package Appli;

import Authenticator.Register;
import Score.ScoreRecord;
import User.UserInfo;
import quiz.QuestionProvider;
import quiz.TopicReader;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import Score.ScoreProvider;
import ScoreDB.ScoreEditor;

import java.util.Scanner;

public class Menu {
    String selectedOption = "";
    Window window = new Window();
    private UserInfo user;
    private ScoreEditor scoreEditor;

    public Menu(UserInfo newUser) {
        user = newUser;
        try {
            scoreEditor = new ScoreEditor();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ScoreProvider scoreProvider = new ScoreProvider();
        String[][] userScores = scoreProvider.getAllUserScores();
        for (int i = 0; i < userScores.length; i++) {
            String row = "";
            for (int j = 0; j < userScores[i].length; j++) {
                if (j == 0 || j == 2) {
                    continue;     // 跳过 realName 和 password
                }
                // 固定 nickname 长度为15
                if (j == 1) {
                    row += String.format("%-15s", userScores[i][j]);
                    row += ":       ";
                } else {        // 其他列宽固定为12
                    row += String.format("%-12s", userScores[i][j]);
                }

            }
            window.printContent(row);
        }
        // 空行
        window.printContent("");
        window.printContent("Enter 'x' to return to the main menu:");
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            input = sc.nextLine();
        } while (!input.equalsIgnoreCase("x"));

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
        String[] interactiveOptions = {"Change Nickname", "Change Password", "Cancel Account"};
        int[] index = {1, 2, 3};
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
                        window.bottom();
                        changeNicknameMenu();
                        accountManagementMenu();
                        break;
                    case "Change Password":
                        window.bottom();
                        changePasswordMenu();
                        accountManagementMenu();
                        break;
                    case "Cancel Account":
                        window.bottom();
                        cancelAccountMenu();
                        unloggedMenu();
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
        try {
            UserInfo userInfo = new UserInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String currentUser = user.getNickname();
        String currentPassword = user.getPassword();
        boolean quitInstantly = false;

        window.top();
        Scanner sc = new Scanner(System.in);
        window.printContent("Please enter your original password: (or 'x' to return to Account Management)");

        while (true) {
            String inputPassword = sc.nextLine();

            if (inputPassword.equalsIgnoreCase("X")) {
                window.bottom();
                return;
            }

            if (inputPassword.equals(currentPassword)) {
                window.printContent("Password confirmation passed.");
                break; // 密码正确，退出当前循环
            } else {
                window.printContent("Wrong password. Please try again.");
            }
        }

        // 提示用户输入新昵称
        while (true) {
            window.printContent("Please enter your new nickname: (or 'x' to return to Account Management)");
            String newNickname1 = sc.nextLine();

            if (newNickname1.equalsIgnoreCase("X")) {
                window.bottom();
                return;
            }

            window.printContent("Please re-enter your new nickname: (or 'x' to return to Account Management)");
            String newNickname2 = sc.nextLine();

            if (newNickname2.equalsIgnoreCase("X")) {
                window.bottom();
                return;
            }

            if (newNickname1.equals(newNickname2)) {
                user.updateNickname(newNickname1);       // 更新昵称
                window.printContent("Nickname updated successfully!");
                window.printContent("PRESS ANY BUTTON to return to Account Management.");
                sc.nextLine();
                window.bottom();
                return;

            } else {
                window.printContent("Nicknames do not match, please try again.");
            }
        }
    }


    public void changePasswordMenu() {
        try {
            UserInfo userInfo = new UserInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String currentUser = user.getNickname();
        String currentPassword = user.getPassword();
        boolean quitInstantly = false;

        window.top();
        Scanner sc = new Scanner(System.in);
        window.printContent("Please enter your original password: (or 'x' to return to Account Management)");

        while (true) {
            String inputPassword = sc.nextLine();

            if (inputPassword.equalsIgnoreCase("X")) {
                window.bottom();
                return;
            }

            if (inputPassword.equals(currentPassword)) {
                window.printContent("Password confirmation passed.");
                break; // 密码正确，退出当前循环
            } else {
                window.printContent("Wrong password. Please try again.");
            }
        }

        // 提示用户输入新昵称
        while (true) {
            window.printContent("Please enter your new password: (or 'x' to return to Account Management)");
            String newPassword1 = sc.nextLine();

            if (newPassword1.equalsIgnoreCase("X")) {
                window.bottom();
                return;
            }

            window.printContent("Please re-enter your new password: (or 'x' to return to Account Management)");
            String newPassword2 = sc.nextLine();

            if (newPassword2.equalsIgnoreCase("X")) {
                window.bottom();
                return;
            }

            if (newPassword1.equals(newPassword2)) {
                user.updatePassword(newPassword1);       // 更新密码
                window.printContent("Password updated successfully!");
                window.printContent("PRESS ANY BUTTON to return to Account Management.");
                sc.nextLine();
                window.bottom();
                return;

            } else {
                window.printContent("Passwords do not match, please try again.");
            }
        }
    }

    public void cancelAccountMenu() {
        Window window = new Window();
        Scanner scanner = new Scanner(System.in);

        window.printContent("Enter your real name: (or enter 'x' to exit)");
        String realName = scanner.nextLine().trim();
        if (realName.equalsIgnoreCase("x")) {
            window.printContent("Exiting account deletion...");
            return;
        }

        window.printContent("Enter your password: (or enter 'x' to exit)");
        String password = scanner.nextLine().trim();
        if (password.equalsIgnoreCase("x")) {
            window.printContent("Exiting account deletion...");
            return;
        }
        // 注销账号
        if (user.Login(realName, password)) {
            if (user.deleteAccount(realName)) {
                window.printContent("Account deleted successfully.");
                user.deleteAccount(user.getNickname());
                // TODO
            } else {
                window.printContent("Account deletion failed.");
            }
        } else {
            window.printContent("Invalid credentials. Account deletion failed.");
        }
        window.bottom();
    }


    public void dog () {
        System.out.println("       __/ \\   \\/    / \\__       ");
        System.out.println("   __/@    )   ||    (    @\\__   ");
        System.out.println(" O         \\  _||_  /         O  ");
        System.out.println("  \\_____)   \\/ ||  \\/   (_____/  ");
        System.out.println("    U  \\_____\\ /\\ /_____/   U  ");
    }

}
