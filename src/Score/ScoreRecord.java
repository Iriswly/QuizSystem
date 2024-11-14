package Score;

import java.util.Scanner;
import quiz.TopicReader;
import quiz.QuestionProvider;

public class ScoreRecord {

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
    public void displayQuestionsAndScore(String[][] finalQuestions) {
        Scanner sc = new Scanner(System.in);
        int score = 0;

        // 遍历每一道题
        for (int i = 0; i < finalQuestions.length; i++) {
            System.out.println("Question " + (i + 1) + ": " + finalQuestions[i][3]); // 显示问题内容

            // 显示选项
            String[] questionNum = new String[]{"A", "B", "C", "D"};
            for (int j = 0; j < 4; j++) { // 假设每个问题都有4个选项
                System.out.println(questionNum[j] + ": " + finalQuestions[i][4 + j * 2]);
            }


            // 先初始化用户答案和合法性，在后面的代码里验证合法以后再获取输入
            String userAnswer = "";
            boolean validAnswer = false;

            // 循环直到用户输入合法答案
            while (!validAnswer) {
                System.out.print("Your answer (A, B, C, D): ");
                userAnswer = sc.nextLine().toUpperCase();  // 读取用户输入并转为大写

                // 判断用户输入是否有效
                if (!isValidAnswer(userAnswer)) {
                    System.out.println("Invalid input. Please enter A, B, C, or D.");
                } else {
                    validAnswer = true;  // 输入合法，则跳出循环
                }
            }


            // 判断用户答案是否正确
            boolean isCorrect = checkAnswer(finalQuestions[i], userAnswer);
            if (isCorrect) {
                System.out.println("Correct!");
                score = score + 100 / finalQuestions.length;
            } else {
                System.out.println("Incorrect!");
            }

            System.out.println(); // 分隔下一题
        }

        // 显示最终得分
        System.out.println("Your final score: " + score + "/100");
    }


    public static void main(String[] args) {
        // 创建 QuestionProvider 实例
        QuestionProvider questionProvider = new QuestionProvider();

        // 显示主题选择
        TopicReader topicReader = new TopicReader();
        topicReader.showTopic();
        topicReader.selectTopic();
        topicReader.selectDifficulty();

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
        String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 20, selectedDifficulty);
        System.out.println("Number of quiz questions: " + (quizQuestions != null ? quizQuestions.length : 0));
        // 创建 ScoreRecord 实例并开始答题
        ScoreRecord startQuiz = new ScoreRecord();
        startQuiz.displayQuestionsAndScore(quizQuestions);
    }
}
