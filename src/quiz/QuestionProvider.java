package quiz;

import CSVEditor_QuestionSystem.CSVBase;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;

import Question.InsertQuestion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class QuestionProvider {

    private String[][] questionStorage;
    private int questionCount = 0;
    private int capacity = 10; // 固定容量为10，后续可扩展
    private CSVBase csvBase;

    public QuestionProvider() {
        this.questionStorage = new String[this.capacity][12];
        this.questionCount = 0;
        try {
            this.csvBase = new CSVBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addQuestion(Question question) {
        try {
            // 检查是否需要扩展存储空间
            if (questionCount >= capacity) {
                increaseCapacity(); // 扩展容量
            }
            // 使用 InsertQuestion 进行插入，这会处理重复性检查
            new InsertQuestion(question, questionCount); // 直接调用 InsertQuestion

            // 将问题存储到内存
            questionStorage[questionCount][0] = String.valueOf(questionCount);
            questionStorage[questionCount][1] = question.getTopic();
            questionStorage[questionCount][2] = question.getDifficulty().name();
            questionStorage[questionCount][3] = question.getQuestionStatement();

            Option[] options = question.getOptions();
            for (int i = 0; i < options.length; i++) {
                questionStorage[questionCount][4 + i * 2] = options[i].getAnswer();
                questionStorage[questionCount][5 + i * 2] = String.valueOf(options[i].isCorrectAnswer());
            }

            questionCount++;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // 处理问题插入失败的情况，例如重复问题
            System.out.println("Failed to add question: " + e.getMessage());
        }
    }

    private void increaseCapacity() {
        capacity += 10;
        String[][] newQuestionStorage = new String[capacity][12];
        // 复制旧数组
        for (int i = 0; i < questionCount; i++) {
            newQuestionStorage[i] = questionStorage[i];
        }
        questionStorage = newQuestionStorage;  // 更新存储

    }

    public String[][] getSelectedQuestions(String selectedTopic) {
        String[][] selectedQuestions = new String[capacity][12];
        int sameTopicCount = 0;

        String filePath = getCSVFilePath(selectedTopic);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equals(selectedTopic)) {
                    selectedQuestions[sameTopicCount++] = parts;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] sameTopicQuestions = new String[sameTopicCount][];
        System.arraycopy(selectedQuestions, 0, sameTopicQuestions, 0, sameTopicCount);
        return sameTopicQuestions;
    }

    private String getCSVFilePath(String topic) {
        switch (topic) {
            case "philosophy": return CSVBase.FILEPATH_PHILOSOPHY;
            case "psychology": return CSVBase.FILEPATH_PSYCHOLOGY;
            case "astronomy": return CSVBase.FILEPATH_ASTRONOMY;
            case "geography": return CSVBase.FILEPATH_GEOGRAPHY;
            default: return CSVBase.FILEPATH_NEW + "/" + topic + ".csv"; // 默认路径
        }
    }

    // 获取相同主题的问题并重新编号
    public String[][] getSameTopicQuestions(String topicToSelect) {
        // 统计相同主题问题的数量
        int count = 0;
        for (int i = 0; i < questionCount; i++) {
            if (questionStorage[i][1].equals(topicToSelect)) {
                count++;
            }
        }

        // 创建一个新的二维数组，用于存储重新编号的问题
        String[][] sameTopicQuestions = new String[count][questionStorage[0].length];
        int newIndex = 0;

        for (int i = 0; i < questionCount; i++) {
            if (questionStorage[i][1].equals(topicToSelect)) {
                // 重新编号
                sameTopicQuestions[newIndex][0] = String.valueOf(newIndex); // 新的编号
                // 复制其他列
                for (int j = 1; j < questionStorage[i].length; j++) {
                    sameTopicQuestions[newIndex][j] = questionStorage[i][j];
                }
                newIndex++;
            }
        }

        return sameTopicQuestions;
    }
}
