package quiz;

import Appli.Menu;
import java.util.Scanner;
import Appli.Window;

public class TopicReader {
    private Menu menu = new Menu();

    private String topicToSelect;   // User-selected topic
    private final String[] difficulties = {"EASY", "MEDIUM", "HARD", "VERY_HARD"}; // Difficulty levels
    private String difficultyToSelect;   // User-selected difficulty
    private final String[] topicArray = {"mathematics", "psychology", "astronomy", "geography"};
    private final int[] index = {1, 2, 3, 4};
    boolean quitInstantlyTopic = false; // Flag to quit topic selection
    boolean quitInstantlyDifficulty = false; // Flag to quit difficulty selection

    public TopicReader() {
        // Initialization constructor
    }

    // Display topic selection
    public void showTopic() {
        Window window = new Window();
        // Determine the maximum length of the topics
        int maxLength = 0;
        for (String topic : topicArray) {
            if (topic.length() > maxLength) {
                maxLength = topic.length();
            }
        }

        // Upper border
        String upperBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            upperBorder += "-";
        }
        upperBorder += "------";
        window.printContent(upperBorder); // Output upper border

        for (int i = 0; i < topicArray.length; i++) {
            String line = "      |  " + index[i] + "  |  " + topicArray[i];

            // Output padding spaces for alignment
            int spacesToFill = maxLength - topicArray[i].length();
            for (int j = 0; j < spacesToFill; j++) {
                line += " "; // Fill spaces
            }
            line += "        |"; // Add ending border
            window.printContent(line);
        }

