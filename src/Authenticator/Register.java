package Authenticator;

import User.UserInfo;
import java.util.ArrayList;
import java.util.Scanner;

public class Register extends UserInfo {
    public Register() throws Exception {
            super();
    }
    //注册或登陆界面
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome! Are you a new user? (yes/no)");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            registerNewUser(scanner);
        } else if (choice.equals("no")) {
            loginUser(scanner);
        } else {
            System.out.println("Invalid choice. Please try again.");
            displayMenu();
        }
    }

    // 注册新用户界面
    private void registerNewUser(Scanner scanner) {
        String nickname;
        while (true) {
            System.out.println("Enter your nickname:");
            nickname = scanner.nextLine();
            if (searchUserLineIndex(nickname) != -1) {
                System.out.println("Nickname already exists. Please choose a different nickname.");
            } else {
                break;
            }
        }
        System.out.println("Enter your real name:");
        String realName = scanner.nextLine();
        System.out.println("Create a password (8-16 characters):");
        String password = scanner.nextLine();

        if (Register(nickname, realName, password)) {
            System.out.println("Registration successful! Please log in.");
            loginUser(scanner);
        } else {
            System.out.println("Registration failed. Please try again.");
            displayMenu();
        }
    }
    // 登陆界面
    private void loginUser(Scanner scanner) {
        System.out.println("Enter your nickname:");
        String nickname = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        if (Login(nickname, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Please try again.");
            displayMenu();
        }
    }

    public boolean Register(String nickname,
                            String realName,
                            String password) {

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

//是否有同昵称
        if (searchUserLineIndex(nickname) != -1) {
            if (DEBUG) System.out.println("Nickname already exists");
            return false;
        }

        // 密码长度要求8-16位
        if (password.length() < 8 || password.length() > 16) {
            if (DEBUG) System.out.println("Password must be between 8 and 16 characters");
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

        // Add new user to the database
        if (addAccount_DB(newProfile)) {
            if (DEBUG) System.out.println("User registered successfully");
            return true;
        } else {
            if (DEBUG) System.out.println("User registration failed");
            return false;
        }
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
