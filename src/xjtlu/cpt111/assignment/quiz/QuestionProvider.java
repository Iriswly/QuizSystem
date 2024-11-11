package xjtlu.cpt111.assignment.quiz;

import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;

public class QuestionProvider {
    private final String topic;
    private final String questionStatement;
    private final String[] answerList;
    private final boolean[] trueFalseList;
    private final Question question;

    public QuestionProvider(Question question) {
        this.question = question;
        this.topic = question.getTopic();
        this.questionStatement = question.getQuestionStatement();

        Option[] options = question.getOptions();
        this.answerList = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            this.answerList[i] = options[i].getAnswer();
        }

        this.trueFalseList = new boolean[options.length];
        for (int i = 0; i < options.length; i++) {
            this.trueFalseList[i] = options[i].isCorrectAnswer();
        }
    }

    public boolean belongsToTopic(String topic) {
        return this.topic != null && this.topic.equals(topic);
    }

    public boolean hasStatement() {
        return this.questionStatement != null && !this.questionStatement.isEmpty();
    }

    public boolean hasMultipleChoices() {
        return this.answerList != null && this.answerList.length > 1;
    }

    public boolean oneCorrectAnswer() {
        int correctNum = 0;
        for (int i = 0; i < this.answerList.length; i++) {
            if (trueFalseList[i]) {
                correctNum++;
            }
        }
        return correctNum == 1;
    }

    public boolean passValidation(String topic) {
        return this.belongsToTopic(topic) && this.hasStatement() && this.hasMultipleChoices() && this.oneCorrectAnswer();
    }

    public String[][] selectedQuestions(TopicReader topicReader) {
        String selectedTopic = topicReader.getTopicToSelect();
        if (!this.belongsToTopic(selectedTopic)) {
            return null; // 如果主题不匹配，返回null
        }

        int numOfOptions = answerList.length;
        String[][] selectedQuestions = new String[1][numOfOptions * 2 + 4];   // +4 for index, topic, statement, difficulty
        selectedQuestions[0][0] = String.valueOf(0);   // 从0开始编号
        selectedQuestions[0][1] = this.topic;

        // 数字序号对应的难度字符串
        String[] difficulties = {"EASY", "MEDIUM", "HARD", "VERY_HARD"};
        int difficultyIndex = this.question.getDifficulty().ordinal();   // 获取枚举的序号
        selectedQuestions[0][2] = difficulties[difficultyIndex];

        selectedQuestions[0][3] = this.questionStatement;

        for (int i = 0; i < numOfOptions; i++) {
            selectedQuestions[0][4 + i * 2] = answerList[i]; // 选项
            selectedQuestions[0][5 + i * 2] = String.valueOf(trueFalseList[i]); // 正确性
        }

        return selectedQuestions;
    }


}
