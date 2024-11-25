package Authenticator;

import User.UserInfo;
import java.util.ArrayList;
import java.util.Scanner;
import Appli.Window;
import Appli.Menu;

public class Register {
    private boolean DEBUG = true;
    private UserInfo user;

    public Register(UserInfo user) throws Exception {
        this.user = user;
    }

    // General method to check for illegal characters
    private boolean containsIllegalCharacters(String input) {
        return input.contains("\n") || input.contains(",") || input.contains("\t") || input.contains("\r") || input.contains("\\");
    }

    private String getUserName(Scanner scanner, Window window) {
        String nickname;
        while (true) {
            window.printContent("Enter your nickname:");
            nickname = scanner.nextLine().trim();

            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0);
            }
            // Check if empty
            if (nickname.isEmpty()) {
                window.printContent("Nickname cannot be empty. Please try again.");
                continue;
            }

            // Check for illegal characters
            if (containsIllegalCharacters(nickname)) {
                window.printContent("Nickname contains illegal characters. Please try again.");
                continue;
            }

            // Check if already exists
            if (user.searchUserLineIndex(nickname) != -1) {
                window.printContent("Nickname already exists. Please choose a different nickname.");
            } else {
                break;
            }
        }
        return nickname;
    }

    // Welcome screen
    public void displayMenu() {
        Window window = new Window();
        Scanner scanner = new Scanner(System.in);

        window.printContent("Welcome! ");
        window.printContent("--------------------------------------------------------------" +
                "-------------------------------------------------------");
        window.printContent("Please choose an option:");
        window.printContent("1. Register a new user");
        window.printContent("2. Log in to an existing user");
        window.printContent("3. Exit the program");
        window.printContent("--------------------------------------------------------------" +
                "-------------------------------------------------------");
        window.printContent("Enter your choice: (1/2/3)");

        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) {
            case "1":
                registerNewUser(scanner);
                break;
            case "2":
                loginUser(scanner);
                break;
            case "3":
                window.printContent("Exiting the program...");
                System.exit(0);
                break;
            default:
                window.printContent("Invalid choice. Please try again.");
                displayMenu();
                break;
        }
    }

    // Register new user screen
    private void registerNewUser(Scanner scanner) {
        Window window = new Window();
        // Nickname
        String nickname;
        while (true) {
            window.printContent("Enter your nickname:(or enter 'x' to exit)");
            nickname = scanner.nextLine().trim();

            // Check if 'x' is entered
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Returning to welcome screen...");
                displayMenu();
                return;
            }

            // Check for illegal characters
            if (containsIllegalCharacters(nickname)) {
                window.printContent("Nickname contains illegal characters. Please try again.");
                continue;
            }

            // Check if empty
            if (nickname.isEmpty()) {
                window.printContent("Nickname cannot be empty. Please try again.");
                continue;
            }
            // Check if already exists
            if (user.searchUserLineIndex(nickname) != -1) {
                window.printContent("Nickname already exists. Please choose a different nickname. (or enter 'x' to exit)");
            } else {
                break;
            }
        }

        // Real name
        String realName;
        while (true) {
            window.printContent("Enter your real name: (or enter 'x' to exit)");
            realName = scanner.nextLine().trim();

            // Check if 'x' is entered
            if (realName.equalsIgnoreCase("x")) {
                window.printContent("Returning to welcome screen...");
                displayMenu();
                return;
            }

            // Check if empty
            if (realName.isEmpty()) {
                window.printContent("Real name cannot be empty. Please try again.");
                continue;
            }

            // Check for illegal characters
            if (containsIllegalCharacters(realName)) {
                window.printContent("Real name contains illegal characters. Please try again.");
                continue;
            } else {
                break;
            }
        }

        // Password
        String password;
        while (true) {
            window.printContent("Create a password (8-16 characters):(or enter 'x' to exit)");
            password = scanner.nextLine().trim();

            // Password must be between 8 and 16 characters
            if (password.length() < 8 || password.length() > 16) {
                window.printContent("Password must be between 8 and 16 characters. Please try again.");
                continue;
            }

            // Check if 'x' is entered
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // Exit the program
            }

            // Check if empty
            if (password.isEmpty()) {
                window.printContent("Password cannot be empty. Please try again.");
                continue;
            }

            // Check for illegal characters
            if (containsIllegalCharacters(password)) {
                window.printContent("Password contains illegal characters. Please try again.");
                continue;
            }

            int passwordScore = calculatePasswordScore(password);

            if (passwordScore < 50) {
                window.printContent("Password security level is low. Please set a stronger password. (or enter 'x' to exit)");
            } else if (passwordScore <= 75) {
                window.printContent("Password security level is good. Password accepted.");
                break;
            } else {
                window.printContent("Password security level is excellent. Password accepted.");
                break;
            }
        }

        if (Register(nickname, realName, password)) {
            window.printContent("Registration successful! Please log in. (or enter 'x' to exit)");
            loginUser(scanner);
        } else {
            window.printContent("Registration failed. Please try again. (or enter 'x' to exit)");
            displayMenu();
        }
    }

    // Login screen
    private void loginUser(Scanner scanner) {
        Window window = new Window();

        // Enter nickname first
        window.printContent("Enter your nickname: (or enter 'x' to exit)");
        String nickname = scanner.nextLine().trim();

        // Check if 'x' is entered
        if (nickname.equalsIgnoreCase("x")) {
            window.printContent("Returning to welcome screen...");
            displayMenu();
            return;
        }

        // Check if empty
        if (nickname.isEmpty()) {
            window.printContent("Nickname cannot be empty. Please try again.");
            loginUser(scanner);
            return;
        }

        // Allow user to re-enter password until successful login or choose to exit
        while (true) {
            window.printContent("Enter your password: (or enter 'x' to exit)");
            String password = scanner.nextLine().trim();

            // Check if 'x' is entered
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // Exit the program
            }

            // Check if empty
            if (password.isEmpty()) {
                window.printContent("Password cannot be empty. Please try again.");
                continue;
            }

            if (user.Login(nickname, password)) {
                window.printContent("Login successful!");
                // Login status is true
                window.bottom();
                break; // Successful login, exit loop
            } else {
                window.printContent("Login failed. Please try again. (or enter 'x' to exit)");
                window.printContent("Do you want to try password again? (Yes/No) (or enter 'x' to exit)");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("no")) {
                    window.printContent("Returning to main menu...");
                    displayMenu(); // Return to main menu
                    break; // Exit loop
                }
            }
        }
    }

    public boolean Register(String nickname, String realName, String password) {
        Window window = new Window();

        // Check if nickname already exists
        if (user.searchUserLineIndex(nickname) != -1) {
            if (DEBUG) window.printContent("Nickname already exists");
            return false;
        }

        // Password length requirement 8-16 characters
        if (password.length() < 8 || password.length() > 16) {
            if (DEBUG) window.printContent("Password must be between 8 and 16 characters");
            return false;
        }

        // Create new user profile
        ArrayList<String> newProfile = new ArrayList<>();
        newProfile.add(nickname);
        newProfile.add(realName);
        newProfile.add(password);

        newProfile.add("-1");
        newProfile.add("_");
        newProfile.add("-1");
        newProfile.add("_");
        newProfile.add("-1");
        newProfile.add("_");
        newProfile.add("-1");

        if (user.addAccount_DB(newProfile)) {
            if (DEBUG) window.printContent("User registered successfully");
            return true;
        } else {
            if (DEBUG) window.printContent("User registration failed");
            return false;
        }
    }

    // Calculate password strength
    private int calculatePasswordScore(String password) {
        int score = 0;
        boolean hasLower = false, // Has lowercase letters
                hasUpper = false, // Has uppercase letters
                hasDigit = false, // Has digits
                hasSpecial = false; // Has special characters
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c) && !hasLower) {
                score += 5;
                hasLower = true;
            } else if (Character.isUpperCase(c) && !hasUpper) {
                score += 8;
                hasUpper = true;
            } else if (Character.isDigit(c) && !hasDigit) {
                score += 5;
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c) && !hasSpecial) {
                score += 8;
                hasSpecial = true;
            }
        }
        return Math.min(score, 25) * 4; // Each item has a maximum score of 25, total score is 100
    }

    public static void main(String[] args) throws Exception {
        UserInfo user = new UserInfo();
        try {
            Register register = new Register(user);
            register.displayMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
