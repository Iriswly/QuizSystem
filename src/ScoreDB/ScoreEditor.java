package ScoreDB;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ScoreEditor class extends ScoreReader, provides functions to read all scores,
 * insert new records, and update player nicknames ...
 */
public class ScoreEditor extends ScoreReader {
    private List<String> currentLines = new ArrayList<>();

    /**
     * constructor, call super constructor and read all scores
     * @throws Exception throw Exception when reading scores
     */
    public ScoreEditor() throws Exception {
        super();
        readAll();
    }

    /**
     * read all the scores data in the scores.txt
     * if the scores.txt do no exists or error happends during the reading process
     *
     * @return boolean whether the reading operation has been successful
     */
    public boolean readAll() {
        if (currentLines == null) currentLines = new ArrayList<>();
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
     * @param nickname player nickname (unique)
     * @param topic topic
     * @param difficulty level of difficulty
     * @param score score
     * @return boolean whether the insert operation has been successful
     */
    public boolean insertRow(String nickname, String topic, int difficulty, int score) {
        // argument validation test
        readAll();
        if (!isValidInput(nickname) || !isValidInput(topic)) {
            logger.log("Invalid input: nickname or topic contains illegal characters.");
            return false;
        }
        if (difficulty < 1 || difficulty > 4) { // DIFF 1-4
            logger.log("Invalid difficulty: must be between 1 and 10.");
            return false;
        }
        if (score < 0 || score > 100) { // SCORE 0-100
            logger.log("Invalid score: must be between 0 and 100.");
            return false;
        }

        int newId = currentLines.size() + 1;

        String newRow = String.format("%d, %s, %s, %d, %d", newId, nickname, topic, difficulty, score);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILEPATH, true))) {
            bw.write(newRow);
            bw.newLine();
            currentLines.add(newRow);
            logger.log("Inserted new row: " + newRow);
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * change the someone's nick name in the database
     * when the user changes his nickname, please use this method to update the database
     *
     * @param oldNickname old nick name, used to locate the record to be updated
     * @param newNickname new nick name, used to replace the old nick name
     * @return return true if updated successfully, otherwise return false
     */
    public boolean updateNickname(String oldNickname, String newNickname) {
        readAll();
        // 参数合法性检测
        if (!isValidInput(oldNickname) || !isValidInput(newNickname)) {
            logger.log("Invalid input: nickname contains illegal characters.");
            return false;
        }

        boolean updated = false;
        for (int i = 0; i < currentLines.size(); i++) {
            String[] fields = currentLines.get(i).split(",");
            if (fields[1].trim().equals(oldNickname)) {
                fields[1] = newNickname;
                currentLines.set(i, String.join(", ", fields));
                updated = true;
            }
        }

        if (!updated) {
            logger.log("No rows found for old nickname: " + oldNickname);
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILEPATH))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine();
            }
            logger.log("Updated nickname from " + oldNickname + " to " + newNickname);
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * delete the record in the database according to the nickname
     * this function will read all the record in the database,
     * and then traverse these records to find the matching item according to the given nickname and delete it
     * if the nickname is invalid or no matching record is found, the method will return false.
     *
     * @param nickname the nick name to be deleted
     * @return if deleted successfully, return true, otherwise return false
     */
    public boolean deleteByNickname(String nickname) {
        readAll();
        // 参数合法性检测
        if (!isValidInput(nickname)) {
            logger.log("Invalid input: nickname contains illegal characters.");
            return false;
        }

        boolean deleted = false;
        for (int i = 0; i < currentLines.size(); i++) {
            String[] fields = currentLines.get(i).split(",");
            if (fields[1].trim().equals(nickname)) {
                currentLines.remove(i);
                i--; // i -- to go back to the previous item
                deleted = true;
            }
        }

        if (!deleted) {
            logger.log("No rows found for nickname: " + nickname);
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILEPATH))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine();
            }
            logger.log("Deleted rows with nickname: " + nickname);
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * check if the input string is valid
     * the input string is valid if it is not empty, does not contain commas, and does not contain newlines
     *
     * @param input the input string to be checked
     * @return return true if the input string is valid, otherwise return false
     */
    private boolean isValidInput(String input) {
        if (input == null || input.isEmpty()) return false;
        return !input.contains(",") && !input.contains("\n");
    }

    /**
     * simple example of using ScoreEditor
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            ScoreEditor editor = new ScoreEditor();

            // eg1. inserting new line
            System.out.println("inserting new line:");
            if (editor.insertRow("Alice", "Math", 2, 95)) {
                System.out.println("insert newline done!");
            } else {
                System.out.println("insert failed");
            }

            // eg 2. change nick name
            System.out.println("changing nickname:");
            if (editor.updateNickname("Alice", "Alicia")) {
                System.out.println("nickname change done");
            } else {
                System.out.println("nickname change failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
