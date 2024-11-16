package Appli;

import Authenticator.Register;
import Question.InsertQuestion;
import quiz.TopicReader;
import quiz.QuestionProvider;
import Score.ScoreRecord;
import User.UserInfo;

public class QuizSystemAppli {

    public static void main(String[] args) throws Exception {
        Window window = new Window(120, 20, "  Quiz System");
        UserInfo user = new UserInfo();
        Menu menu = new Menu(user);

        // 注册登录部分
        menu.unloggedMenu();
        try {
            Register register = new Register(user);
            menu.mainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 主循环
        while (true) {
            menu.mainMenu(); // 每次循环都展示主菜单
            String option = menu.getSelectedOption(); // 获取用户选择的选项

            switch (option) {
                case "Quiz": {
                    menu.QuizMenu();
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
                    menu.unloggedMenu();
                    break;
                }
                default: {
                    System.out.println("Invalid option selected. Please try again.");
                    break;
                }
            }

            menu.clearSelectedOption(); // 清空选项
        }
    }
}
