package Score;

import java.util.Scanner;

import ScoreDB.ScoreEditor;
import quiz.TopicReader;
import quiz.QuestionProvider;
import Appli.Window;
import Appli.Menu;
import User.UserInfo;


public class ScoreRecord {
    private Menu menu;
    private String scorename= "";
    private UserInfo user;
    private int Round=0;
    private boolean quitInstantly = false;
    private Scanner sc;

    //Stores all session records
    private String[][] sessionInfo = new String[0][3];

    public ScoreRecord(UserInfo newUser) throws Exception {
        user = newUser;
        menu = new Menu(user);
        sc = new Scanner(System.in);
    }
       //Retrieve difficulty, topic, and score data from session
       public String[] getAllScores() {
        String[] scores = new String[sessionInfo.length];
        for (int i = 0; i < sessionInfo.length; i++) {
            scores[i] = sessionInfo[i][2]; // Column 2 stores the score information
        }
        return scores;
    }
    //Retrieve all topic information
       public String[] getAllTopics() {
        String[] topics = new String[sessionInfo.length];
        for (int i = 0; i < sessionInfo.length; i++) {
            topics[i] = sessionInfo[i][0]; //Column 0 stores the topic information
        }
        return topics;
    }

    //Retrieve all difficulty information
    public int[] getAllDifficulties() {
        //Check if sessionInfo is empty
        if (sessionInfo == null || sessionInfo.length == 0) {
            return new int[0];
        }

        int[] difficultyIntegers = new int[sessionInfo.length];

        //Iterate through sessionInfo, extracting and converting difficulty to integers
        for (int i = 0; i < sessionInfo.length; i++) {
            String difficulty = sessionInfo[i][1]; //Column 1 stores difficulty information
            if (difficulty == null) {
                difficultyIntegers[i] = 0; //Handle null case
                continue;
            }

            switch (difficulty.toLowerCase()) {
                case "easy":
                    difficultyIntegers[i] = 1;
                    break;
                case "medium":
                    difficultyIntegers[i] = 2;
                    break;
                case "hard":
                    difficultyIntegers[i] = 3;
                    break;
                case "very_hard":
                    difficultyIntegers[i] = 4;
                    break;
                default:
                    difficultyIntegers[i] = 0;
            }
        }

        return difficultyIntegers;
    }

    //Record one round of topic, difficulty, and score
    public void recordSession(String topic, String difficulty, int score) {
        //Expand the sessionInfo array each round(ListArray is better)
        String[][] newSessionInfo = new String[sessionInfo.length + 1][3];

        //Copy old data to the new array
        for (int i = 0; i < sessionInfo.length; i++) {
            newSessionInfo[i] = sessionInfo[i];
        }

        //Add new record to the new array
        newSessionInfo[sessionInfo.length][0] = topic;
        newSessionInfo[sessionInfo.length][1] = difficulty;
        newSessionInfo[sessionInfo.length][2] = Integer.toString(score);

        //Update
        sessionInfo = newSessionInfo;
    }
    //Display all session records
    public void displayAllSessions() {
        Window window = new Window();
        window.printContent("All Sessions: ");
        for (int i = 0; i < sessionInfo.length; i++) {
            window.printContent("Session " + (i + 1) + ": Topic : " + sessionInfo[i][0] +
                    ", Difficulty : " + sessionInfo[i][1] + ", Score : " + sessionInfo[i][2]);
        }
    }
    //Check if the answer is valid
    private boolean isValidAnswer(String userAnswer) {
        return userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C") || userAnswer.equals("D");
    }

    //Check if the user's answer is correct
    private boolean checkAnswer(String[] question, String userAnswer) {
        String[] questionNum = new String[]{"A", "B", "C", "D"};
        int index = -1;
        for (int i = 0; i < questionNum.length; i++) {
            if (userAnswer.equals(questionNum[i])) {
                index = i;
                break;
            }
        }
        //If the answer is invalid, return false
        if (index == -1) {
            return false;//输入无效
        }
        //After passing the validity check, check correctness
        //finalQuestions[i][5 + index * 2] stores the correct answer flag
        return "true".equalsIgnoreCase(question[5 + index * 2]);
    }

