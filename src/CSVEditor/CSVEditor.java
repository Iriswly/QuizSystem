package CSVEditor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVEditor extends CSVReader{
    public CSVEditor() throws Exception{
        super();
        readAll();
    }

    // 将读入currentLines的数据写入temp_users.csv
    private boolean dumpAllToTemp() {
        if (!isTempUserCSVExists()) tempUsersCSVCreator();
        if (currentLines == null) {
            if (DEBUG) logger.log("currentLines is null");
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEMP_FILEPATH))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine(); // 确保每行都换行
            }
            logger.log("dump all lines to temp csv Done");
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
        logger.log("========= TEMP DEBUG =========");
        logger.log(currentLines.toString());
        logger.log("========= TEMP DEBUG =========");
        currentLines.add(newLine);
//        currentLines.add(newLine);
//        currentLines.add(newLine);
        logger.log("========= TEMP DEBUG =========");
        logger.log(currentLines.toString());
        logger.log("========= TEMP DEBUG =========");
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
                        if (DEBUG) logger.log("addNewLine Done");
                        if (overcast()) {
                            if (DEBUG) logger.log("overcast Done");
                            return true;
                        }
                    }
                    else {
                        if (DEBUG) logger.log("addNewLine failed");
                    }
                    return false;
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            }
            case 2: // EDIT
            {
                String tempLine = inputLineFormatter(newLine);
                if (tempLine == null) {
                    if (DEBUG) logger.log("EDIT failed: newLine is null");
                    return false;
                }
                if (lineIndex == -1) {
                    if (DEBUG) logger.log("user not found");
                    return false;
                }
                try {
                    if (editLine(lineIndex, tempLine)) {
                        if (overcast()) {
                            if (DEBUG) logger.log("overcast Done");
                            return true;
                        }
                        return false;
                    }
                    else {
                        if (DEBUG) logger.log("editLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }
            }

            case 3: // DELETE
                try{
                    if (lineIndex == -1) {
                        if (DEBUG) logger.log("user not found");
                        return false;
                    }

                    if (deleteLine(lineIndex)) {
                        if (overcast()) {
                            if (DEBUG) logger.log("overcast Done");
                            return true;
                        }
                        return false;
                    }
                    else {
                        if (DEBUG) logger.log("deleteLine failed");
                        return false;
                    }
                } catch (Exception e) {
                    if (DEBUG) e.printStackTrace();
                    return false;
                }

            default:
                if (DEBUG) logger.log("invalid mode");
                return false;
        }
    }

    // 提交修改到数据库
    // 先将users.csv 的修改提交到last.csv （历史记录，以后可能做备份恢复功能）
    // 再将temp_csv 的修改提交到users.csv
    protected boolean overcast() {
        if (currentLines == null) {
            if (DEBUG) logger.log("currentLines is null");
            readAll();  // 读取所有内容到 currentLines 中
        }

        // 调试输出：打印 currentLines 内容，检查是否包含所有行
        if (DEBUG) {
            logger.log("currentLines 内容:");
            for (String line : currentLines) {
                logger.log(line);
            }
        }

        if (!isUserCSVExists()) usersCSVCreator();
        if (!isLastCSVExists()) lastCSVCreator();

        File userCSV = new File(FILEPATH);
        File lastCSV = new File(LAST_FILEPATH);

        // 将 users.csv 的内容复制到 last.csv 作为备份
        try (FileInputStream fis = new FileInputStream(userCSV);
             FileOutputStream fos = new FileOutputStream(lastCSV)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            if (DEBUG) logger.log("复制 users.csv 到 last.csv 成功");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        // 将 currentLines 写入 users.csv，确保 users.csv 始终是最新版本
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userCSV))) {
            for (String line : currentLines) {
                bw.write(line);
                bw.newLine();
            }
            if (DEBUG) logger.log("将最新内容写入 users.csv 完成");
        } catch (IOException e) {
            if (DEBUG) e.printStackTrace();
            return false;
        }

        if (DEBUG) logger.log("提交修改完成");
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
//            if (DEBUG) logger.log("currentLines is null");
//            return false;
//        }
//        if (overcast()) {
//            if (DEBUG) logger.log("overcast Done");
//        } else {
//            if (DEBUG) logger.log("overcast failed");
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
        logger.log("1");
        logger.log("");

        writer.readAll();

        logger.log("2");
        logger.log("");

        writer.showLines();

        logger.log("3");
        logger.log("");

        if (!writer.dumpAllToTemp()){
            logger.log("failed to dump");
            return;
        }

        logger.log("4");
        logger.log("");

        try {
            writer.addNewLine("new line");
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.log("5");
        logger.log("");

        try {
            for (int i = 0; i < writer.currentLines.size(); i++) logger.log(writer.nextLine());
        } catch (Exception e){
            logger.log("out now");
        }

        logger.log("6");
        logger.log("");

        try {
            writer.editLine(1, "edited line");
        } catch (IndexOutOfBoundsException e) {
            logger.log("shit");
        }
        writer.showLines();

        logger.log("7");
        logger.log("");

        if (!writer.overcast()) {
            logger.log("overcast failed");
        }
    }


}
