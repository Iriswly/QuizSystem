package CSVEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVEditor extends CSVReader{
    public CSVEditor() throws Exception{
        super();
    }

    // 将读入currentLines的数据写入temp_users.csv
    private boolean dumpAllToTemp() {
        if (!isTempUserCSVExists()) tempUsersCSVCreator();
        if (currentLines == null) {
            if (DEBUG) System.out.println("currentLines is null");
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEMP_FILEPATH)))  {
            for (String line : currentLines){
                bw.write(line + '\n');
            }
            System.out.println("dump all lines to temp csv Done");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    // 修改特定的行， 注意输入的line得是已经格式化好的
    protected boolean editLine(int lineIndex, String newLine) throws IndexOutOfBoundsException{
        if (currentLines == null) throw new IndexOutOfBoundsException("invalid currentLines");
        try {
            currentLines.set(lineIndex, newLine);
            // throw IndexOutOfBoundsException
        } catch (IndexOutOfBoundsException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }
        return true;
    }

    // 添加一行, 注意输入的line得是已经格式化好的
    protected boolean addNewLine(String newLine) throws Exception{
        if (currentLines == null) throw new Exception("invalid currentLines");
        currentLines.add(newLine);
        return true;
    }

    // 删除一行
    protected boolean deleteLine(int lineIndex) throws Exception{
        if (currentLines == null) throw new Exception("invalid currentLines");
        if (lineIndex < 0 || lineIndex > currentLines.size() - 1) return false;
        currentLines.remove(lineIndex);
        return true;
    }

    // 提交修改到数数据库的外界用的api （虽然user类中又对它包装了一次）
    public boolean operationsDB(int mode, ArrayList<String> newLine, int lineIndex){
        switch (mode) {
            case 1: // ADD
            {
                String tempLine = inputLineFormatter(newLine);
                if (tempLine == null) return false;
                try {
                    if (addNewLine(tempLine)) {
                        if (DEBUG) System.out.println("addNewLine Done");
                        if (overcast()) {
                            if (DEBUG) System.out.println("overcast Done");
                            return true;
                        }
                        return false;
                    }
                    else {
                        if (DEBUG) System.out.println("addNewLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            }
            case 2: // EDIT
            {
                String tempLine = inputLineFormatter(newLine);
                if (tempLine == null) return false;
                try {
                    if (editLine(lineIndex, tempLine)) {
                        if (overcast()) {
                            if (DEBUG) System.out.println("overcast Done");
                            return true;
                        }
                        return false;
                    }
                    else {
                        if (DEBUG) System.out.println("editLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            }

            case 3: // DELETE
                try{
                    if (deleteLine(lineIndex)) {
                        if (overcast()) {
                            if (DEBUG) System.out.println("overcast Done");
                            return true;
                        }
                        return false;
                    }
                    else {
                        if (DEBUG) System.out.println("deleteLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }

            default:
                if (DEBUG) System.out.println("invalid mode");
                return false;
        }
    }

    // 提交修改到数据库
    // 先将users.csv 的修改提交到last.csv （历史记录，以后可能做备份恢复功能）
    // 再将temp_csv 的修改提交到users.csv
    protected boolean overcast() {
        if (currentLines == null) {
            if (DEBUG) System.out.println("currentLines is null");
            readAll();
        }

        if (!isUserCSVExists()) usersCSVCreator();
        if (!isLastCSVExists()) lastCSVCreator();
        if (!isTempUserCSVExists()) tempUsersCSVCreator();

        // warning: one readAll function is needed before using this shit
        dumpAllToTemp();

        File userCSV = new File(FILEPATH);
        File lastCSV = new File(LAST_FILEPATH);
        File tempCSV = new File(TEMP_FILEPATH);

        lastCSV.delete();
        if (lastCSV.exists()) {
            if (DEBUG) System.out.println("deleted last.csv FAILED");
            return false;
        }
        if (DEBUG) System.out.println("deleted last.csv");
        if (!userCSV.renameTo(lastCSV)) return false;
        if (DEBUG) System.out.println("renamed users.csv to last.csv");
        if (!tempCSV.renameTo(userCSV)) return false;
        if (DEBUG) System.out.println("renamed temp.csv to user.csv");
        if (DEBUG) System.out.println("Done");
        return true;
    }

    // 数据库对外的输出格式的api
    public ArrayList<String> outputLineFormatter(int lineIndex){
        if (currentLines == null) readAll();
        if (lineIndex < 0 || lineIndex > currentLines.size() - 1) return null;
        ArrayList<String> line =  new ArrayList<>(List.of(currentLines.get(lineIndex).split(",")));
        if (line.size() >= 10) return line;
        return null;
    }

//    protected boolean saveAndUpdate(){
//        if (currentLines == null) {
//            if (DEBUG) System.out.println("currentLines is null");
//            return false;
//        }
//        if (overcast()) {
//            if (DEBUG) System.out.println("overcast Done");
//        } else {
//            if (DEBUG) System.out.println("overcast failed");
//            return false;
//        }
//        readAll();
//    }

    // 辅助函数
    private boolean isValidString(String str){
        return str != null && !str.isEmpty();
    }

    // 辅助函数
    private boolean isValidDigit(String str){
        if (isValidString(str)) {
            try {
                Integer.parseInt(str);
                return true;
            } catch (NumberFormatException e){
                return false;
            }
        }
        return false;
    }

    // 验证外部输入是否有效
    protected boolean isFormatValid(ArrayList<String> line){
        if (line.size() < 10) return false;
        return (isValidString(line.get(0))) && (isValidString(line.get(1))) && (isValidString(line.get(2)))
                && (isValidDigit(line.get(3)))
                && (isValidString(line.get(4))) && (isValidDigit(line.get(5)))
                && (isValidString(line.get(6))) && (isValidDigit(line.get(7)))
                && (isValidString(line.get(8))) && (isValidDigit(line.get(9)));
    }

    // 外界的输入进入数据库的API
    public String inputLineFormatter(ArrayList<String> line){
        if (currentLines == null) readAll();
        if (isFormatValid(line)) {
            return String.join(",", line);
        }
        return null;
    }

    public static void main(String[] args) {
        CSVEditor writer;

        try{
            writer = new CSVEditor();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println(1);
        System.out.println();

        writer.readAll();

        System.out.println(2);
        System.out.println();

        writer.showLines();

        System.out.println(3);
        System.out.println();

        if (!writer.dumpAllToTemp()){
            System.out.println("failed to dump");
            return;
        }

        System.out.println(4);
        System.out.println();

        try {
            writer.addNewLine("new line");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(5);
        System.out.println();

        try {
            for (int i = 0; i < writer.currentLines.size(); i++) System.out.println(writer.nextLine());
        } catch (Exception e){
            System.out.println("out now");
        }

        System.out.println(6);
        System.out.println();

        try {
            writer.editLine(13, "edited line");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("shit");
        }
        writer.showLines();

        System.out.println(7);
        System.out.println();

        if (!writer.overcast()) {
            System.out.println("overcast failed");
        }
    }


}