    //Display questions and let the user answer
    public void displayQuestionsAndScore(String[][] finalQuestions,TopicReader topicReader) {
        Window window = new Window();
        int score = 0;
        Round++;
        String topic = topicReader.getTopicToSelect();
        String difficulty = topicReader.getDifficultyToSelect();

        //Store question, options, user answer, and correct answer
        String[][] questionDetails = new String[finalQuestions.length][7];  //Store question-related information
        String[][] sessionInfo = new String[1][3];  //Store topic, difficulty, score

        //Iterate through each question
        for (int i = 0; i < finalQuestions.length; i++) {
            window.printContent("Question " + (i + 1) + ": " + finalQuestions[i][3]);

            //Display options
            String[] questionNum = new String[]{"A", "B", "C", "D"};
            for (int j = 0; j < 4; j++) {
                window.printContent(questionNum[j] + ": " + finalQuestions[i][4 + j * 2]);
                if ("TRUE".equals(finalQuestions[i][5 + j * 2])) {
                    questionDetails[i][5] = finalQuestions[i][4 + j * 2];  //Record correct answer option letter to help get score
                }
            }


            //Initialize user answer and validity, and verify input before retrieving
            String userAnswer = "";
            boolean validAnswer = false;

            //Loop until the user enters a valid answer
            while (!validAnswer) {
                window.printContent("Your answer (A, B, C, D or 'x' to exit): ");
                userAnswer = sc.nextLine().toUpperCase();
                //Support user exiting instantly
                if (userAnswer.equals("X")) {
                    window.printContent("Exiting quiz and returning to main menu...");
                    quitInstantly = true;
                    return; //End current method and return to main menu
                }

                //Check if the user input is valid
                if (!isValidAnswer(userAnswer)) {
                    window.printContent("Invalid input. Please enter A, B, C, or D.");
                } else {
                    validAnswer = true;
                }
            }

            //Record question details, options, user answer, and correct answer
            questionDetails[i][0] = finalQuestions[i][3];  //Question content
            questionDetails[i][1] = finalQuestions[i][4];  //OptionsA
            questionDetails[i][2] = finalQuestions[i][6];  //OptionsB
            questionDetails[i][3] = finalQuestions[i][8];  //OptionsC
            questionDetails[i][4] = finalQuestions[i][10]; //OptionsD
            questionDetails[i][6] = userAnswer;//user answer
            //Check if the answer is correct
            boolean isCorrect = checkAnswer(finalQuestions[i], userAnswer);
            //Calculate the score
            if (isCorrect) {
                window.printContent("Correct!");
                score = score + 100 / finalQuestions.length;
            } else {
                window.printContent("Incorrect!");
            }
            // 记录题目详情
            questionDetails[i][0] = finalQuestions[i][3];  // 问题内容
            for (int j = 0; j < 4; j++) {
                questionDetails[i][j + 1] = finalQuestions[i][4 + j * 2];  // 选项
            }
            window.printContent(""); // 分隔下一题
        }

        window.printContent("Your answers for this session: ");
            for (int i = 0; i < questionDetails.length; i++) {
                if (questionDetails[i][0] != null) {
                    window.printContent("Question " + (i + 1) + ": " + questionDetails[i][0]);
                    window.printContent("Options: ");
                    window.printContent("A: " + questionDetails[i][1]);
                    window.printContent("B: " + questionDetails[i][2]);
                    window.printContent("C: " + questionDetails[i][3]);
                    window.printContent("D: " + questionDetails[i][4]);
                    window.printContent("Correct answer: " + questionDetails[i][5]);
                    window.printContent("Your answer: " + questionDetails[i][6]);
                    window.printContent("");
                }
            }
        window.printContent("");
        // 显示最终得分
        window.printContent("Your final score: " + score + "/100");
        // 假设在某次答题后获得了分数 score
        recordSession(topic, difficulty, score);  // 记录当前 session

        // 显示所有 session 记录
        displayAllSessions();


        //Insert name, topic, difficulty, score into ScoreDB
        try {
            String nickname= user.getNickname();
            ScoreEditor scoreEditor=new ScoreEditor();
            String[] AllScores = getAllScores();
            String[] AllTopics = getAllTopics();
            int[] AllDifficulties = getAllDifficulties();

            scoreEditor.insertRow(nickname,AllTopics[Round-1],AllDifficulties[Round-1],Integer.parseInt(AllScores[Round-1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //Insert score into CSV file
        try {
            UserInfo userInfo = new UserInfo();
            String[] AllScore = getAllScores();
            scorename = "Round: " + Round;
            user.updateScore(Integer.parseInt(AllScore[Round-1]), scorename);
            System.out.println(AllScore[Round-1]);
            System.out.println("Score recorded.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Handle user's choice to continue answering questions
    public boolean askContinue() {
        if (quitInstantly) {
            return false;
        }
        String userChoice = "";
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("Do you want to continue (yes/no)? ");
            userChoice = sc.nextLine().trim().toLowerCase();

            if (userChoice.equals("yes")) {
                validChoice = true;
                return true;
            } else if (userChoice.equals("no")) {
                validChoice = true;
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        return false;
    }

    public static void main(String[] args) {
        QuestionProvider questionProvider = new QuestionProvider();
        ScoreRecord scoreRecord = null;
        try {
            // fix: remind to substitute user in the main logic
            UserInfo user = new UserInfo();
            user.Login("Rose", "Dxllovejava");
            scoreRecord = new ScoreRecord(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int round = 0;//Count the number of rounds
        do {
            TopicReader topicReader = new TopicReader();
            topicReader.showTopic();
            topicReader.selectTopic();
            topicReader.selectDifficulty();
            round++;
            //Get selected topic questions
            String selectedTopic = topicReader.getTopicToSelect();
            System.out.println("Selected Topic: " + selectedTopic);
            String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);
            System.out.println("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

            //Categorize the selected topic questions by difficulty
            String[][] easyQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "EASY");
            System.out.println("Number of easy questions: " + (easyQuestions != null ? easyQuestions.length : 0));
            String[][] mediumQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "MEDIUM");
            System.out.println("Number of medium questions: " + (mediumQuestions != null ? mediumQuestions.length : 0));
            String[][] hardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "HARD");
            System.out.println("Number of hard questions: " + (hardQuestions != null ? hardQuestions.length : 0));
            String[][] veryhardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "VERY_HARD");
            System.out.println("Number of veryhard questions: " + (veryhardQuestions != null ? veryhardQuestions.length : 0));


            //Randomly select questions based on the difficulty level chosen by the user
            String selectedDifficulty = topicReader.getDifficultyToSelect();
            String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 20, selectedDifficulty);
            System.out.println("Number of quiz questions: " + (quizQuestions != null ? quizQuestions.length : 0));

            //Create ScoreRecord instance and start the quiz
            scoreRecord.displayQuestionsAndScore(quizQuestions, topicReader);

        } while (scoreRecord.askContinue());

        System.out.println("Exiting...");
    }
}


