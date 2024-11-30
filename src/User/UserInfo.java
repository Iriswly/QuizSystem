package User;

import java.util.ArrayList;
import java.util.List;

/*
 * UserInfo class
 * extends the UseBase class to provide the api for user
 * for example, login, logout, register, delete account, get ranked accounts, etc.
 */
public class UserInfo extends UserBase {

    public UserInfo() throws Exception {
        super();
    }

    //  ============ INTERFACES ==================

    public boolean Login(String nickname, String password) {
        int searchResult = searchUserLineIndex(nickname);
        if (searchResult == -1) {
            logger.log("[Login(String nickname, String password)] Account not found");
            return false;
        }
        ArrayList<String> profile = getProfile(searchResult);
        if (profile == null) {
            logger.log("Profile not found");
            return false;
        }
        logger.log("[Login(String nickname, String password)] Profile: " + profile);
        if (password.equals(profile.get(2))) {
            if (!loginSetter(profile)) {
                logger.log("[Login(String nickname, String password)] Login failed");
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
            logger.log("[Logout] Logout failed");
            return false;
        } else {
            logger.log("[Logout] Logout DONE!");
            return true;
        }
    }

    public boolean Register(String nickname,
                            String realName,
                            String password) {
        // 判断昵称是否已经存在
        if (searchUserLineIndex(nickname) != -1) {
            logger.log("[Register] Nickname already exists");
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

        if (addAccount_DB(newProfile)) {
            logger.log("[Register] Register DONE!");
            return true;
        } else {
            logger.log("[Register] Register failed");
            return false;
        }
    }

    /*
     * check if the current account is logged in
     * according to whether the user' attribute is null
     * @return whether the user is logged in
     */
    public boolean isLogin() {
        // 根据属性是否为null 判断是否已经登录
        return nickname != null && realName != null && password != null;
    }

    /*
     * logout the current account
     * logout through setting the attribute of use the NULL
     * @return whether the logout operation is successful
     */
    public boolean logout() {
        nickname = realName = password = null;
        testScore1 = testScore2 = testScore3 = max = -1;
        testName1 = testName2 = testName3 = null;
        return !isLogin();
    }

    /*
     * delete the current account
     * @return whether the operation is successful
     */
    public boolean deleteAccount(String nickname) {
        int index = searchUserLineIndex(nickname);
        if (index == -1) {
            logger.log("Account not found");
            return false;
        }
        // completement thee logic that delete the use from the db there
        // if the user is deleted successfully, return true
        return deleteAccount_DB(nickname);
    }

    /*
     * query the ranked accounts by max score
     *
     * @return a list of ranked accounts
     */
    public List<String> getRankedAccountsByMaxScore() {
        List<AccountScore> accountsWithScores = new ArrayList<>();

        for (int i = 1; i <= csvEditor.getLineCount(); i++) {
            ArrayList<String> profile = getProfile(i);
            if (profile == null) {
                logger.log("[getRankedAccountsByMaxScore] Profile not found for line " + i);
                continue;
            }

            String nickname = profile.get(0);
            String maxScoreStr = profile.get(3);

            // check if the max is valid ("-1" means null)
            if (!"-1".equals(maxScoreStr)) {
                try {
                    int maxScore = Integer.parseInt(maxScoreStr);
                    accountsWithScores.add(new AccountScore(nickname, maxScore));
                } catch (NumberFormatException e) {
                    logger.log("[getRankedAccountsByMaxScore] Invalid max score for " + nickname);
                }
            }
        }

        // bubble sorting there
        for (int i = 0; i < accountsWithScores.size() - 1; i++) {
            for (int j = 0; j < accountsWithScores.size() - 1 - i; j++) {
                if (accountsWithScores.get(j).maxScore < accountsWithScores.get(j + 1).maxScore) {
                    AccountScore temp = accountsWithScores.get(j);
                    accountsWithScores.set(j, accountsWithScores.get(j + 1));
                    accountsWithScores.set(j + 1, temp);
                }
            }
        }
        // output
        List<String> rankedAccounts = new ArrayList<>();
        for (AccountScore account : accountsWithScores) {
            rankedAccounts.add("Nickname: " + account.nickname + ", Max Score: " + account.maxScore);
        }

        return rankedAccounts;
    }

    /*
     * update the max score of the current account
     *
     * @return: whether the operation is successful
     */
    public boolean updateScore(int score, String scoreName) {
        // check if login
        if (!isLogin()) {
            logger.log("[updateScore] Not logged in");
            return false;
        }
        // check the range of the score and scoreName
        // the _ is reserved for the empty testName
        if (score < MIN_SCORE || score > MAX_SCORE || scoreName == null || scoreName.isEmpty() || scoreName.equals("_")) {
            logger.log("[updateScore] Invalid score or scoreName");
            if (scoreName != null && scoreName.equals("_")) {
                logger.log("[updateScore] score name _ is not usable");
            }
            return false;
        }

        // swap the score storage in the current account (current attributes)
        int tempScore = testScore2;
        String tempScoreName = testName2;

        testScore2 = testScore1;
        testName2 = testName1;

        testScore3 = tempScore;
        testName3 = tempScoreName;

        testScore1 = score;
        testName1 = scoreName;

        // format the attribute into the ArrayList
        ArrayList<String> newProfile = getCurrentProfile();
        if (newProfile == null) {
            logger.log("[updateScore] Profile not found");
            return false;
        }

        // put the profile into the csv
        if (!editAccountProfile_DB(newProfile)) {
            logger.log("[updateScore] Update failed");
            return false;
        }

        return true;
    }

    /*
     * update nickname
     */
    public boolean updateNickname(String newNickname) {
        return editAccountProfile(1, newNickname);
    }

    /*
     * update real name
     */
    public boolean updateRealName(String newRealName) {
        return editAccountProfile(2, newRealName);
    }

    /*
     * update password
     */
    public boolean updatePassword(String newPassword) {
        return editAccountProfile(3, newPassword);
    }

    /*
     * update the max score of the user
     */
    public boolean updateMaxScore(String newMax) {
        return editAccountProfile(4, newMax);
    }

    /*
     * helping method to put the profile into the attribute of current user
     * then the attributes should be not null
     * and then the user is called "login"
     *
     * called when login
     */
    public boolean loginSetter(ArrayList<String> profile) {
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
            logger.log("[loginSetter] Invalid profile");
        }

        return isLogin();
    }

    /*
     * get the profile of the current user
     *
     * @return: the profile of the current user
     */
    public ArrayList<String> getCurrentProfile() {
        if (!isLogin()) {
            logger.log("[getCurrentProfile] Not logged in");
            return null;
        }

        //
        int highestScore = Math.max(testScore1, Math.max(testScore2, testScore3));

        // return an ArrayList that contains the user info
        // the right freaking pattern is actually a dog head, please just ignore it  _orz
        // I really don't know why my workmate did this :(
        // -- Xinrong Li :(
        return new ArrayList<>() {{
            add("Nickname: " + nickname);
            add("Real Name: " + realName + "                                        " + "       __/ \\   \\/    / \\__       ");
            add("Password: " + password + "                            " + "   __/@    )   ||    (    @\\__   ");
            add("Max Score: " + highestScore + "                                       " + " O         \\  _||_  /         O  ");
            add(testName1 + ": " + testScore1 + "                                        " + "  \\_____)   \\/ ||  \\/   (_____/  ");
            add(testName2 + ": " + testScore2 + "                                        " + "    U  \\_____\\ /\\ /_____/   U  ");
            add(testName3 + ": " + testScore3);
        }};
    }


    //  ============ INTERFACES DONE ==================

    /*
     * helping class to record the nickname and max score
     */
    private static class AccountScore {
        private final String nickname;
        private final int maxScore;

        public AccountScore(String nickname, int maxScore) {
            this.nickname = nickname;
            this.maxScore = maxScore;
        }
    }

    /*
       change the nickname if the mode is 1
       change the real name if the mode is 2
       change the password if the mode is 3
       change the max score if the mode is 4

       @return : whether the operation is successful
     */
    protected boolean editAccountProfile(int mode, String newValue) {
        if (!isLogin()) {
            logger.log("[editAccountProfile] Not logged in");
            return false;
        }

        boolean isStrInvalid = newValue == null || newValue.isEmpty() || newValue.equals("_");

        switch (mode) {
            case 1:
                // edit the nickname

                // validation
                if (isStrInvalid) {
                    logger.log("[editAccountProfile] Invalid nickname");
                    return false;
                }
                // check if the name exists
                if (searchUserLineIndex(newValue) != -1) {
                    logger.log("[editAccountProfile] Nickname already exists or you are using the previous name");
                    return false;
                }
                // change the name and save to the csv
                String temp = nickname;
                nickname = newValue;
                ArrayList<String> newProfile = getCurrentProfile();
                if (!editAccountProfile_DB(newProfile, temp)) {
                    logger.log("[editAccountProfile] Update nickname failed");
                    return false;
                }
                return true;

            case 2:
                // edit the real name
                if (isStrInvalid) {
                    logger.log("[editAccountProfile] Invalid real name");
                    return false;
                }
                realName = newValue;
                if (!editAccountProfile_DB(getCurrentProfile())) {
                    logger.log("[editAccountProfile] Update real name failed");
                    return false;
                }
                return true;

            case 3:
                // strength check part should be ahead of edition
                // edit the password
                if (newValue == null || newValue.isEmpty()) {
                    logger.log("[editAccountProfile] Invalid password");
                    return false;
                }
                password = newValue;
                if (!editAccountProfile_DB(getCurrentProfile())) {
                    logger.log("[editAccountProfile] Update password failed");
                    return false;
                }
                return true;

            case 4:
                if (newValue == null || newValue.isEmpty()) {
                    logger.log("[editAccountProfile] Invalid max score");
                    return false;
                }
                int tempMax = Integer.parseInt(newValue);
                if (tempMax < MIN_SCORE || tempMax > MAX_SCORE) {
                    logger.log("[editAccountProfile] Invalid max score");
                    return false;
                }
                if (tempMax < max) {
                    logger.log("[editAccountProfile] No! New max score is smaller than current max score");
                    return false;
                }
                if (!editAccountProfile_DB(getCurrentProfile())) {
                    logger.log("[editAccountProfile] Update nickname failed");
                    return false;
                }
                return true;

            default:
                logger.log("[editAccountProfile] Invalid mode");
                return false;
        }
    }

    /*
     * show all the accounts (used in debug)
     */
    protected boolean showAllAccounts() {
        logger.log("[editAccountProfile] Showing all accounts");
        ArrayList<String> allAccounts = new ArrayList<>();
        for (int i = 1; i <= csvEditor.getLineCount(); i++) {
            ArrayList<String> profile = getProfile(i);
            if (profile == null) {
                logger.log("[editAccountProfile] Profile not found");
                return false;
            }
            allAccounts.add(profile.toString());
        }
        logger.log("[editAccountProfile] All accounts: " + allAccounts);
        return true;
    }

    /*
     * remove account according to the nickname
     */
    protected boolean deleteCurrentAccount() {
        if (isLogin()) {
            boolean deleteResult = deleteAccount_DB(nickname);
            logout();
            return deleteResult;
        } else {
            logger.log("[deleteCurrentAccount] Permission denied");
            return false;
        }

    }
}
