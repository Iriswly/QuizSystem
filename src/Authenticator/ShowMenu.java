package Authenticator;

import User.UserBase;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowMenu extends UserBase {
    private final Scanner scanner;

    public ShowMenu() throws Exception {
        super();
        scanner = new Scanner(System.in);
    }

    // 主界面
    public void displayMenu() {
        System.out.println("Welcome! Are you a new user? (yes/no):");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes") || choice.equals("y")) {
            registerNewUser(); // 进入注册界面
        } else if (choice.equals("no") || choice.equals("n")) {
            loginUser(); // 进入登录界面
        } else {
            System.out.println("Invalid choice. Please try again.");
            displayMenu();
        }
    }

    // 注册新用户界面
    private void registerNewUser() {
        String nickname = getUniqueNickname();
        String realName = getRealName();
        String password = getPassword();

        // 注册用户
        if (register(nickname, realName, password)) {
            System.out.println("Registration successful! Please log in.");
            loginUser(); // 进入登录界面
        } else {
            System.out.println("Registration failed. Please try again.");
            displayMenu();
        }
    }

    // 获取并验证唯一的昵称
    private String getUniqueNickname() {
        String nickname;
        while (true) {
            System.out.println("Enter your nickname:");
            nickname = scanner.nextLine().trim();
            if (nickname.isEmpty()) {
                System.out.println("Nickname cannot be empty. Please enter a nickname.");
            } else if (searchUserLineIndex(nickname) != -1) {
                System.out.println("Nickname already exists. Please choose a different nickname.");
            } else {
                break;
            }
        }
        return nickname;
    }

    // 获取真实姓名
    public String getRealName() {
        System.out.println("Enter your real name:");
        return scanner.nextLine().trim();
    }

    // 获取并验证密码
    private String getPassword() {
        String password;
        while (true) {
            System.out.println("Enter your password (maximum 20 characters):");
            password = scanner.nextLine().trim();
            if (password.length() > 20) {
                System.out.println("Password must be less than 20 characters. Please try again.");
            } else {
                int passwordScore = calculatePasswordScore(password);
                if (passwordScore < 50) {
                    System.out.println("Password security level is low. Please set a stronger password.");
                } else {
                    System.out.println(passwordScore <= 75
                            ? "Password security level is good. Password accepted."
                            : "Password security level is excellent. Password accepted.");
                    break;
                }
            }
        }
        return password;
    }

    // 用户注册方法
    public boolean register(String nickname, String realName, String password) {
        if (searchUserLineIndex(nickname) != -1) {
            if (DEBUG) System.out.println("Nickname already exists");
            return false;
        }
        if (password.length() > 20) {
            if (DEBUG) System.out.println("Password must be less than 20 characters");
            return false;
        }

        // 创建新用户资料
        ArrayList<String> newProfile = new ArrayList<>();
        newProfile.add(nickname);
        newProfile.add(realName);
        newProfile.add(password);

        // 将新用户添加到数据库
        if (addAccount_DB(newProfile)) {
            if (DEBUG) System.out.println("User registered successfully");
            return true;
        } else {
            if (DEBUG) System.out.println("User registration failed");
            return false;
        }
    }

    // 登陆界面
    private void loginUser() {
        while (true) {
            System.out.println("Enter your nickname:");
            String nickname = scanner.nextLine().trim();
            System.out.println("Enter your password:");
            String password = scanner.nextLine().trim();

            if (login(nickname, password)) {
                System.out.println("Login successful!");
                break;
            } else {
                System.out.println("Login failed. Please re-enter your nickname and password.");
            }
        }
    }

    // 账号验证方法
    public boolean login(String nickname, String password) {
        int searchResult = searchUserLineIndex(nickname);
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
            return loginSetter(profile) && isLogin();
        } else {
            return false;
        }
    }

    // 计算密码强度
    private int calculatePasswordScore(String password) {
        int score = 0;
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;

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
        return Math.min(score, 25) * 4;
    }

    public static void main(String[] args) {
        try {
            Register register = new Register();
            register.displayMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

