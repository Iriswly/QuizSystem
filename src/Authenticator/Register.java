package Authenticator;

import User.UserInfo;
import java.util.ArrayList;
import java.util.Scanner;
import Appli.Window;
import Appli.Menu;



public class Register{
    private boolean DEBUG = true;
    private UserInfo user;

    public Register(UserInfo user) throws Exception {
        this.user = user;
    }

    // 通用非法字符检查方法
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
            // 检查是否为空
            if (nickname.isEmpty()) {
                window.printContent("Nickname cannot be empty. Please try again.");
                continue;
            }

            // 检查是否包含非法字符
            if (containsIllegalCharacters(nickname)) {
                window.printContent("Nickname contains illegal characters. Please try again.");
                continue;
            }

            // 检查是否已存在
            if (user.searchUserLineIndex(nickname) != -1) {
                window.printContent("Nickname already exists. Please choose a different nickname.");
            } else {
                break;
            }
        }
        return nickname;
    }





    //welcome界面
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






    // 注册新用户界面
    private void registerNewUser(Scanner scanner) {
        Window window = new Window();
        //昵称
        String nickname;
        while (true) {
            window.printContent("Enter your nickname:(or enter 'x' to exit)");
            nickname = scanner.nextLine().trim();

            // 检查是否输入 x
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Returning to welcome screen...");
                displayMenu();
                return;
            }

            //检查非法字符
            if (containsIllegalCharacters(nickname)) {
                window.printContent("Nickname contains illegal characters. Please try again.");
                continue;
            }

            // 检查是否为空
            if (nickname.isEmpty()) {
                window.printContent("Nickname cannot be empty. Please try again.");
                continue;
            }
            // 检查是否已存在
            if (user.searchUserLineIndex(nickname) != -1) {
                window.printContent("Nickname already exists. Please choose a different nickname. (or enter 'x' to exit)");
            }else {
                break;
            }
        }

        //真名
        String realName;
        while (true) {
            window.printContent("Enter your real name: (or enter 'x' to exit)");
            realName = scanner.nextLine().trim();

            // 检查是否输入 x
            if (realName.equalsIgnoreCase("x")) {
                window.printContent("Returning to welcome screen...");
                displayMenu();
                return;
            }

            // 检查是否为空
            if (realName.isEmpty()) {
                window.printContent("Real name cannot be empty. Please try again.");
                continue;
            }

            //检查非法字符
            if (containsIllegalCharacters(realName)) {
                window.printContent("Real name contains illegal characters. Please try again.");
                continue;
            }else {
                break;
            }
        }


        //密码
        String password;
        while (true) {
            window.printContent("Create a password (8-16 characters):(or enter 'x' to exit)");
            password = scanner.nextLine().trim();

            //密码在8-16位之间
            if (password.length() < 8 || password.length() > 16) {
                window.printContent("Password must be between 8 and 16 characters. Please try again.");
                continue;
            }

            // 检查是否输入 x
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // 退出程序
            }

            // 检查是否为空
            if (password.isEmpty()) {
                window.printContent("Password cannot be empty. Please try again.");
                continue;
            }

            //检查非法字符
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








    // 登陆界面
    private void loginUser(Scanner scanner) {
        Window window = new Window();

        // 先输入昵称
        window.printContent("Enter your nickname: (or enter 'x' to exit)");
        String nickname = scanner.nextLine().trim();

        // 检查是否输入 x
        if (nickname.equalsIgnoreCase("x")) {
            window.printContent("Returning to welcome screen...");
            displayMenu();
            return;
        }

         // 检查是否为空
        if (nickname.isEmpty()) {
            window.printContent("Nickname cannot be empty. Please try again.");
            loginUser(scanner);
            return;
        }

        // 允许用户重复输入密码，直到成功登录或选择退出
        while (true) {
            window.printContent("Enter your password: (or enter 'x' to exit)");
            String password = scanner.nextLine().trim();

            // 检查是否输入 x
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // 退出程序
            }

            // 检查是否为空
            if (password.isEmpty()) {
                window.printContent("Password cannot be empty. Please try again.");
                continue;
            }


            if (user.Login(nickname, password)) {
                window.printContent("Login successful!");
                // 登录状态为 true
                window.bottom();
                break; // 成功登录，退出循环
            } else {
                window.printContent("Login failed. Please try again. (or enter 'x' to exit)");
                window.printContent("Do you want to try password again? (Yes/No) (or enter 'x' to exit)");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (choice.equals("no")) {
                    window.printContent("Returning to main menu...");
                    displayMenu(); // 返回主菜单
                    break; // 退出循环
                }
            }
        }
    }






    public boolean Register(String nickname,
                            String realName,
                            String password) {
        Window window = new Window();

        //是否有同昵称
        if (user.searchUserLineIndex(nickname) != -1) {
            if (DEBUG) window.printContent("Nickname already exists");
            return false;
        }

        // 密码长度要求8-16位
        if (password.length() < 8 || password.length() > 16) {
            if (DEBUG) window.printContent("Password must be between 8 and 16 characters");
            return false;
        }

       //创建新用户资料
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

    // 计算密码强度
    private int calculatePasswordScore(String password) {
        int score = 0;
        boolean hasLower = false,//是否有小写字母
                hasUpper = false,//是否有大写字母
                hasDigit = false, //是否有数字
                hasSpecial = false;//是否有特殊字符
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
        return Math.min(score, 25) * 4; //每一项的最大得分为25，总分为100分
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
