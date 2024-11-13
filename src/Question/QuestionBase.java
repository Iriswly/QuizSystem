package Question;

import CSVEditor_QuestionSystem.CSVEditor;
import xjtlu.cpt111.assignment.quiz.model.Difficulty;

import java.util.ArrayList;

public class QuestionBase {
    protected CSVEditor csvEditor = new CSVEditor();
    protected final boolean DEBUG = true;

    protected int questionIndex = -1;
    protected String topic = null;
    protected Difficulty difficulty = Difficulty.MEDIUM;
    protected String questionStatement = null;
    protected String option1 = null;
    protected boolean correctness1 = false;
    protected String option2 = null;
    protected boolean correctness2 = false;
    protected String option3 = null;
    protected boolean correctness3 = false;
    protected String option4 = null;
    protected boolean correctness4 = false;

    public QuestionBase() throws Exception {

    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public String getTopic() {
        return topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getQuestionStatement() {
        return questionStatement;
    }

    public String getOption1() {
        return option1;
    }

    public boolean getCorrectness1() {
        return correctness1;
    }

    public String getOption2() {
        return option2;
    }

    public boolean getCorrectness2() {
        return correctness2;
    }

    public String getOption3() {
        return option3;
    }

    public boolean getCorrectness3() {
        return correctness3;
    }

    public String getOption4() {
        return option4;
    }

}
