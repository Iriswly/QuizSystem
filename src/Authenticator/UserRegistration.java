
package Authenticator;

import User.UserBase;
import java.util.ArrayList;
import java.util.Scanner;

public class UserRegistration extends UserBase {
    private final Scanner scanner;
    public UserRegistration() throws Exception {
            super();
            scanner = new Scanner(System.in);
    }

    //主界面
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome! Are you a new user? (yes/no):");
        //System.out.println();
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes") || choice.equals("y") || choice.equals("Yes")) {
            registerNewUser();//进入注册界面
        } else if (choice.equals("no") || choice.equals("n") || choice.equals("No")) {
            loginUser();//进入登陆界面
        } else {
            System.out.println("Invalid choice. Please try again.");
            displayMenu();
        }
    }

    // 注册新用户界面
    private void registerNewUser() {
        //输入并验证昵称是否重复
        String nickname;
        while (true) {
            System.out.println("Enter your nickname:");
            nickname = scanner.nextLine();
            if (searchUserLineIndex(nickname) != -1) {
                System.out.println("Nickname already exists. Please choose a different nickname.");
                nickname = ""; // 清除输入的昵称
            } else if (nickname.isEmpty()) {
                System.out.println("Nickname cannot be empty. Please enter a nickname.");
                nickname = ""; // 清除输入的昵称
            }else {
                break;
            }
        }

        //真名
        System.out.println("Enter your real name:");
        String realName = scanner.nextLine();

        //输入并验证密码是否安全
        String password;
        while (true) {
            System.out.println("Enter your password (maximum 20 characters):");
            password = scanner.nextLine();
            // 密码长度要求最多20位
            if ( password.length() > 20) {
                System.out.println("Password must be less than 20 characters. Please try again.");
                password = ""; // Clear the entered password
            } else {
                int passwordScore = calculatePasswordScore(password);
                if (passwordScore < 50) {
                    System.out.println("Password security level is low. Please set a stronger password.");
                } else if (passwordScore <= 75) {
                    System.out.println("Password security level is good. Password accepted.");
                    break;
                } else {
                    System.out.println("Password security level is excellent. Password accepted.");
                    break;
                }
            }

        }

        //注册用户
        if (Register(nickname, realName, password)) {
            System.out.println("Registration successful! Please log in.");
            loginUser();//进入登陆界面
        } else {
            System.out.println("Registration failed. Please try again.");
            displayMenu();
        }
    }
    // 登陆界面
    private void loginUser() {
        while (true) {
            System.out.println("Enter your nickname:");
            String nickname = scanner.nextLine();
            System.out.println("Enter your password:");
            String password = scanner.nextLine();

            if (Login(nickname, password)) {
                System.out.println("Login successful!");
                break;//登陆成功
            } else {
                System.out.println("Login failed. Please re-enter your nickname and password.");
                nickname = ""; // Clear the entered nickname
                password = ""; // Clear the entered password
            }
        }

    }

    //用户注册方法
    public boolean Register(String nickname, String realName, String password) {

        if (searchUserLineIndex(nickname) != -1) {
            if (DEBUG) System.out.println("Nickname already exists");
            return false;
        }
        if(password.length() > 20) {
            if (DEBUG) System.out.println("Password must be less than 20 characters");
            return false;
        }

       //创建新用户资料
        ArrayList<String> newProfile = new ArrayList<>();
        newProfile.add(nickname);
        newProfile.add(realName);
        newProfile.add(password);
       /* newProfile.add("-1");
        newProfile.add("_");
        newProfile.add("-1");
        newProfile.add("_");
        newProfile.add("-1");
        newProfile.add("_");
        newProfile.add("-1");*/
        // 将新用户添加到数据库
       if (addAccount_DB(newProfile)) {
            if (DEBUG) System.out.println("User registered successfully");
            return true;
        } else {
            if (DEBUG) System.out.println("User registration failed");
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

    //账号验证方法
    public boolean Login(String nickname, String password) {
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
            if (!loginSetter(profile)) {
                if (DEBUG) System.out.println("Login failed");
                return false;
            }
            return isLogin();
        } else {
            return false;
        }
    }
    public static void main(String[] args) {
        try {
            UserRegistration register = new UserRegistration();
            register.displayMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}








