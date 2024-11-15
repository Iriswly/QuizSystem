package Score;

import java.util.Scanner;
import quiz.TopicReader;
import quiz.QuestionProvider;
import Appli.Window;
import Appli.Menu;
import User.UserInfo;

public class ScoreRecord extends UserInfo{

    private Menu menu = new Menu();
    private String scorename="";
    private int Round=0;
    // 存储所有的 session 记录
    private String[][] sessionInfo = new String[0][3];  // 初始时空数组，表示没有记录

    public ScoreRecord() throws Exception {
        super();
    }


    public String[] getAllScores() {
        String[] scores = new String[sessionInfo.length];
        for (int i = 0; i < sessionInfo.length; i++) {
            scores[i] = sessionInfo[i][2]; // 2 列存储了 score 信息
        }
        return scores;
    }
    // 记录一轮答题的topic, difficulty, score
    public void recordSession(String topic, String difficulty, int score) {
        // 扩展 sessionInfo 数组
        String[][] newSessionInfo = new String[sessionInfo.length + 1][3];

        // 复制旧数据到新数组
        for (int i = 0; i < sessionInfo.length; i++) {
            newSessionInfo[i] = sessionInfo[i];
        }

        // 将新记录添加到新数组
        newSessionInfo[sessionInfo.length][0] = topic;
        newSessionInfo[sessionInfo.length][1] = difficulty;
        newSessionInfo[sessionInfo.length][2] = Integer.toString(score);

        // 更新 sessionInfo 数组
        sessionInfo = newSessionInfo;
    }
    // 显示所有记录
    public void displayAllSessions() {
        Window window = new Window();
        window.printContent("All Sessions: ");
        for (int i = 0; i < sessionInfo.length; i++) {
            window.printContent("Session " + (i + 1) + ": Topic : " + sessionInfo[i][0] +
                    ", Difficulty : " + sessionInfo[i][1] + ", Score : " + sessionInfo[i][2]);
        }
    }
    // 判断是否是有效的选项
    private boolean isValidAnswer(String userAnswer) {
        return userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C") || userAnswer.equals("D");
    }

    // 判断用户答案是否正确
    private boolean checkAnswer(String[] question, String userAnswer) {
        // 选项字母与数组中的索引映射
        String[] questionNum = new String[]{"A", "B", "C", "D"};
        int index = -1;
        //查看答案是否有效
        for (int i = 0; i < questionNum.length; i++) {
            if (userAnswer.equals(questionNum[i])) {
                index = i;
                break;
            }
        }
        //输入非法答案则返回错误
        if (index == -1) {
            return false;//输入无效
        }
        //通过合法性检验后检测正确性
        // finalQuestions[i][5 + index * 2] 存储的是正确答案标记
        return "true".equalsIgnoreCase(question[5 + index * 2]);
    }

