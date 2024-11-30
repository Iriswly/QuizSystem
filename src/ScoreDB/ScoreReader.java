package ScoreDB;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ScoreReader class
 * This class provides methods for reading and managing score data from the scores.csv file
 */
public class ScoreReader extends ScoreDBBase {
    protected List<String> currentLines = new ArrayList<>();
    private final int MAX_COL_NUM = 5; // id, nickname, topic, difficulty, score

    public ScoreReader() throws Exception {
        super();
        readAll();
    }

    /**
     * Reads all data from the scores.csv file into the currentLines list
     * @return Returns true if the read is successful, otherwise returns false
     */
    protected boolean readAll() {
        currentLines = new ArrayList<>();
        if (isScoresCSVExists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILEPATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    currentLines.add(line);
                }
                logger.log("Read all lines from scores.csv");
            } catch (IOException e) {
                if (DEBUG) e.printStackTrace();
                return false;
            }
        } else {
            logger.log("Scores.csv does not exist.");
            return false;
        }
        return true;
    }

    /**
     * Gets an individual's score information based on conditions such as nickname, topic, and difficulty
     * @param nickname The nickname of the player
     * @param topicFilter The topic filter, null for no filter
     * @param difficultyFilter The difficulty filter, null for no filter
     * @param ascending Whether the scores are sorted in ascending order
     * @return Returns a list of score information that meets the conditions
     */
    public List<String> getPersonalScores(
            String nickname,
            String topicFilter,
            Integer difficultyFilter,
            boolean ascending) {
        List<String> results = new ArrayList<>();

        for (String line : currentLines) {
            String[] fields = line.split(",");
            if (fields.length < MAX_COL_NUM) continue;

            String nicknameField = fields[1].trim();
            String topicField = fields[2].trim();
            int difficultyField = Integer.parseInt(fields[3].trim());
            int scoreField = Integer.parseInt(fields[4].trim());

            boolean matches = nicknameField.equals(nickname) &&
                    (topicFilter == null || topicField.equals(topicFilter)) &&
                    (difficultyFilter == null || difficultyField == difficultyFilter);

            if (matches) {
                results.add(line);
            }
        }

        results.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split(",")[4].trim());
            int scoreB = Integer.parseInt(b.split(",")[4].trim());
            return ascending ? Integer.compare(scoreA, scoreB) : Integer.compare(scoreB, scoreA);
        });

//        return results.subList(0, Math.min(limit, results.size()));
        return results;
    }

    /**
     * Gets public score information based on conditions such as topic and difficulty
     * @param topic The topic filter, null for no filter
     * @param difficulty The difficulty filter, null for no filter
     * @param ascending Whether the scores are sorted in ascending order
     * @return Returns a list of score information that meets the conditions
     */
    // sorting and return according to the topic and difficulty, you can specify the limit, ascending rule
    public List<String> getPublicScores(
            String topic,
            Integer difficulty,
            boolean ascending) {
        List<String> results = new ArrayList<>();

        for (String line : currentLines) {
            String[] fields = line.split(",");
            if (fields.length < MAX_COL_NUM) continue;

            String topicField = fields[2].trim();
            int difficultyField = Integer.parseInt(fields[3].trim());
            int scoreField = Integer.parseInt(fields[4].trim());

            boolean matches = (topic == null || topicField.equals(topic)) &&
                    (difficulty == null || difficultyField == difficulty);

            if (matches) {
                results.add(line);
            }
        }

        // sorting
        results.sort((a, b) -> {
            int scoreA = Integer.parseInt(a.split(",")[4].trim());
            int scoreB = Integer.parseInt(b.split(",")[4].trim());
            return ascending ? Integer.compare(scoreA, scoreB) : Integer.compare(scoreB, scoreA);
        });

//        return results.subList(0, Math.min(limit, results.size()));
        return results;
    }

/**
 * Gets all score information
 * @return Returns a list of all score information
 */
public static void main(String[] args) {
    try {
        // Initialize the ScoreReader
        ScoreReader reader = new ScoreReader();

        // Simulate loading data
        reader.currentLines = Arrays.asList(
                "1, Alice, Math, 1,89",
                "2, Bob, Math, 2,90",
                "3, Alice, Science, 1, 95",
                "4, Charlie, Math, 1, 80",
                "5, Alice, Math, 1, 88",
                "6, David, Math, 1, 70",
                "7, Bob, Science, 2, 92",
                "8, Alice, Science, 2, 85",
                "9, Charlie, Science, 1, 78"
        );

        // Example 1: Personal score query - Find all Math scores for Alice, sorted by descending score, with a maximum of 3 records
        System.out.println("Example 1: Alice's Math scores (sorted by descending score)");
        List<String> personalScores1 = reader.getPersonalScores("Alice", "Math", null, false);
        personalScores1.forEach(System.out::println);

        // Example 2: Personal score query - Find all scores for Bob, sorted by ascending score, with a maximum of 5 records
        System.out.println("\nExample 2: All scores for Bob (sorted by ascending score)");
        List<String> personalScores2 = reader.getPersonalScores("Bob", null, null, true);
        personalScores2.forEach(System.out::println);

        // Example 3: Personal score query - Find Alice's scores in Science, level 2, sorted by descending score, with a maximum of 2 records
        System.out.println("\nExample 3: Alice's Science scores (level 2)");
        List<String> personalScores3 = reader.getPersonalScores("Alice", "Science", 2, false);
        personalScores3.forEach(System.out::println);

        // Example 4: Public score query - Find all Math scores, sorted by descending score, with a maximum of 4 records
        System.out.println("\nExample 4: Public Math scores (sorted by descending score)");
        List<String> publicScores1 = reader.getPublicScores("Math", null, false);
        publicScores1.forEach(System.out::println);

        // Example 5: Public score query - Find all Science scores at level 1, sorted by ascending score, with a maximum of 3 records
        System.out.println("\nExample 5: Public Science scores (level 1, sorted by ascending score)");
        List<String> publicScores2 = reader.getPublicScores("Science", 1, true);
        publicScores2.forEach(System.out::println);

        // Example 6: Personal score query - Find all scores for Charlie, sorted by ascending score, with a maximum of all records
        System.out.println("\nExample 6: All scores for Charlie (sorted by ascending score)");
        List<String> personalScores4 = reader.getPersonalScores("Charlie", null, null, true);
        personalScores4.forEach(System.out::println);

        // Example 7: Public score query - Find all Math scores at level 1, sorted by ascending score, with a maximum of 2 records
        System.out.println("\nExample 7: Public Math scores (level 1, sorted by ascending score)");
        List<String> publicScores3 = reader.getPublicScores("Math", 1, true);
        publicScores3.forEach(System.out::println);

        // Example 8: Personal score query - Find David's scores, with no filtering, sorted by descending score, with a maximum of 1 record
        System.out.println("\nExample 8: David's score (sorted by descending score)");
        List<String> personalScores5 = reader.getPersonalScores("David", null, null, false);
        personalScores5.forEach(System.out::println);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
