package Appli;

import Authenticator.Register;
import User.UserInfo;

public class QuizSystemAppli {

    public static void main(String[] args) throws Exception {
        UserInfo user = new UserInfo();
        Window window = new Window();
        Menu menu = new Menu(user);

        // 初始化
        menu.initializeMenu();

        // 注册登录部分
        menu.unloggedMenu();
        try {
            Register register = new Register(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 主循环
        while (true) {
            menu.mainMenu(); // 每次循环都展示主菜单
            String option = menu.getSelectedOption(); // 获取用户选择的选项

            System.out.println("Debug: Selected Option = " + option);

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