    // 显示题目并让用户作答
    public void displayQuestionsAndScore(String[][] finalQuestions,TopicReader topicReader) {
        Window window = new Window();
        Scanner sc = new Scanner(System.in);
        int score = 0;
        Round++;
        String topic = topicReader.getTopicToSelect();
        String difficulty = topicReader.getDifficultyToSelect();

        // 记录题目、选项、用户答案、正确答案
        String[][] questionDetails = new String[finalQuestions.length][7];  // 存储题目相关信息
        String[][] sessionInfo = new String[1][3];  // 存储话题、难度、得分

        // 遍历每一道题
        for (int i = 0; i < finalQuestions.length; i++) {
            window.printContent("Question " + (i + 1) + ": " + finalQuestions[i][3]); // 显示问题内容

            // 显示选项
            String[] questionNum = new String[]{"A", "B", "C", "D"};
            for (int j = 0; j < 4; j++) {
                window.printContent(questionNum[j] + ": " + finalQuestions[i][4 + j * 2]);
                if ("TRUE".equals(finalQuestions[i][5 + j * 2])) {
                    questionDetails[i][5] = finalQuestions[i][4 + j * 2];  // 记录正确答案的选项字母
                }
            }


            // 先初始化用户答案和合法性，在后面的代码里验证合法以后再获取输入
            String userAnswer = "";
            boolean validAnswer = false;

            // 循环直到用户输入合法答案
            while (!validAnswer) {
                window.printContent("Your answer (A, B, C, D): ");
                userAnswer = sc.nextLine().toUpperCase();  // 读取用户输入并转为大写

                // 判断用户输入是否有效
                if (!isValidAnswer(userAnswer)) {
                    window.printContent("Invalid input. Please enter A, B, C, or D.");
                } else {
                    validAnswer = true;  // 输入合法，则跳出循环
                }
            }
// 将题目、选项、用户答案、正确答案记录到 questionDetails 数组

            questionDetails[i][0] = finalQuestions[i][3];  // 问题
            questionDetails[i][1] = finalQuestions[i][4];  // 选项A
            questionDetails[i][2] = finalQuestions[i][6];  // 选项B
            questionDetails[i][3] = finalQuestions[i][8];  // 选项C
            questionDetails[i][4] = finalQuestions[i][10]; // 选项D
            questionDetails[i][6] = userAnswer;//用户答案
            // 判断用户答案是否正确
            boolean isCorrect = checkAnswer(finalQuestions[i], userAnswer);

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
        //将成绩录入csv
        try {
            UserInfo userInfo = new UserInfo();
            String[] AllScore = getAllScores();
            scorename = "Round: " + Round;
            updateScore(Integer.parseInt(AllScore[Round-1]), scorename);
            System.out.println(AllScore[Round-1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    // 处理用户是否继续做题的选择
    public boolean askContinue() {
        Scanner sc = new Scanner(System.in);
        String userChoice = "";
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("Do you want to continue (yes/no)? ");
            userChoice = sc.nextLine().trim().toLowerCase();  // 去除多余空格并转换为小写

            if (userChoice.equals("yes")) {
                validChoice = true;
                return true;
            } else if (userChoice.equals("no")) {
                validChoice = true;
                menu.mainMenu();
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        return false; // 默认返回false，如果进入了while会退出循环返回值
    }





    public static void main(String[] args) {
        // 创建三个类的实例
        QuestionProvider questionProvider = new QuestionProvider();
        ScoreRecord scoreRecord = null;
        try {
            scoreRecord = new ScoreRecord();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int round=0;//计算做题次数
        do {
            TopicReader topicReader = new TopicReader();
            topicReader.showTopic();
            topicReader.selectTopic();
            topicReader.selectDifficulty();
            round++;
            // 获取选择主题的题目
            String selectedTopic = topicReader.getTopicToSelect();
            System.out.println("Selected Topic: " + selectedTopic);
            String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);
            System.out.println("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

            // 按题目难度分类该主题题目
            String[][] easyQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "EASY");
            System.out.println("Number of easy questions: " + (easyQuestions != null ? easyQuestions.length : 0));
            String[][] mediumQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "MEDIUM");
            System.out.println("Number of medium questions: " + (mediumQuestions != null ? mediumQuestions.length : 0));
            String[][] hardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "HARD");
            System.out.println("Number of hard questions: " + (hardQuestions != null ? hardQuestions.length : 0));
            String[][] veryhardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "VERY_HARD");
            System.out.println("Number of veryhard questions: " + (veryhardQuestions != null ? veryhardQuestions.length : 0));


            // 根据用户选择的难度等级随机选择题目
            String selectedDifficulty = topicReader.getDifficultyToSelect();
            String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 10, selectedDifficulty);
            System.out.println("Number of quiz questions: " + (quizQuestions != null ? quizQuestions.length : 0));

            // 创建 ScoreRecord 实例并开始答题
            scoreRecord.displayQuestionsAndScore(quizQuestions, topicReader);


        } while (scoreRecord.askContinue());

        System.out.println("Exiting...");
    }
}

