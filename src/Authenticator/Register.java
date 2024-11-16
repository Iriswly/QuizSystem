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
    private String getUserName(Scanner scanner, Window window) {
        String nickname;
        while (true) {
            window.printContent("Enter your nickname:");
            nickname = scanner.nextLine().trim();

            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0);
            }

            if (user.searchUserLineIndex(nickname) != -1) {
                window.printContent("Nickname already exists. Please choose a different nickname.");
            } else {
                break;
            }
        }
        return nickname;
    }

    //注册或登陆界面
    public void displayMenu() {
        Window window = new Window();
        Scanner scanner = new Scanner(System.in);

        window.printContent("Welcome! \nAre you a new user? (Yes/No) " +
                "\nEnter 'x' to exit the program if you want.");
        String choice = scanner.nextLine().trim().toLowerCase();

        // 检查是否输入 x 或 exit
        if (choice.equals("x") || choice.equals("exit")) {
            window.printContent("Exiting the program...");
            System.exit(0); // 退出程序
        }

        if (choice.equals("yes")) {
            registerNewUser(scanner);
        } else if (choice.equals("no")) {
            loginUser(scanner);
        } else {
            window.printContent("Invalid choice. Please try again.");
            displayMenu();
        }
    }

    // 注册新用户界面
    private void registerNewUser(Scanner scanner) {
        Window window = new Window();
        //昵称
        String nickname;
        while (true) {
            window.printContent("Enter your nickname:");
            nickname = scanner.nextLine();

            // 检查是否输入 x
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // 退出程序
            }

            if (user.searchUserLineIndex(nickname) != -1) {
                window.printContent("Nickname already exists. Please choose a different nickname.");
            }else {
                break;
            }
        }

        //真名
        window.printContent("Enter your real name:");
        String realName = scanner.nextLine();

// 检查是否输入 x
        if (nickname.equalsIgnoreCase("x")) {
            window.printContent("Exiting the program...");
            System.exit(0); // 退出程序
        }
        //密码
        String password;
        while (true) {
            window.printContent("Create a password (8-16 characters):");
            password = scanner.nextLine();

            // 检查是否输入 x
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // 退出程序
            }

            int passwordScore = calculatePasswordScore(password);
            if (passwordScore < 50) {
                window.printContent("Password security level is low. Please set a stronger password.");
            } else if (passwordScore <= 75) {
                window.printContent("Password security level is good. Password accepted.");
                break;
            } else {
                window.printContent("Password security level is excellent. Password accepted.");
                break;
            }
        }


        if (Register(nickname, realName, password)) {
            window.printContent("Registration successful! Please log in.");
            loginUser(scanner);
        } else {
            window.printContent("Registration failed. Please try again.");
            displayMenu();
        }
    }

    // 登陆界面
    private void loginUser(Scanner scanner) {
        Window window = new Window();

        // 先输入昵称
        window.printContent("Enter your nickname:");
        String nickname = scanner.nextLine();

        // 检查是否输入 x
        if (nickname.equalsIgnoreCase("x")) {
            window.printContent("Exiting the program...");
            System.exit(0); // 退出程序
        }


        // 允许用户重复输入密码，直到成功登录或选择退出
        while (true) {
            window.printContent("Enter your password:");
            String password = scanner.nextLine();

            // 检查是否输入 x
            if (nickname.equalsIgnoreCase("x")) {
                window.printContent("Exiting the program...");
                System.exit(0); // 退出程序
            }

            if (user.Login(nickname, password)) {
                window.printContent("Login successful!");
                // 登录状态为 true
                window.bottom();
                break; // 成功登录，退出循环

            } else {
                window.printContent("Login failed. Please try again.");
                window.printContent("Do you want to try password again? (Yes/No)");
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

    // 注销用户界面
    private void deleteUser(Scanner scanner) {
        Window window = new Window();
        window.printContent("Enter your nickname:");
        String nickname = scanner.nextLine();

        window.printContent("Enter your password:");
        String password = scanner.nextLine();

        if (user.Login(nickname, password)) {
            if (user.deleteAccount(nickname)) {
                window.printContent("Account deleted successfully.");
            } else {
                window.printContent("Account deletion failed.");
            }
        } else {
            window.printContent("Invalid credentials. Account deletion failed.");
        }
        displayMenu();
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

    /*int searchResult = searchUserLineIndex(nickname);
        if (searchResult == -1) {
            if (DEBUG) System.out.println("Account not found");
            return false;
        }
        ArrayList<String> profile = getProfile(searchResult);
        if (profile == null) {
            if (DEBUG) System.out.println("Profile not found");
            return false;
        }
        if (DEBUG) System.out.println("Profile: " + profile);
        if (password.equals(profile.get(2))) {
            if (!loginSetter(profile)) {
                if (DEBUG) System.out.println("Login failed");
                return false;
            }
            return isLogin();
        } else {
            // TODO: UI / MENU interface needed here
            return false;
        }
    }*/
}
