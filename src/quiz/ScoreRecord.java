package quiz;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;

import java.util.Scanner;

public class ScoreRecord {
        // 判断用户答案是否正确
         private boolean checkAnswer(String[] question, String userAnswer) {
        // 选项字母与数组中的索引映射
        String[] questionNum = new String[]{"A", "B", "C", "D"};
        int index = -1;

        for (int i = 0; i < questionNum.length; i++) {
            if (userAnswer.equals(questionNum[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Invalid input. Please enter A, B, C, or D.");
            return false;
        }

        // 判断选项是否正确，finalQuestions[i][5 + index * 2] 存储的是正确答案标记
        return "true".equals(question[5 + index * 2]);
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
                for (int j = 4; j <= 10; j += 2) {
                    if (finalQuestions[i][j] != null) {
                        int questionNumIndex = (j - 4) / 2;  // 计算选项字母索引
                        System.out.println(questionNum[questionNumIndex] + ": " + finalQuestions[i][j]);
                    }
                }

                // 获取用户答案
                System.out.print("Your answer (A, B, C, D): ");
                String userAnswer = sc.nextLine().toUpperCase();  // 读取用户输入并转为大写

                // 判断用户答案是否正确
                boolean isCorrect = checkAnswer(finalQuestions[i], userAnswer);
                if (isCorrect) {
                    System.out.println("Correct!");
                    score=score+100/finalQuestions.length;
                } else {
                    System.out.println("Incorrect!");
                }

                System.out.println(); // 分隔下一题
            }

            // 显示最终得分
            System.out.println("Your final score: " + score + "/100" );
        }

    // 在 main 方法中获取最终选中的题目
    public static void main(String[] args) {
        // 问题1
        Option option1 = new Option("Mars", false);
        Option option2 = new Option("Venus", false);
        Option option3 = new Option("Mercury", true);
        Option option4 = new Option("Jupiter", false);
        Option[] options1 = {option1, option2, option3, option4};
        Question astronomyQuestion1 = new Question("astronomy", Difficulty.MEDIUM,
                "Which planet is the closest to the Sun?", options1);

        // 问题2
        Option option5 = new Option("Black Hole", true);
        Option option6 = new Option("White Dwarf", false);
        Option option7 = new Option("Neutron Star", false);
        Option option8 = new Option("Red Giant", false);
        Option[] options2 = {option5, option6, option7, option8};
        Question astronomyQuestion2 = new Question("astronomy", Difficulty.HARD,
                "What do we call a region in space where the gravitational pull is so strong that nothing can escape?", options2);

        // 问题3
        Option option9 = new Option("Existence is an illusion", false);
        Option option10 = new Option("Cogito, ergo sum", true);
        Option option11 = new Option("Morality is subjective", false);
        Option option12 = new Option("I don't know", false);
        Option[] options3 = {option9, option10, option11, option12};
        Question philosophyQuestion1 = new Question("astronomy", Difficulty.MEDIUM,
                "What is Descartes famous philosophical statement?", options3);

        // 创建 QuestionProvider 实例并添加问题
        QuestionProvider questionProvider = new QuestionProvider(astronomyQuestion1);
        questionProvider.addQuestion(astronomyQuestion2);
        questionProvider.addQuestion(philosophyQuestion1);

        // 显示主题选择
        TopicReader topicReader = new TopicReader();
        topicReader.showTopic();
        topicReader.selectTopic();
        topicReader.selectDifficulty();

        // 获取选择主题的题目并筛选难度
        String[][] selectedQuestions = questionProvider.getSelectedQuestions(topicReader);
        /*检验话题问题输出是否有问题
        if (selectedQuestions != null && selectedQuestions.length > 0) {
            for (int row = 0; row < selectedQuestions.length; row++) {
                System.out.println("Question " + (row+1) + ": ");
                for (int col = 0; col < selectedQuestions[row].length; col++) {
                    System.out.println("selectedQuestions[" + row + "][" + col + "] = " + selectedQuestions[row][col]);
                }
                System.out.println();
            }
        } else {
            System.out.println("No questions found for the selected topic.");*/

        // 筛选题目
        String[][] filteredQuestions = topicReader.filterQuestionsByDifficulty(selectedQuestions, topicReader.getDifficultyToSelect());
        // 随机选择题目
        String[][] finalQuestions = topicReader.randomlySelectQuestions(filteredQuestions, 2);
        //！！！！！！！！！！！题库更新后改成10

        // 显示最终选中的题目
        /*if (finalQuestions.length == 0) {
            System.out.println("No questions found for the selected topic and difficulty.");
        } else {
            for (int i = 0; i < finalQuestions.length; i++) {
                System.out.println("Question " + (i + 1) + ": " + finalQuestions[i][3]); // 显示问题内容
                for (int j = 4; j <= 10; j += 2) {
                    if (finalQuestions[i][j] != null) {
                        String[] QuestionNum=new String[]{"A","B","C","D"};
                        int QuestionNumIndex=(j-4)/2;
                        System.out.println(QuestionNum[QuestionNumIndex] +":"+ finalQuestions[i][j]);
                    }
                }
                System.out.println();
            }
        }*/
        // 创建 QuizGame 实例并开始答题
        ScoreRecord Startquiz = new ScoreRecord();
        Startquiz.displayQuestionsAndScore(finalQuestions);
    }
    }


