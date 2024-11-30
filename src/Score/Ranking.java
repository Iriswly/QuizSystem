package Score;

import ScoreDB.ScoreReader;

import java.util.List;
import java.util.Scanner;

import Appli.Window;

public class Ranking {

    private final ScoreReader scoreReader;
    Window window = new Window();

    public Ranking(ScoreReader scoreReader) {
        this.scoreReader = scoreReader;
    }

    public void displayRanking() {
        Scanner sc = new Scanner(System.in);

        String[] topics = {"mathematics", "psychology", "astronomy", "geography"};
        String[] difficulties = {"easy", "medium", "hard", "very_hard"};

        //User selects the topic they want to search
        String topic = null;
        while (topic == null) {
            window.printContent("Select a topic:");
            for (int i = 0; i < topics.length; i++) {
                window.printContent((i + 1) + ". " + topics[i]);
            }
            window.printContent("Enter the topic name or number: ");

            if (sc.hasNextInt()) {
                int topicIndex = sc.nextInt();
                sc.nextLine();
                if (topicIndex >= 1 && topicIndex <= topics.length) {
                    topic = topics[topicIndex - 1];
                } else {
                    window.printContent("Invalid number. Please choose a valid option.");
                }
            } else {
                String inputTopic = sc.nextLine();
                for (String t : topics) {
                    if (t.equalsIgnoreCase(inputTopic)) {
                        topic = t;
                        break;
                    }
                }
                if (topic == null) {
                    window.printContent("Invalid input. Please enter a valid topic name or number.");
                }
            }
        }

        //User selects a difficulty
        int difficulty = 0;
        while (difficulty == 0) {
            window.printContent("Select a difficulty:");
            for (int i = 0; i < difficulties.length; i++) {
                window.printContent((i + 1) + ". " + difficulties[i]);
            }
            window.printContent("Enter the difficulty name or number: ");

            if (sc.hasNextInt()) {
                int difficultyIndex = sc.nextInt();
                sc.nextLine();
                if (difficultyIndex >= 1 && difficultyIndex <= difficulties.length) {
                    difficulty = difficultyIndex;
                } else {
                    window.printContent("Invalid number. Please choose a valid option.");
                }
            } else {
                String inputDifficulty = sc.nextLine().toLowerCase();
                for (int i = 0; i < difficulties.length; i++) {
                    if (difficulties[i].equalsIgnoreCase(inputDifficulty)) {
                        difficulty = i + 1;
                        break;
                    }
                }
                if (difficulty == 0) {
                    window.printContent("Invalid input. Please enter a valid difficulty name or number.");
                }
            }
        }


        //Call getPublicScore to retrieve the leaderboard data
        List<String> rankchart = scoreReader.getPublicScores(topic, difficulty, false);

        //Display the ranking
        window.printContent("");
        window.printContent("       --- Ranking for Topic: " + topic + ", Difficulty: " + difficulties[difficulty - 1] + " ---");

        if (rankchart == null || rankchart.isEmpty()) {
            window.printContent("No scores available for the selected topic and difficulty.");

        } else {
            window.printContent("");
            String row_head = "          ";
            row_head += String.format("%-20s", "Rank");
            row_head += String.format("%-18s", "User");
            row_head += String.format("%-17s", "Score");
            window.printContent(row_head);

            int rankIndex = 1;
            int lastScore = Integer.parseInt(rankchart.get(0).split(",")[4].trim());

            for (String line : rankchart) {
                //Split the line by commas into an array
                String[] fields = line.split(",");

                //Get the 2nd, 3rd, and 5th elements
                String name = fields[1].trim(); //3rd element (index 2)
                String score = fields[4].trim(); //5th element (index 4)
                int _score = Integer.parseInt(score);
                rankIndex = (lastScore == _score) ? rankIndex : rankIndex + 1;
                lastScore = _score;


                //Output the elements
                String row_content = "          ";

                row_content += String.format("%-20s", rankIndex);
                row_content += String.format("%-18s", name);
                row_content += String.format("%-17s", score);
                window.printContent(row_content);
            }
            window.printContent("");
            window.printContent("");
            window.printContent("Enter 'x' to return to Main Menu");
            while (true) {
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("x")) {break;}
            }
        }
    }

    public static void main(String[] args) {
        try {
            //Create an instance of ScoreReader to read data
            ScoreReader scoreReader = new ScoreReader();

            //Create an instance of Ranking
            Ranking ranking = new Ranking(scoreReader);

            //Call the displayRanking method to display the ranking
            ranking.displayRanking();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





