package Question;

import CSVEditor_QuestionSystem.CSVBase;
import quiz.QuestionProvider;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;

import java.io.*;

public class InsertQuestion {

    private String topic;
    private Difficulty difficulty;
    private String questionStatement;
    private String[] options = new String[4];
    private boolean[] correctness = new boolean[4];
    private int questionCount;

    private CSVBase csvBase;

    public InsertQuestion(Question question, int questionCount) throws IOException {
        try {
            this.csvBase = new CSVBase();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 进行问题合规性验证
        if (!passValidation(question)) {
            throw new IllegalArgumentException("Question does not pass validation.");
        }

        // 获取并设置属性
        this.topic = question.getTopic();
        this.difficulty = question.getDifficulty();
        this.questionStatement = question.getQuestionStatement();
        Option[] optionsArray = question.getOptions();

        for (int i = 0; i < optionsArray.length; i++) {
            options[i] = optionsArray[i].getAnswer();
            correctness[i] = optionsArray[i].isCorrectAnswer();
        }

        // 在插入之前检查重复
        if (isDuplicateQuestion(this.questionStatement)) {
            System.out.println("Error: A question with the same statement already exists. Not inserting.");
            return; // 不插入重复问题
        }

        this.questionCount = questionCount;

        // 写入CSV
        writeQuestionToCSV();
    }

    private boolean passValidation(Question question) {
        return belongsToTopic(question) &&
                hasStatement(question) &&
                hasMultipleChoices(question) &&
                oneCorrectAnswer(question);
    }

    private boolean belongsToTopic(Question question) {
        return question.getTopic() != null;
    }

    private boolean hasStatement(Question question) {
        return question.getQuestionStatement() != null && !question.getQuestionStatement().isEmpty();
    }

    private boolean hasMultipleChoices(Question question) {
        Option[] options = question.getOptions();
        return options != null && options.length == 4; // 确保有4个选项
    }

    public boolean oneCorrectAnswer(Question question) {
        Option[] options = question.getOptions();
        int correctNum = 0;
        for (Option option : options) {
            if (option.isCorrectAnswer()) {
                correctNum++;
            }
        }
        return correctNum == 1;
    }

    private boolean isDuplicateQuestion(String questionStatement) {
        String filePath = getCSVFilePath(topic);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // 根据 CSV 格式分割
                if (parts.length > 3 && parts[3].equals(questionStatement)) { // 假设 questionStatement 在第四列
                    return true; // 找到重复问题
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 没有找到重复问题
    }

    private void writeQuestionToCSV() {
        String filePath = getCSVFilePath(topic);
        File file = new File(filePath);
        boolean isNewFile = !file.exists() || file.length() == 0; // 检查文件是否不存在或为空

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // 如果是新文件，写入标题行
            if (isNewFile) {
                writer.write("ID,Topic,Difficulty,Question,Option1,Correct1,Option2,Correct2,Option3,Correct3,Option4,Correct4\n");
            }

            // 写入问题内容
            writer.write(String.format("%d,%s,%s,%s,%s,%b,%s,%b,%s,%b,%s,%b%n",
                    questionCount,                 // 使用 questionCount 作为编号
                    topic,
                    difficulty,
                    questionStatement,
                    options[0], correctness[0],
                    options[1], correctness[1],
                    options[2], correctness[2],
                    options[3], correctness[3]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCSVFilePath(String topic) {
        switch (topic) {
            case "philosophy":
                return CSVBase.FILEPATH_PHILOSOPHY;
            case "psychology":
                return CSVBase.FILEPATH_PSYCHOLOGY;
            case "astronomy":
                return CSVBase.FILEPATH_ASTRONOMY;
            case "geography":
                return CSVBase.FILEPATH_GEOGRAPHY;
            default:
                return CSVBase.FILEPATH_NEW + "/" + topic + ".csv";
        }
    }


    public static void main(String[] args) {
        // 创建 QuestionProvider 实例
        QuestionProvider questionProvider = new QuestionProvider();

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
        Option option10 = new Option("Cogito is ergo sum", true);
        Option option11 = new Option("Morality is subjective", false);
        Option option12 = new Option("I don't know", false);
        Option[] options3 = {option9, option10, option11, option12};
        Question philosophyQuestion1 = new Question("philosophy", Difficulty.EASY,
                "What is Descartes famous philosophical statement?", options3);


        System.out.println("Inserting question 1...");
        questionProvider.addQuestion(astronomyQuestion1);

        System.out.println("Inserting question 2...");
        questionProvider.addQuestion(astronomyQuestion2);

        System.out.println("Inserting question 3...");
        questionProvider.addQuestion(philosophyQuestion1);

        System.out.println("Questions have been inserted successfully!");
    }
}
