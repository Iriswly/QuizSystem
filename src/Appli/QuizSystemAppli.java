package Appli;

import Authenticator.Register;
import User.UserInfo;

public class QuizSystemAppli {

    public static void main(String[] args) throws Exception {
        UserInfo user = new UserInfo();
        Window window = new Window();
        Menu menu = new Menu(user);

        // Initialization
        menu.initializeMenu();

        // Registration and login section
        menu.unloggedMenu();
        try {
            Register register = new Register(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main loop
        while (true) {
            menu.mainMenu(); // Display the main menu on each iteration
            String option = menu.getSelectedOption(); // Get the user's selected option

            switch (option) {
                case "Quiz": {
                    menu.quizMenu();
                    break;
                }
                case "Insert Question": {
                    menu.insertQuestionMenu();
                    break;
                }
                case "Ranking": {
                    menu.rankingMenu();
                    break;
                }
                case "Logout": {
                    menu.logOutMenu();
                    break;
                }
                case "Account Management": {
                    menu.accountManagementMenu();
                    break;
                }
                default: {
                    window.printContent("Invalid option selected. Please try again.");
                    break;
                }
            }
        }
    }
}
