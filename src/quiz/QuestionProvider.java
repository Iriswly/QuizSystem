package quiz;

import CSVEditor_QuestionSystem.CSVBase;
import xjtlu.cpt111.assignment.quiz.model.Question;
import xjtlu.cpt111.assignment.quiz.model.Option;

import Question.InsertQuestion;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class QuestionProvider {

    private String[][] questionStorage;
    private int questionCount = 0;
    private int capacity = 10; // Fixed capacity of 10, can be expanded later
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

    // Add a new question
    public void addQuestion(Question question) {
        try {
            // Check if storage needs to be expanded
            if (questionCount >= capacity) {
                increaseCapacity(); // Expand capacity
            }
            // Use InsertQuestion for insertion, which handles duplication checks
            new InsertQuestion(question, questionCount); // Directly call InsertQuestion

            // Store the question in memory
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
            // Handle the case where question insertion fails, e.g., duplicate question
            System.out.println("Failed to add question: " + e.getMessage());
        }
    }

    // Increase the capacity of the question storage
    private void increaseCapacity() {
        capacity += 10;
        String[][] newQuestionStorage = new String[capacity][12];
        // Copy old array
        for (int i = 0; i < questionCount; i++) {
            newQuestionStorage[i] = questionStorage[i];
        }
        questionStorage = newQuestionStorage; // Update storage
    }

    // Get selected questions based on the topic
    public String[][] getSelectedQuestions(String selectedTopic) {
        String[][] selectedQuestions = new String[capacity][12];
        int sameTopicCount = 0;

        String filePath = getCSVFilePath(selectedTopic);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].equals(selectedTopic)) {
                    // Check if selectedQuestions array needs to be expanded
                    if (sameTopicCount >= selectedQuestions.length) {
                        // Create a new array with increased size
                        String[][] newSelectedQuestions = new String[selectedQuestions.length + 10][12]; // Increase by 10 each time
                        System.arraycopy(selectedQuestions, 0, newSelectedQuestions, 0, selectedQuestions.length);
                        selectedQuestions = newSelectedQuestions; // Update reference
                    }
                    selectedQuestions[sameTopicCount++] = parts;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the return array
        String[][] sameTopicQuestions = new String[sameTopicCount][];
        System.arraycopy(selectedQuestions, 0, sameTopicQuestions, 0, sameTopicCount);
        return sameTopicQuestions;
    }

    // Get the file path for the corresponding topic
    private String getCSVFilePath(String topic) {
        switch (topic) {
            case "mathematics": return CSVBase.FILEPATH_MATHEMATICS;
            case "psychology": return CSVBase.FILEPATH_PSYCHOLOGY;
            case "astronomy": return CSVBase.FILEPATH_ASTRONOMY;
            case "geography": return CSVBase.FILEPATH_GEOGRAPHY;
            default: return CSVBase.FILEPATH_NEW + "/" + topic + ".csv"; // Default path
        }
    }

    // Get questions of the same topic and renumber them
    public String[][] getSameTopicQuestions(String topicToSelect) {
        // Count the number of questions with the same topic
        int count = 0;
        for (int i = 0; i < questionCount; i++) {
            if (questionStorage[i][1].equals(topicToSelect)) {
                count++;
            }
        }

        // Create a new 2D array for storing renumbered questions
        String[][] sameTopicQuestions = new String[count][questionStorage[0].length];
        int newIndex = 0;

        for (int i = 0; i < questionCount; i++) {
            if (questionStorage[i][1].equals(topicToSelect)) {
                // Renumber the questions
                sameTopicQuestions[newIndex][0] = String.valueOf(newIndex); // New numbering
                // Copy other columns
                for (int j = 1; j < questionStorage[i].length; j++) {
                    sameTopicQuestions[newIndex][j] = questionStorage[i][j];
                }
                newIndex++;
            }
        }

        return sameTopicQuestions;
    }
}
