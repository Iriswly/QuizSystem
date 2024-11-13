package quiz;

import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import java.util.Scanner;

public class TopicReader {

    private String topicToSelect; // 用户选择的主题
    private final String[] difficulties = {"EASY", "MEDIUM", "HARD", "VERY_HARD"}; // 难度级别
    private String difficultyToSelect; // 用户选择的难度
    private final String[] topicArray = {"philosophy", "psychology", "astronomy", "geography"}; // 可选主题
    private final int[] index = {1, 2, 3, 4}; // 主题的索引

    public TopicReader() { }

    // 显示主题选择
    public void showTopic() {
        int maxLength = 0;
        for (String s : topicArray) {
            if (s.length() > maxLength) maxLength = s.length();
        }
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) System.out.print("-");
        System.out.println();
        for (int i = 0; i < topicArray.length; i++) {
            System.out.print("|  " + index[i] + "  |  " + topicArray[i]);
            for (int j = topicArray[i].length(); j < maxLength; j++) System.out.print(" ");
            System.out.println("  |");
        }
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) System.out.print("-");
        System.out.println();
    }

    public void selectTopic() {
        System.out.println("Please enter the topic you would like to choose:");
        System.out.println("    Either type in an index (Integer) or type in a topic name (String):");
        Scanner sc = new Scanner(System.in);

        while (topicToSelect == null) {
            if (sc.hasNextInt()) {
                int inputIndex = sc.nextInt();
                // 检查输入的索引是否有效
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    topicToSelect = topicArray[inputIndex - 1]; // 索引从1开始，数组从0开始
                } else {
                    System.out.println("Please enter a valid index or topic name:");
                }
            } else if (sc.hasNext()) {
                String inputTopic = sc.next(); // 读取topic
                // 检查topic是否有效
                for (int i = 0; i < topicArray.length; i++) {
                    String s = topicArray[i];
                    if (s.equals(inputTopic)) {
                        topicToSelect = inputTopic;
                        break;
                    }
                }
                if (topicToSelect == null) {
                    System.out.println("Please enter a valid index or topic name:");
                }
            }
            // 清除无效输入
            sc.nextLine();
        }

        System.out.println("Selected successfully!");
        System.out.println("Your topic is: " + topicToSelect);
        System.out.println();
        System.out.println();
    }

    // 选择难度
    public void selectDifficulty() {
        System.out.println("Please select a difficulty level:");
        for (int i = 0; i < difficulties.length; i++) {
            System.out.println((i + 1) + ". " + difficulties[i]);
        }

        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.print("Enter difficulty index (1-4): ");
            choice = sc.nextInt();
        } while (choice < 1 || choice > difficulties.length);

        difficultyToSelect = difficulties[choice - 1];
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
    public String[][] filterQuestionsByDifficulty(String[][] questions, String selectedDifficulty) {
        int count = 0;
        // 遍历一遍问题数组，计算符合条件的题目数量
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(selectedDifficulty)) {
                count++;
            }
        }

        // 创建符合条件的题目数组
        String[][] filteredQuestions = new String[count][12];
        int index = 0;

        // 再次遍历题目，填充符合条件的题目
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(selectedDifficulty)) {
                filteredQuestions[index++] = questions[i];
            }
        }

        return filteredQuestions;
    }

    // 从筛选出的题目中随机选择指定数量的题目
    public  String[][] randomlySelectQuestions(String[][] questions, int numQuestions) {
        if (questions.length == 0) {
            System.out.println("Error: No questions available to select.");
            return new String[0][0]; // 返回空数组
        }

        String[][] finalQuestions = new String[Math.min(numQuestions, questions.length)][];
        boolean[] selectedIndices = new boolean[questions.length];

        for (int i = 0; i < numQuestions; i++) {
            int randomIndex;

            do {
                randomIndex = (int) (Math.random() * questions.length); // 使用 Math.random() 生成随机数
            } while (selectedIndices[randomIndex]);

            finalQuestions[i] = questions[randomIndex];
            selectedIndices[randomIndex] = true; // 标记该题目为已选择
        }

        return finalQuestions;
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
        if (finalQuestions.length == 0) {
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
        }
    }
}
