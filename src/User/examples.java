package User;

public class examples {
    public static void main(String[] args) {
        try {
            // Create an instance of UserInfo
            UserInfo userInfo = new UserInfo();

            // Test 1: Register a new user
            String nickname = "tanjiro";
            String realName = "Kamado Tanjiro";
            String password = "sunbreathing";
            System.out.println("Test 1: Register a new user");
            boolean registerResult = userInfo.Register(nickname, realName, password);
            System.out.println("Registration result: " + (registerResult ? "Success" : "Failure"));

            // Test 2: Try to register with an existing nickname
            System.out.println("\nTest 2: Try to register with an existing nickname");
            boolean registerDuplicateResult = userInfo.Register(nickname, realName, password);
            System.out.println("Registration result: " + (registerDuplicateResult ? "Success" : "Failure"));

            // Test 3: Login with the registered user
            System.out.println("\nTest 3: Login with the registered user");
            boolean loginResult = userInfo.Login(nickname, password);
            System.out.println("Login result: " + (loginResult ? "Success" : "Failure"));

            // Test 4: Login with the wrong password
            System.out.println("\nTest 4: Login with the wrong password");
            boolean loginWrongPassword = userInfo.Login(nickname, "wrongpassword");
            System.out.println("Login result: " + (loginWrongPassword ? "Success" : "Failure"));

            // Test 5: Check if the user is logged in
            System.out.println("\nTest 5: Check if the user is logged in");
            boolean isLoggedIn = userInfo.isLogin();
            System.out.println("Is logged in: " + (isLoggedIn ? "Yes" : "No"));

            // Test 6: Logout the user
            System.out.println("\nTest 6: Logout the user");
            boolean logoutResult = userInfo.Logout();
            System.out.println("Logout result: " + (logoutResult ? "Success" : "Failure"));

            // Test 7: Check if the user is logged out after logout
            System.out.println("\nTest 7: Check if the user is logged out after logout");
            boolean isLoggedOut = !userInfo.isLogin();
            System.out.println("Is logged out: " + (isLoggedOut ? "Yes" : "No"));

            // Test 8: Try to login again after logout
            System.out.println("\nTest 8: Try to login again after logout");
            boolean reLoginResult = userInfo.Login(nickname, password);
            System.out.println("Re-login result: " + (reLoginResult ? "Success" : "Failure"));

            // Test 9: Update nickname
            System.out.println("\nTest 9: Update nickname");
            boolean updateNicknameResult = userInfo.updateNickname("tanjiro_kamado");
            System.out.println("Update nickname result: " + (updateNicknameResult ? "Success" : "Failure"));

            // Test 10: Update real name
            System.out.println("\nTest 10: Update real name");
            boolean updateRealNameResult = userInfo.updateRealName("Kamado T.");
            System.out.println("Update real name result: " + (updateRealNameResult ? "Success" : "Failure"));

            // Test 11: Update password
            System.out.println("\nTest 11: Update password");
            boolean updatePasswordResult = userInfo.updatePassword("newSunBreathing");
            System.out.println("Update password result: " + (updatePasswordResult ? "Success" : "Failure"));

            // Test 12: Update maximum score
            System.out.println("\nTest 12: Update maximum score");
            boolean updateMaxScoreResult = userInfo.updateMaxScore("100");
            System.out.println("Update maximum score result: " + (updateMaxScoreResult ? "Success" : "Failure"));

            // Test 13: Update score records
            System.out.println("\nTest 13: Update score records");
            boolean updateScoreResult1 = userInfo.updateScore(90, "DemonSlayer");
            boolean updateScoreResult2 = userInfo.updateScore(85, "WaterBreathing");
            boolean updateScoreResult3 = userInfo.updateScore(95, "HinokamiKagura");
            System.out.println("Update score result 1: " + (updateScoreResult1 ? "Success" : "Failure"));
            System.out.println("Update score result 2: " + (updateScoreResult2 ? "Success" : "Failure"));
            System.out.println("Update score result 3: " + (updateScoreResult3 ? "Success" : "Failure"));

            // Test 14: Delete the current user
            System.out.println("\nTest 14: Delete the current user");
            boolean deleteUserResult = userInfo.deleteCurrentAccount();
            System.out.println("Delete user result: " + (deleteUserResult ? "Success" : "Failure"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) {
        try {
            // Create an instance of UserInfo
            UserInfo userInfo = new UserInfo();

            // Test 1: Register and login user tanjiro
            System.out.println("Test 1: Register and login user tanjiro");
            boolean registerResult1 = userInfo.Register("tanjiro", "Kamado Tanjiro", "sunbreathing");
            System.out.println("Registration result: " + (registerResult1 ? "Success" : "Failure"));
            boolean loginResult1 = userInfo.Login("tanjiro", "sunbreathing");
            System.out.println("Login result: " + (loginResult1 ? "Success" : "Failure"));

            // Test 3: Logout user tanjiro
            System.out.println("\nTest 3: Logout user tanjiro");
            boolean logoutResult1 = userInfo.Logout();
            System.out.println("Logout result: " + (logoutResult1 ? "Success" : "Failure"));

            // Test 4: Register and login user nezuko
            System.out.println("\nTest 4: Register and login user nezuko");
            boolean registerResult2 = userInfo.Register("nezuko", "Kamado Nezuko", "demon");
            System.out.println("Registration result: " + (registerResult2 ? "Success" : "Failure"));
            boolean loginResult2 = userInfo.Login("nezuko", "demon");
            System.out.println("Login result: " + (loginResult2 ? "Success" : "Failure"));

            // Test 6: Logout user nezuko
            System.out.println("\nTest 6: Logout user nezuko");
            boolean logoutResult2 = userInfo.Logout();
            System.out.println("Logout result: " + (logoutResult2 ? "Success" : "Failure"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
