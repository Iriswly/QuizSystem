package quiz;

import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Option;
import xjtlu.cpt111.assignment.quiz.model.Question;
import java.util.Scanner;

public class TopicReader {

    private String topicToSelect;

    public TopicReader() {

    }

    private final String[] topicArray = {"philosophy", "psychology", "astronomy", "geography"};
    private final int[] index = {1, 2, 3, 4};

    public void showTopic() {
        // 确定主题的最大长度
        int maxLength = 0;
        for (int i = 0; i < topicArray.length; i++) {
            String s = topicArray[i];
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }

        // 上边框
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < topicArray.length; i++) {
            System.out.print("|  "); // 左侧边框
            System.out.print(index[i]); // 显示索引
            System.out.print("  |  "); // 列分隔

            // 输出主题并填充空格使其对齐
            System.out.print(topicArray[i]);

            // 计算需要填充的空格数量
            int spacesToFill = maxLength - topicArray[i].length();
            for (int j = 0; j < spacesToFill; j++) {
                System.out.print(" "); // 填充空格
            }

            System.out.print("  |");
            System.out.println();
        }

        // 下边框
        System.out.print("------------");
        for (int i = 0; i < maxLength; i++) {
            System.out.print("-");
        }
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

    public String getTopicToSelect() {
        return this.topicToSelect;
    }

    public static void main(String[] args) {
        // 问题1
        Option option1 = new Option("Mars", false);
        Option option2 = new Option("Venus", false);
        Option option3 = new Option("Mercury", true);
        Option option4 = new Option("Jupiter", false);
        Option[] options1 = {option1, option2, option3, option4};
        Question astronomyQuestion1 = new Question("astronomy", Difficulty.MEDIUM,
                "Which planet is the closest to the Sun?", options1);

        // 问题3
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
        Question philosophyQuestion1 = new Question("philosophy", Difficulty.EASY,
                "What is Descartes famous philosophical statement?", options3);

        // 创建 QuestionProvider 实例并添加问题
        quiz.QuestionProvider questionProvider = new quiz.QuestionProvider(astronomyQuestion1);
        questionProvider.addQuestion(astronomyQuestion2);
        questionProvider.addQuestion(philosophyQuestion1);

        // 显示主题选择
        TopicReader topicReader = new TopicReader();
        topicReader.showTopic();
        topicReader.selectTopic();

        // 根据选择的主题获取问题
        String[][] selectedQuestions = questionProvider.getSelectedQuestions(topicReader);
        if (selectedQuestions != null && selectedQuestions.length > 0) {
            for (int row = 0; row < selectedQuestions.length; row++) {
                System.out.println("Question " + (row+1) + ": ");
                for (int col = 0; col < selectedQuestions[row].length; col++) {
                    System.out.println("selectedQuestions[" + row + "][" + col + "] = " + selectedQuestions[row][col]);
                }
                System.out.println();
            }
        } else {
            System.out.println("No questions found for the selected topic.");
        }
    }

}