        // Lower border
        String lowerBorder = "      ------------";
        for (int i = 0; i < maxLength; i++) {
            lowerBorder += "-";
        }
        lowerBorder += "------";
        window.printContent(lowerBorder); // Output lower border
        window.printContent("");
    }

    // Select a topic
    public void selectTopic() {
        Window window = new Window();
        window.printContent("Please enter the topic you would like to choose: (or 'x' to exit)");
        window.printContent("    Either type in an index (Integer) or type in a topic name (String):");
        Scanner sc = new Scanner(System.in);

        while (topicToSelect == null) {
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("X")) {
                quitInstantlyTopic = true;
                window.bottom();
                return;
            }

            // Check if input is a valid integer index
            int inputIndex = -1;
            boolean isIndex = true;

            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    isIndex = false;
                    break;
                }
            }

            if (isIndex) {
                inputIndex = Integer.parseInt(input);
                // Check if input index is valid
                if (inputIndex >= 1 && inputIndex <= index.length) {
                    topicToSelect = topicArray[inputIndex - 1]; // Index starts from 1, array starts from 0
                } else {
                    window.printContent("Please enter a valid index or topic name:");
                }
            } else {
                // Check if the input topic is valid
                boolean foundTopic = false;
                for (String topic : topicArray) {
                    if (topic.equalsIgnoreCase(input)) {
                        topicToSelect = input;
                        foundTopic = true;
                        break;
                    }
                }
                if (!foundTopic) {
                    window.printContent("Please enter a valid index or topic name:");
                }
            }
        }

        window.printContent("Selected successfully!");
        window.printContent("Your topic is: " + topicToSelect);
        window.printContent("");
    }

    public boolean getQuitInstantlyForTopic() {
        return quitInstantlyTopic;
    }

    // Select difficulty
    public void selectDifficulty() {
        Window window = new Window();
        window.printContent("Please select a difficulty level:");
        for (int i = 0; i < difficulties.length; i++) {
            window.printContent((i + 1) + ". " + difficulties[i]);
        }

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (difficultyToSelect == null) {
            window.printContent("Enter difficulty index (1-4): (or 'x' to return to Main Menu)");

            String input = sc.nextLine();

            if (input.equalsIgnoreCase("X")) {
                quitInstantlyDifficulty = true;
                window.bottom();
                return;
            }

            boolean isIndex = true;
            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    isIndex = false;
                    break;
                }
            }

            if (isIndex) {
                choice = Integer.parseInt(input); // Convert to integer if it is a number

                // Check if the input number is within the valid range
                if (choice >= 1 && choice <= difficulties.length) {
                    difficultyToSelect = difficulties[choice - 1]; // Set selected difficulty
                    window.printContent("Selected successfully!");
                    window.printContent("Your difficulty is: " + difficultyToSelect);
                    window.printContent("");
                } else {
                    window.printContent("Invalid choice. Please enter a number between 1 and " + difficulties.length + ".");
                }
            } else {
                window.printContent("Invalid input. Please enter a valid number.");
            }
        }
    }

    public boolean getQuitInstantlyForDifficulty() {
        return quitInstantlyDifficulty;
    }

    // Get the selected difficulty
    public String getDifficultyToSelect() {
        return difficultyToSelect;
    }

    // Get the selected topic
    public String getTopicToSelect() {
        return this.topicToSelect;
    }

    // Filter questions by difficulty based on the selected topic
    public static String[][] QuestionsByDifficulty(String[][] questions, String Difficulty) {
        // Ensure the questions array is not empty
        if (questions == null || questions.length == 0) {
            return new String[0][0]; // Return an empty array
        }

        int count = 0;
        // Traverse the questions array to count questions that match the criteria
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(Difficulty)) {
                count++;
            }
        }

        // Create an array for questions that match the criteria
        String[][] Questionsbydifficulty = new String[count][];
        int index = 0;

        // Traverse the questions again to fill the matching questions
        for (int i = 0; i < questions.length; i++) {
            if (questions[i][2].equals(Difficulty)) {
                Questionsbydifficulty[index++] = questions[i];
            }
        }

        return Questionsbydifficulty;
    }

    // Randomly select questions from a specific difficulty pool
    private static String[][] selectRandomlyFromCategory(String[][] questions, int numQuestions, String[][] selectedQuestions, int startIndex) {
        for (int i = 0; i < numQuestions; i++) {
            if (questions.length == 0) break;  // Prevent selecting from an insufficient pool of questions

            // Use Math.random() to generate a random number
            int randomIndex = (int) (Math.random() * questions.length);

            // Ensure the selected question is not a duplicate
            while (questions[randomIndex] == null) {
                randomIndex = (int) (Math.random() * questions.length);
            }

            selectedQuestions[startIndex + i] = questions[randomIndex];
            questions[randomIndex] = null; // Mark this question as selected to prevent duplication
        }

        return selectedQuestions;
    }

    // Randomly select questions based on user-selected difficulty
    public static String[][] QuizQuestion(String[][] questions, int numQuestions, String difficulty) {
        // Determine proportions based on difficulty
        double easyPercentage = 0, mediumPercentage = 0, hardPercentage = 0, veryHardPercentage = 0;

        switch (difficulty) {
            case "EASY":
                easyPercentage = 0.6;
                mediumPercentage = 0.3;
                hardPercentage = 0.1;
                break;
            case "MEDIUM":
                easyPercentage = 0.4;
                mediumPercentage = 0.4;
                hardPercentage = 0.2;
                break;
            case "HARD":
                easyPercentage = 0.3;
                mediumPercentage = 0.3;
                hardPercentage = 0.4;
                break;
            case "VERY_HARD":
                mediumPercentage = 0.3;
                hardPercentage = 0.3;
                veryHardPercentage = 0.4;
                break;
        }

        // Calculate the number of questions needed for each difficulty
        int easyCount = (int) (numQuestions * easyPercentage);
        int mediumCount = (int) (numQuestions * mediumPercentage);
        int hardCount = (int) (numQuestions * hardPercentage);
        int veryHardCount = (int) (numQuestions * veryHardPercentage);

        // Filter questions by difficulty
        String[][] easyQuestions = QuestionsByDifficulty(questions, "EASY");
        String[][] mediumQuestions = QuestionsByDifficulty(questions, "MEDIUM");
        String[][] hardQuestions = QuestionsByDifficulty(questions, "HARD");
        String[][] veryHardQuestions = QuestionsByDifficulty(questions, "VERY_HARD");

        // Select questions proportionally from each difficulty
        String[][] quizquestions = new String[numQuestions][12];
        int index = 0;
        quizquestions = selectRandomlyFromCategory(easyQuestions, easyCount, quizquestions, index);
        index += easyCount;
        quizquestions = selectRandomlyFromCategory(mediumQuestions, mediumCount, quizquestions, index);
        index += mediumCount;
        quizquestions = selectRandomlyFromCategory(hardQuestions, hardCount, quizquestions, index);
        index += hardCount;
        quizquestions = selectRandomlyFromCategory(veryHardQuestions, veryHardCount, quizquestions, index);

        return quizquestions;
    }

    public static void main(String[] args) {
        // Create an instance of QuestionProvider
        QuestionProvider questionProvider = new QuestionProvider();

        // Display topic selection
        TopicReader topicReader = new TopicReader();
        topicReader.showTopic();
        topicReader.selectTopic();
        topicReader.selectDifficulty();

        // Get questions based on the selected topic
        String selectedTopic = topicReader.getTopicToSelect();
        String[][] selectedQuestions = questionProvider.getSelectedQuestions(selectedTopic);

        System.out.println("Number of selected questions: " + (selectedQuestions != null ? selectedQuestions.length : 0));

        if (selectedQuestions != null && selectedQuestions.length > 0) {
            // Display all questions and their options
            for (int row = 0; row < selectedQuestions.length; row++) {
                if (selectedQuestions[row] != null) { // Check for non-null rows
                    System.out.println("Question " + (row + 1) + ": " + selectedQuestions[row][3]); // Display question content
                    for (int col = 0; col < selectedQuestions[row].length; col++) {
                        System.out.println("selectedQuestions[" + row + "][" + col + "]: " + selectedQuestions[row][col]);
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("No questions found for the selected topic.");
        }
    }
}
