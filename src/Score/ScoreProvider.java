package Score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * ScoreProvider class responsible for reading user scores from a CSV file and
 * storing them in a 2D array. It can expand the storage dynamically as new
 * data is read.
 */
public class ScoreProvider {

    // Path to the CSV file containing user score data
    private static final String CSV_FILE_PATH = "resources/users.csv";
    // 2D array to store user scores, initially supporting 10 users, each with 10 data points
    private String[][] userScores;
    // Tracks the current number of users (rows) stored in the array
    private int currentCount = 0;

    /**
     * Constructor that initializes the userScores array with an assumed maximum
     * capacity for 10 users and 10 score fields per user.
     */
    public ScoreProvider() {
        userScores = new String[10][10]; // Assumes at most 10 users
    }

    /**
     * Reads the CSV file containing user scores and returns the data in a 2D array.
     * The array dynamically expands if more than the initial capacity is needed.
     *
     * @return A 2D array of user scores where each row represents a user, and
     *         each column represents a score field.
     */
    public String[][] getAllUserScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Reads each line in the CSV file
            while ((line = reader.readLine()) != null) {
                // Checks if the current array capacity is enough
                if (currentCount >= userScores.length) {
                    // Expands the array capacity by 10 rows
                    String[][] newScores = new String[userScores.length + 10][10]; // Expands by 10 rows
                    System.arraycopy(userScores, 0, newScores, 0, userScores.length);
                    // Copies existing data
                    userScores = newScores; // Updates the reference to the new expanded array
                }

                // Splits the line by commas and stores the result in the array
                userScores[currentCount++] = line.split(","); // Increment currentCount after storing
            }
        } catch (IOException e) {
            e.printStackTrace();
            // If an error occurs while reading the file, print the stack trace
        }

        // Creates a result array of exact size based on the number of rows read
        String[][] result = new String[currentCount][10];
        for (int i = 0; i < currentCount; i++) {
            result[i] = userScores[i]; // Copies the data for each row into the result array
        }
        return result; // Returns the 2D array containing all user scores read from the CSV
    }
}