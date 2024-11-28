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
import ScoreDB.ScoreEditor;
import java.util.ArrayList;

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

    // the menu in unlogged status
    public void unloggedMenu() {
        window.top();
        try {
            Register register = new Register(user);
            register.displayMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // clear options
        selectedOption = "";
    }

    public void mainMenu() {
        // show
        window.top();
        String[] interactiveOptions = {"Quiz", "Insert Question", "Ranking", "Account Management", "Logout"};
        int[] index = {1, 2, 3, 4, 5};

        // determine max length of options
        int maxLength = 0;
        for (String option : interactiveOptions) {
            if (option.length() > maxLength) {
                maxLength = option.length();
            }
        }

        // upper board
        String upperBorder = "      ------------" + "-".repeat(maxLength) + "------";
        window.printContent(upperBorder);

        for (int i = 0; i < interactiveOptions.length; i++) {
            String line = "      |  " + index[i] + "  |  " + interactiveOptions[i];
            int spacesToFill = maxLength - interactiveOptions[i].length();
            line += " ".repeat(spacesToFill) + "        |";

            // print the dog image
            if (i == 0) {
                line += "                    " +"       __/ \\   \\/    / \\__       ";
            } else if (i == 1) {
                line += "                    " + "   __/@    )   ||    (    @\\__   ";
            } else if (i == 2) {
                line += "                    " + " O         \\  _||_  /         O  ";
            } else if (i == 3) {
                line += "                    " + "  \\_____)   \\/ ||  \\/   (_____/  ";
            } else if (i == 4) {
                line += "                    " + "    U  \\_____\\ /\\ /_____/   U  ";
            }
            window.printContent(line);
        }

        // lower board
        String lowerBorder = "      ------------" + "-".repeat(maxLength) + "------";
        window.printContent(lowerBorder);
        window.printContent("");

        // choose specific option
        window.printContent("Waiting for your choice: ...");
        window.printContent("    Either type in an index (Integer) or an option name (String):");
        Scanner sc = new Scanner(System.in);

        // clear options
        selectedOption = "";

        while (true) {
            if (sc.hasNextInt()) {
                int inputIndex = sc.nextInt();
                sc.nextLine();
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    selectedOption = interactiveOptions[inputIndex - 1];
                    break;
                } else {
                    window.printContent("Please enter a valid index or an option name:");
                }
            } else if (sc.hasNext()) {
                String inputTopic = sc.next();
                sc.nextLine();
                // check whether the topic is valid
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

    // take quiz
    public void quizMenu() {
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

            if (topicReader.getQuitInstantlyForTopic()) {
                return;
            }
            topicReader.selectDifficulty();

            if (topicReader.getQuitInstantlyForDifficulty()) {
                return;
            }

            // retrieve questions on the chosen topic
            String selectedTopic = topicReader.getTopicToSelect();
            window.printContent("Selected Topic: " + selectedTopic);
            String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);
            window.printContent("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

            // divide questions according to different difficulty levels
            String[][] easyQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "EASY");
            window.printContent("Number of easy questions: " + (easyQuestions != null ? easyQuestions.length : 0));
            String[][] mediumQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "MEDIUM");
            window.printContent("Number of medium questions: " + (mediumQuestions != null ? mediumQuestions.length : 0));
            String[][] hardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "HARD");
            window.printContent("Number of hard questions: " + (hardQuestions != null ? hardQuestions.length : 0));
            String[][] veryHardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "VERY_HARD");
            window.printContent("Number of veryHard questions: " + (veryHardQuestions != null ? veryHardQuestions.length : 0));

            // retrieve the questions of the topic in specific difficulty level
            String selectedDifficulty = topicReader.getDifficultyToSelect();
            String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 20, selectedDifficulty);
            window.printContent("Number of quiz questions: " + (quizQuestions != null ? quizQuestions.length : 0));

            window.printContent("");

            scoreRecord.displayQuestionsAndScore(quizQuestions, topicReader);

        } while (scoreRecord.askContinue());
        window.bottom();

    }

    public void insertQuestionMenu() {
        Scanner scanner = new Scanner(System.in);
        QuestionProvider questionProvider = new QuestionProvider();

        String topic;
        String questionText;
        Option[] options = new Option[4];

        // get the topic
        do {
            window.printContent("Enter the topic of the question: (or 'x' to exit)");
            topic = scanner.nextLine().trim();
            if (topic.equalsIgnoreCase("X")) {
                window.printContent("Exiting quiz and returning to main menu...");
                return;
            }
            // avoid IllegalArgumentException
            if (topic.isEmpty()) {
                window.printContent("Topic cannot be empty. Please try again.");
                }
        } while (topic.isEmpty());


        // get the question statement
        do {
            window.printContent("Enter the question: (or 'x' to exit)");
            questionText = scanner.nextLine().trim();
            if (questionText.equalsIgnoreCase("X")) {
                window.printContent("Exiting quiz and returning to main menu...");
                return;
            }
            if (questionText.isEmpty()) {
                window.printContent("Question cannot be empty. Please try again.");
            }
        } while (questionText.isEmpty());


        // get each answer and correctness
        for (int i = 0; i < options.length; i++) {
            String optionText;

            do {
                window.printContent("Enter option " + (i + 1) + ": (or 'x' to exit)");
                optionText = scanner.nextLine().trim();
                if (optionText.equalsIgnoreCase("X")) {
                    window.printContent("Exiting quiz and returning to main menu...");
                    return;
                }
                if (optionText.isEmpty()) {
                    window.printContent("Option " + (i + 1) + " cannot be empty. Please try again.");
                }
            } while (optionText.isEmpty());

            // ask for correctness
            String correctnessInput;

            do {
                window.printContent("Is this option " + (i + 1) + " correct? (true/false): (or 'x' to exit)");
                correctnessInput = scanner.nextLine().trim();

                if (correctnessInput.equalsIgnoreCase("X")) {
                    window.printContent("Exiting quiz and returning to main menu...");
                    return;
                }
                if (!correctnessInput.equalsIgnoreCase("true") && !correctnessInput.equalsIgnoreCase("false")) {
                    window.printContent("Invalid input for correctness. Please enter true or false.");
                }
            } while (!correctnessInput.equalsIgnoreCase("true") && !correctnessInput.equalsIgnoreCase("false"));

            boolean isCorrect = Boolean.parseBoolean(correctnessInput);

            // create array
            options[i] = new Option(optionText, isCorrect);
        }

        // create question
        Question newQuestion = new Question(topic, Difficulty.MEDIUM, questionText, options);

        // insert question
        window.printContent("Inserting question...");
        questionProvider.addQuestion(newQuestion);
        window.printContent("Your question has been inserted successfully!");
    }

    public void rankingMenu() {
        window.top();
        try {
            ScoreReader scoreReader = new ScoreReader();
            Ranking ranking = new Ranking(scoreReader);

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
        // spare 3 rows
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
        String[] interactiveOptions = {"Change Nickname", "Change Password", "Cancel Account", "Individual History"};
        int[] index = {1, 2, 3, 4};
        String choiceToSelect = null;

        // determine max length
        int maxLength = 0;
        for (String choice : interactiveOptions) {
            if (choice.length() > maxLength) {
                maxLength = choice.length();
            }
        }

        // upper board
        String upperBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            upperBorder += "-";
        }
        upperBorder += "------";
        window.printContent(upperBorder);

        for (int i = 0; i < interactiveOptions.length; i++) {
            String line = "      |  " + index[i] + "  |  " + interactiveOptions[i];

            int spacesToFill = maxLength - interactiveOptions[i].length();
            for (int j = 0; j < spacesToFill; j++) {
                line += " "; // filling spaces
            }
            line += "        |";
            window.printContent(line);
        }

        // lower board
        String lowerBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            lowerBorder += "-";
        }
        lowerBorder += "------";
        window.printContent(lowerBorder);


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

            // check input is number
            boolean isIndex = true;

            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    isIndex = false;
                    break;
                }
            }

            if (isIndex) {
                int inputIndex = Integer.parseInt(input);
                // check the index validation
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    choiceToSelect = interactiveOptions[inputIndex - 1];
                }
            } else {

                for (String option : interactiveOptions) {
                    if (option.equalsIgnoreCase(input)) {
                        choiceToSelect = option;
                        break;
                    }
                }
            }

            // switch choice in different conditions
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
                        if(cancelAccountMenu()) {
                            unloggedMenu();
                        }
                        accountManagementMenu();
                        break;
                    case "Individual History":
                        window.bottom();
                        individualHistoryMenu();
                        accountManagementMenu();
                        break;
                    default:
                        window.printContent("Invalid option selected. Please try again.");
                        break;
                }
                break;
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
                break;  // password correct
            } else {
                window.printContent("Wrong password. Please try again.");
            }
        }

        // enter new nickname
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
                user.updateNickname(newNickname1);       // update nickname
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
                break;
            } else {
                window.printContent("Wrong password. Please try again.");
            }
        }

        // enter new nickname
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
                user.updatePassword(newPassword1);       // update password
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

    public boolean cancelAccountMenu() {
        Window window = new Window();
        Scanner scanner = new Scanner(System.in);

        window.printContent("Enter your real name: (or enter 'x' to exit)");
        String realName = scanner.nextLine().trim();
        if (realName.equalsIgnoreCase("x")) {
            window.printContent("Exiting account deletion...");
            return false;
        }

        window.printContent("Enter your password: (or enter 'x' to exit)");
        String password = scanner.nextLine().trim();
        if (password.equalsIgnoreCase("x")) {
            window.printContent("Exiting account deletion...");
            return false;
        }

        // cancel an account
        if (user.Login(realName, password)) {
            if (user.deleteAccount(realName)) {
                window.printContent("Account deleted successfully.");
                scoreEditor.deleteByNickname(user.getNickname());
                user.deleteAccount(user.getNickname());
                return true;
            } else {
                window.printContent("Account deletion failed.");
            }
        } else {
            window.printContent("Invalid credentials. Account deletion failed.");
        }
        window.bottom();
        return true;
    }

    public void individualHistoryMenu() {
        window.top();
        window.printContent("User Profile");
        ArrayList<String> profile = user.getCurrentProfile();
        if (profile != null) {
            for (String info : profile) {
                window.printContent(info);
            }
        } else {
            window.printContent("No profile available.");
        }
        Scanner sc = new Scanner(System.in);
        window.printContent("");
        window.printContent("");
        window.printContent("Enter 'x' to return");
        do {
            if (sc.nextLine().equalsIgnoreCase("x")) {
                window.bottom();
                return;
            }
        } while (true);
    }

    public void dog () {
        System.out.println("       __/ \\   \\/    / \\__       ");
        System.out.println("   __/@    )   ||    (    @\\__   ");
        System.out.println(" O         \\  _||_  /         O  ");
        System.out.println("  \\_____)   \\/ ||  \\/   (_____/  ");
        System.out.println("    U  \\_____\\ /\\ /_____/   U  ");
    }

}
