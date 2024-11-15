package User;

public class examples {
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

            // 测试 9：更新昵称
            System.out.println("\n测试 9：更新昵称");
            boolean updateNicknameResult = userInfo.updateNickname("tanjiro_kamado");
            System.out.println("更新昵称结果: " + (updateNicknameResult ? "成功" : "失败"));

            // 测试 10：更新真实姓名
            System.out.println("\n测试 10：更新真实姓名");
            boolean updateRealNameResult = userInfo.updateRealName("Kamado T.");
            System.out.println("更新真实姓名结果: " + (updateRealNameResult ? "成功" : "失败"));

            // 测试 11：更新密码
            System.out.println("\n测试 11：更新密码");
            boolean updatePasswordResult = userInfo.updatePassword("newSunBreathing");
            System.out.println("更新密码结果: " + (updatePasswordResult ? "成功" : "失败"));

            // 测试 12：更新最大得分
            System.out.println("\n测试 12：更新最大得分");
            boolean updateMaxScoreResult = userInfo.updateMaxScore("100");
            System.out.println("更新最大得分结果: " + (updateMaxScoreResult ? "成功" : "失败"));

            // 测试 13：更新分数记录
            System.out.println("\n测试 13：更新分数记录");
            boolean updateScoreResult1 = userInfo.updateScore(90, "DemonSlayer");
            boolean updateScoreResult2 = userInfo.updateScore(85, "WaterBreathing");
            boolean updateScoreResult3 = userInfo.updateScore(95, "HinokamiKagura");
            System.out.println("更新分数记录结果 1: " + (updateScoreResult1 ? "成功" : "失败"));
            System.out.println("更新分数记录结果 2: " + (updateScoreResult2 ? "成功" : "失败"));
            System.out.println("更新分数记录结果 3: " + (updateScoreResult3 ? "成功" : "失败"));

            // 测试 14：删除当前用户
            System.out.println("\n测试 14：删除当前用户");
            boolean deleteUserResult = userInfo.deleteCurrentAccount();
            System.out.println("删除用户结果: " + (deleteUserResult ? "成功" : "失败"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) {
        try {
            // 创建一个 UserInfo 实例
            UserInfo userInfo = new UserInfo();

            // 测试 1：注册并登录用户 tanjiro
            System.out.println("测试 1：用户 tanjiro 注册并登录");
            boolean registerResult1 = userInfo.Register("tanjiro", "Kamado Tanjiro", "sunbreathing");
            System.out.println("注册结果: " + (registerResult1 ? "成功" : "失败"));
            boolean loginResult1 = userInfo.Login("tanjiro", "sunbreathing");
            System.out.println("登录结果: " + (loginResult1 ? "成功" : "失败"));

//            // 测试 2：用户 tanjiro 更新分数
//            System.out.println("\n测试 2：用户 tanjiro 更新分数");
//            boolean updateScore1 = userInfo.updateScore(95, "SunBreathing");
//            System.out.println("更新分数结果: " + (updateScore1 ? "成功" : "失败"));

            // 测试 3：用户 tanjiro 注销
            System.out.println("\n测试 3：用户 tanjiro 注销");
            boolean logoutResult1 = userInfo.Logout();
            System.out.println("注销结果: " + (logoutResult1 ? "成功" : "失败"));

            // 测试 4：注册并登录用户 nezuko
            System.out.println("\n测试 4：用户 nezuko 注册并登录");
            boolean registerResult2 = userInfo.Register("nezuko", "Kamado Nezuko", "demon");
            System.out.println("注册结果: " + (registerResult2 ? "成功" : "失败"));
            boolean loginResult2 = userInfo.Login("nezuko", "demon");
            System.out.println("登录结果: " + (loginResult2 ? "成功" : "失败"));

//            // 测试 5：用户 nezuko 更新分数
//            System.out.println("\n测试 5：用户 nezuko 更新分数");
//            boolean updateScore2 = userInfo.updateScore(88, "BloodDemonArt");
//            System.out.println("更新分数结果: " + (updateScore2 ? "成功" : "失败"));
//
            // 测试 6：用户 nezuko 注销
            System.out.println("\n测试 6：用户 nezuko 注销");
            boolean logoutResult2 = userInfo.Logout();
            System.out.println("注销结果: " + (logoutResult2 ? "成功" : "失败"));
//
//            // 测试 7：重新注册并登录用户 tanjiro，然后删除账号
//            System.out.println("\n测试 7：用户 tanjiro 重新注册并登录，然后删除自己");
//            userInfo.Register("tanjiro", "Kamado Tanjiro", "sunbreathing");
//            userInfo.Login("tanjiro", "sunbreathing");
//            boolean deleteResult1 = userInfo.deleteCurrentAccount();
//            System.out.println("删除用户结果: " + (deleteResult1 ? "成功" : "失败"));
//
//            // 测试 8：尝试登录已删除的用户 tanjiro
//            System.out.println("\n测试 8：尝试登录已删除的用户 tanjiro");
//            boolean loginResult3 = userInfo.Login("tanjiro", "sunbreathing");
//            System.out.println("登录结果: " + (loginResult3 ? "成功" : "失败"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
