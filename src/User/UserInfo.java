package User;

import java.util.ArrayList;

public class UserInfo extends UserBase {

    public UserInfo() throws Exception {
        super();
    }

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
            // TODO: UI / MENU interface needed here
            return false;
        }
    }

    public boolean Logout() {
        // 将属性重置
        if (!logout()) { // 如果logout完还在登录状态
            if (DEBUG) System.out.println("Logout failed");
            return false;
        } else {
            if (DEBUG) System.out.println("Logout DONE!");
            return true;
        }
    }

    public boolean Register(String nickname,
                            String realName,
                            String password) {
        // 判断昵称是否已经存在
        if (searchUserLineIndex(nickname) != -1) {
            if (DEBUG) System.out.println("Nickname already exists");
            // TODO: UI / MENU interface needed here
            return false;
        }
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
        if (csvEditor.operationsDB(1, newProfile, -1)) {
            if (DEBUG) System.out.println("Register DONE!");
            return true;
        } else {
            if (DEBUG) System.out.println("Register failed");
            return false;
        }
    }

    public boolean isLogin() {
        // 根据属性是否为null 判断是否已经登录
        return nickname != null && realName != null && password != null;
    }

    protected boolean loginSetter(ArrayList<String> profile) {
        try {
            nickname = profile.get(0);
            realName = profile.get(1);
            password = profile.get(2);
            max = Integer.parseInt(profile.get(3));
            testName1 = profile.get(4);
            testScore1 = Integer.parseInt(profile.get(5));
            testName2 = profile.get(6);
            testScore2 = Integer.parseInt(profile.get(7));
            testName3 = profile.get(8);
            testScore3 = Integer.parseInt(profile.get(9));
        } catch (Exception e) {
            if (DEBUG) System.out.println("Invalid profile");
        }

        return isLogin();
    }

    public boolean logout() {
        nickname = realName = password = null;
        testScore1 = testScore2 = testScore3 = max = -1;
        testName1 = testName2 = testName3 = null;
        return !isLogin();
    }

    protected void tempAccountSetter(ArrayList<String> profile) {
        temp_nickname = profile.get(0);
        temp_password = profile.get(2);
        if (DEBUG) System.out.println("Temp Account: " + temp_nickname);
        if (DEBUG) System.out.println("Temp Password: " + temp_password);
    }


    public static void main(String[] args) {
        try {
            // 创建 UserInfo 实例
            UserInfo userInfo = new UserInfo();

            // 测试 1：注册新用户
            String nickname = "tanjiro";
            String realName = "Kamado Tanjiro";
            String password = "sunbreathing";
            System.out.println("测试 1：注册新用户");
            boolean registerResult = userInfo.Register(nickname, realName, password);
            System.out.println("注册结果: " + (registerResult ? "成功" : "失败"));

            // 测试 2：尝试用已存在的昵称注册
            System.out.println("\n测试 2：用已存在的昵称注册");
            boolean registerDuplicateResult = userInfo.Register(nickname, realName, password);
            System.out.println("注册结果: " + (registerDuplicateResult ? "成功" : "失败"));

            // 测试 3：登录已注册用户
            System.out.println("\n测试 3：登录已注册用户");
            boolean loginResult = userInfo.Login(nickname, password);
            System.out.println("登录结果: " + (loginResult ? "成功" : "失败"));

            // 测试 4：使用错误的密码登录
            System.out.println("\n测试 4：使用错误的密码登录");
            boolean loginWrongPassword = userInfo.Login(nickname, "wrongpassword");
            System.out.println("登录结果: " + (loginWrongPassword ? "成功" : "失败"));

            // 测试 5：检查是否已登录
            System.out.println("\n测试 5：检查是否已登录");
            boolean isLoggedIn = userInfo.isLogin();
            System.out.println("是否已登录: " + (isLoggedIn ? "是" : "否"));

            // 测试 6：注销用户
            System.out.println("\n测试 6：注销用户");
            boolean logoutResult = userInfo.Logout();
            System.out.println("注销结果: " + (logoutResult ? "成功" : "失败"));

            // 测试 7：注销后检查是否已登录
            System.out.println("\n测试 7：注销后检查是否已登录");
            boolean isLoggedOut = !userInfo.isLogin();
            System.out.println("是否已注销: " + (isLoggedOut ? "是" : "否"));

            // 测试 8：注销后再次尝试登录
            System.out.println("\n测试 8：注销后再次尝试登录");
            boolean reLoginResult = userInfo.Login(nickname, password);
            System.out.println("再次登录结果: " + (reLoginResult ? "成功" : "失败"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
