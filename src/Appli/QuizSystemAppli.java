package Appli;

import Authenticator.Register;
import Question.InsertQuestion;
import quiz.TopicReader;
import quiz.QuestionProvider;
import Score.ScoreRecord;

public class QuizSystemAppli {

    public static void main(String[] args) {
        Window window = new Window(120, 20, "  Quiz System");
        QuestionProvider questionProvider = new QuestionProvider();
        ScoreRecord scoreRecord = new ScoreRecord();

        // 注册登录部分
        window.top();
        try {
            Register register = new Register();
            register.displayMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 做题部分
        do {
            window.top();

            TopicReader topicReader = new TopicReader();
            topicReader.showTopic();
            topicReader.selectTopic();
            topicReader.selectDifficulty();

            // 获取选择主题的题目
            String selectedTopic = topicReader.getTopicToSelect();
            window.printContent("Selected Topic: " + selectedTopic);
            String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);
            window.printContent("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

            // 按题目难度分类该主题题目
            String[][] easyQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "EASY");
            window.printContent("Number of easy questions: " + (easyQuestions != null ? easyQuestions.length : 0));
            String[][] mediumQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "MEDIUM");
            window.printContent("Number of medium questions: " + (mediumQuestions != null ? mediumQuestions.length : 0));
            String[][] hardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "HARD");
            window.printContent("Number of hard questions: " + (hardQuestions != null ? hardQuestions.length : 0));
            String[][] veryhardQuestions = topicReader.QuestionsByDifficulty(selectedQuestions, "VERY_HARD");
            window.printContent("Number of veryhard questions: " + (veryhardQuestions != null ? veryhardQuestions.length : 0));

            // 根据用户选择的难度等级随机选择题目
            String selectedDifficulty = topicReader.getDifficultyToSelect();
            String[][] quizQuestions = TopicReader.QuizQuestion(selectedQuestions, 10, selectedDifficulty);
            window.printContent("Number of quiz questions: " + (quizQuestions != null ? quizQuestions.length : 0));

            window.printContent("");
            // 创建 ScoreRecord 实例并开始答题
            scoreRecord.displayQuestionsAndScore(quizQuestions, topicReader);

        } while (scoreRecord.askContinue());

        window.bottom();
    }
}
