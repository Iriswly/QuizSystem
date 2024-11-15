package User;

import CSVEditor_UserSystem.CSVEditor;

import java.util.ArrayList;

public class UserBase {
    protected CSVEditor csvEditor = new CSVEditor();
    protected final boolean DEBUG = true;

    protected String nickname = null;
    protected String realName = null;
    protected String password = null;

    protected int max = -1;

    protected String testName1 = null;
    protected int testScore1 = -1;

    protected String testName2 = null;
    protected int testScore2 = -1;

    protected String testName3 = null;
    protected int testScore3 = -1;

    protected String temp_nickname = null;
    protected String temp_password = null;

    public UserBase() throws Exception {

    }

    public String getNickname() {
        return nickname;
    }

    public String getRealName() {
        return realName;
    }

    public int getMax() {
        return max;
    }

    public String getTestName1() {
        return testName1;
    }

    public int getTestScore1() {
        return testScore1;
    }

    public String getTestName2() {
        return testName2;
    }

    public int getTestScore2(){
        return testScore2;
    }

    public String getTestName3() {
        return testName3;
    }

    public int getTestScore3() {
        return testScore3;
    }

    protected int searchUserLineIndex(String nickname){
        ArrayList<Integer> temp_result = csvEditor.matchCol_exactly_unit_first(nickname, 0);
        if (!temp_result.isEmpty()) {
            return temp_result.get(0);
        } else return -1;
    }

    protected ArrayList<String> getProfile(int lineIndex){
        if (DEBUG) System.out.println("Getting Profile on Line: " + lineIndex);
        if (lineIndex < 0 || lineIndex >= csvEditor.getLineCount()) {
            return null;
        }
        return csvEditor.outputLineFormatter(lineIndex);
    }

    protected ArrayList<String> findingAccountOnNickName(String nickname){
        if (DEBUG) System.out.println("Finding Account on Nickname: " + nickname);
        int lineIndex = searchUserLineIndex(nickname);
        if (lineIndex == -1) {
            if (DEBUG) System.out.println("Account not found");
            return null;
        }
        ArrayList<String> profile = getProfile(lineIndex);
        if (profile == null) {
            if (DEBUG) System.out.println("Profile not found");
            return null;
        }
        if (DEBUG) System.out.println("Profile: " + profile);
        return profile;
    }


    protected boolean addAccount_DB(ArrayList<String> newProfile){
        return (csvEditor.operationsDB(1, newProfile, -1)) ;
    }
    // In UserBase class
    protected boolean loginSetter(ArrayList<String> profile) {
        // Implement the logic to set the login state
        // For example, set a flag or update a session
        return true; // Return true if login state is set successfully
    }

    protected boolean isLogin() {
        // Implement the logic to check if the user is logged in
        // For example, check a flag or session state
        return true; // Return true if the user is logged in
    }

    protected boolean editAccountProfile_DB(ArrayList<String> newProfile){
        return csvEditor.operationsDB(2, newProfile, -1);
    }

    protected boolean deleteAccount_DB(int lineIndex){
        return csvEditor.operationsDB(3, null, lineIndex);
    }
}
