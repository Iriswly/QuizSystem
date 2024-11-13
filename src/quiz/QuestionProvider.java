package quiz;

import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;

public class QuestionProvider {

    private String[][] questionStorage;
    private int questionCount;
    private int capacity;

    public QuestionProvider(Question initialQuestion) {

        // 初始化问题存储
        this.capacity = 10;
        this.questionStorage = new String[this.capacity][12];
        this.questionCount = 0;

        // 添加问题
        addQuestion(initialQuestion);
    }

    public boolean belongsToTopic(Question question) {
        return question.getTopic() != null;
    }

    public boolean hasStatement(Question question) {
        return question.getQuestionStatement() != null && !question.getQuestionStatement().isEmpty();
    }

    public boolean hasMultipleChoices(Question question) {
        Option[] options = question.getOptions();
        return options != null && options.length > 1;
    }

    public boolean oneCorrectAnswer(Question question) {
        Option[] options = question.getOptions();
        boolean[] trueFalseList = new boolean[options.length];
        for (int i = 0; i < options.length; i++) {
            trueFalseList[i] = options[i].isCorrectAnswer();
        }

        int correctNum = 0;
        for (int i = 0; i < options.length; i++) {
            if (trueFalseList[i]) {
                correctNum++;
            }
        }
        return correctNum == 1;

    }

    public boolean passValidation(Question question) {
        return this.belongsToTopic(question) && this.hasStatement(question) && this.hasMultipleChoices(question) && this.oneCorrectAnswer(question);
    }

    public void addQuestion(Question question) {
        if (passValidation(question)) {
            // 如果问题达到容量上限，扩充容量
            if (questionCount >= capacity) {
                increaseCapacity();
            }

            // 存储问题到二维数组中
            questionStorage[questionCount][0] = String.valueOf(questionCount);
            questionStorage[questionCount][1] = question.getTopic();

            String[] difficulties = {"EASY", "MEDIUM", "HARD", "VERY_HARD"};
            int difficultyIndex = question.getDifficulty().ordinal(); // 获取枚举的序号
            questionStorage[questionCount][2] = difficulties[difficultyIndex];
            questionStorage[questionCount][3] = question.getQuestionStatement();

            Option[] options = question.getOptions();
            for (int i = 0; i < options.length; i++) {
                questionStorage[questionCount][4 + i * 2] = options[i].getAnswer(); // 选项
                questionStorage[questionCount][5 + i * 2] = String.valueOf(options[i].isCorrectAnswer()); // 正确性
            }

            questionCount++;

        }
    }

    private void increaseCapacity() {
        int newCapacity = capacity + 10;
        String[][] newQuestionStorage = new String[newCapacity][12];

        // 复制旧数组
        for (int i = 0; i < questionStorage.length; i++) {
            newQuestionStorage[i] = questionStorage[i];
        }

        // 更新
        questionStorage = newQuestionStorage;
        capacity = newCapacity;
    }

    public String[][] getSelectedQuestions(TopicReader topicReader) {
        String selectedTopic = topicReader.getTopicToSelect();
        String[][] selectedQuestions = new String[questionCount][12];
        int sameTopicCount = 0;

        for (int i = 0; i < questionCount; i++) {
            // 筛选同一主题的问题
            if (questionStorage[i][1].equals(selectedTopic)) {
                selectedQuestions[sameTopicCount] = questionStorage[i];
                sameTopicCount++;
            }
        }

        // 创建符合相同主题的问题
        String[][] sameTopicQuestions = new String[sameTopicCount][];
        for (int i = 0; i < sameTopicCount; i++) {
            sameTopicQuestions[i] = selectedQuestions[i];
        }

        return sameTopicQuestions;
    }

}
