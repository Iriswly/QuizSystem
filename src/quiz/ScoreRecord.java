package quiz;
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


    }


