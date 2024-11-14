package User;

import CSVEditor.CSVEditor;

import java.util.ArrayList;

public class UserBase {
    protected CSVEditor csvEditor = new CSVEditor();
    protected final boolean DEBUG = true;
    protected final int MAX_SCORE = 100;
    protected final int MIN_SCORE = 0;
    protected Logger logger = new Logger(DEBUG);

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
        logger.log("[getProfile(int lineIndex)] Getting Profile on Line: " + lineIndex);
        if (lineIndex < 0 || lineIndex >= csvEditor.getLineCount()) {
            return null;
        }
        return csvEditor.outputLineFormatter(lineIndex);
    }

    protected ArrayList<String> findingAccountOnNickName(String nickname){
        logger.log("[findingAccountOnNickName(String nickname)] Finding Account on Nickname: " + nickname);
        int lineIndex = searchUserLineIndex(nickname);
        if (lineIndex == -1) {
            logger.log("[findingAccountOnNickName(String nickname)] Account not found");
            return null;
        }
        ArrayList<String> profile = getProfile(lineIndex);
        if (profile == null) {
            logger.log("[findingAccountOnNickName(String nickname)] Profile not found");
            return null;
        }
        logger.log("[findingAccountOnNickName(String nickname)] Profile: " + profile);
        return profile;
    }

    protected boolean addAccount_DB(ArrayList<String> newProfile){
        logger.log("[addAccount_DB(ArrayList<String> newProfile)] Adding Account: " + newProfile);
        return (csvEditor.operationsDB(1, newProfile, -1)) ;
    }

    protected boolean editAccountProfile_DB(ArrayList<String> newProfile){
        int lineIndex = searchUserLineIndex(newProfile.get(0));
        return csvEditor.operationsDB(2, newProfile, lineIndex);
    }

    protected boolean editAccountProfile_DB(ArrayList<String> newProfile, String nickName){
        // use the nickName in case of the nickname is the item that we are changing
        int lineIndex = searchUserLineIndex(nickName);
        return csvEditor.operationsDB(2, newProfile, lineIndex);
    }

    protected boolean deleteAccount_DB(String nickName){
        int lineIndex = searchUserLineIndex(nickName);
        return csvEditor.operationsDB(3, null, lineIndex);
    }
}
