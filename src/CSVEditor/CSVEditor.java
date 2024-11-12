package CSVEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVEditor extends CSVReader{
    public CSVEditor() throws Exception{
        super();
    }

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

    protected boolean addNewLine(String newLine) throws Exception{
        if (currentLines == null) throw new Exception("invalid currentLines");
        currentLines.add(newLine);
        return true;
    }

    protected boolean deleteLine(int lineIndex) throws Exception{
        if (currentLines == null) throw new Exception("invalid currentLines");
        if (lineIndex < 0 || lineIndex > currentLines.size() - 1) return false;
        currentLines.remove(lineIndex);
        return true;
    }

    protected boolean overcast() {
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
