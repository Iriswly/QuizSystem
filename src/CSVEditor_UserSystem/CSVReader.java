package CSVEditor_UserSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSVReader
 * 提供将users.csv 文件读入currentLines的函数
 * 提供读取指定行
 * 提供显示所有行（调试用）
 * provides searching methods to get the line index of target line
 */
public class CSVReader extends CSVBase {
    protected List<String> currentLines = new ArrayList<>();
    protected int currentLineIndex;

    public CSVReader() throws Exception {
        super(); // 简单初始化一下CSVBase
        readAll();
    }

    // 将用户信息保存在currentLines中
    protected boolean readAll() {
        currentLines.clear();
        if (isUserCSVExists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(FILEPATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    currentLines.add(line);
                }
                logger.log("read all lines Done");
            } catch (IOException e) {
                if (DEBUG) e.printStackTrace();
                return false;
            }
        } else {
            logger.log("Temp user csv does not exists");
            return false;
        }
        return true;
    }

    // 获取一共有多少行 （多少个用户）
    public int getLineCount(){
        if (currentLines == null) return 0;
        else return currentLines.size();
    }

    // 读取指定的一行 (使用行号指定）
    private String readLine(int index) throws Exception {
        if (currentLines == null) {
            if (DEBUG) logger.log("invalid currentLines");
            throw new Exception("invalid currentLines");
        }
        if (index < 0 || index > currentLines.size() - 1) {
            if (DEBUG) logger.log("invalid index of line");
            throw new Exception("invalid index of line");
        }
        return currentLines.get(index);
    }

    // 读取下一行,可以用于遍历，但是现在没用了
    // Warning: this method would be ABORTED!!! (USELESS)
    public String nextLine() throws Exception {
        if (currentLineIndex > currentLines.size() - 1) {
            currentLineIndex = 0;
            throw new IndexOutOfBoundsException("end of file");
        }
        if (currentLineIndex < 0) currentLineIndex = 0; // just in case
        currentLineIndex++;
        return readLine(currentLineIndex);
    }

    // 展示所有行, 用于调试（DEBUG
    protected void showLines() {
        if (currentLines == null) {
            if (DEBUG) logger.log("the lines are empty");
            return;
        }
        for (String line : currentLines) {
            logger.log(line);
        }
    }

    // 搜索行，返回行号，返回null表示没有找到
    public ArrayList<Integer> matchLine(String matchWord, int mode) {
        // the invalid hint would be provided as the null value
        // when the search result is zero, the returnedValue.size should be 0
        // and when there are lines that matches, this function will return the list that contains all the matched line's index

        // the matchWord should not contain any commas
        if (matchWord.contains(",")) {
            if (DEBUG) logger.log("the searching word should not contains any comma");
            return null;
        }
        /*
        mode 1 = search all lines with telling the exactly form ("Abc" != "ABC" != "abc")
        mode 2 = search all lines fuzzy ("Abc" = "ABC" = "abc")
        mode 3 = search the first line with unit matches
        mode 4 = search all lines that has the unit that matches (RECOMMENDED!)
        mode 1 : 分辨大小写的搜索行内容，返回所有符合的行
        mode 2 : 模糊搜索行内容，返回所有符合的行
        mode 3 = 分辨大小写的搜索一行内的单元格，且只返回第一个发现的符合的行
        mode 4 = 分辨大小写的搜索一行内的单元格，且返回所有符合的行
         */
        return switch (mode) {
            case 1 -> matchLines_exactly_all(matchWord, false);
            case 2 -> matchLines_fuzzy_all(matchWord);
            case 3 -> matchLine_exactly_unit_first(matchWord);
            case 4 -> matchLines_exactly_unit_all(matchWord);
            default -> {
                if (DEBUG) logger.log("invalid mode");
                yield null;
            }
        };
    }

    public ArrayList<Integer> matchLines_exactly_all(String matchWord, boolean fuzzy) {
        ArrayList<Integer> matchResult = new ArrayList<>();
        for (int i = 0; i < currentLines.size(); i++) {
            String sentence = fuzzy ? currentLines.get(i).toLowerCase() : currentLines.get(i);
            if (sentence.contains(matchWord)) {
                if (DEBUG) logger.log(
                        "fuzzy: {%b}, matching word: %s in sentence %s\n",
                        fuzzy,
                        matchWord,
                        sentence);
                if (!matchResult.add(i)) {
                    if (DEBUG) logger.log("add failed");
                    return null;
                }
                if (DEBUG) logger.log("match result now: " + matchResult);
            }
        }
        return matchResult;
    }

    public ArrayList<Integer> matchLines_fuzzy_all(String matchWord) {
        return matchLines_exactly_all(matchWord.toLowerCase(), true);
    }

    public ArrayList<Integer> matchLine_exactly_unit_first(String matchWord) {
        for (int i = 0; i < currentLines.size(); i++) {
            String sentence = currentLines.get(i);
            if (sentence.contains("," + matchWord + ",") || sentence.contains("," + matchWord + '\n') || sentence.contains('\n' + matchWord + ",")) {
                int finalI = i;
                return new ArrayList<>() {{
                    add(finalI);
                }};
            }
        }
        return new ArrayList<>() {{
            add(0);
        }};
    }

    public ArrayList<Integer> matchLines_exactly_unit_all(String matchWord) {
        ArrayList<Integer> matchResult = new ArrayList<>();
        for (int i = 0; i < currentLines.size(); i++) {
            String sentence = currentLines.get(i);
            if (sentence.contains("," + matchWord + ",") || sentence.contains("," + matchWord + '\n') || sentence.contains('\n' + matchWord + ",")) {
                matchResult.add(i);
            }
        }
        return matchResult;
    }

    // 在指定的列中搜索符合的项
    // 建议在第零列搜索Nickname用于用户独特性
    public ArrayList<Integer> matchCol(String matchWord, int colIndex, int mode) {
        // I recommend using this function to search the exact unit
        // because there is checking step in the function
        if (colIndex < 0 || colIndex > MAX_COL_NUM - 1) return null;

        return switch (mode) {
            // 模糊搜索,返回所有符合的行号
            case 1 -> matchCol_fuzzy_unit_all(matchWord, colIndex);
            // 精确搜索,返回所有符合的行号
            case 2 -> matchCol_exactly_unit_all(matchWord, colIndex);
            // 模糊搜索,返回第一个符合的行号
            case 3 -> matchCol_fuzzy_unit_first(matchWord, colIndex);
            // 精确搜索,返回第一个符合的行号
            case 4 -> matchCol_exactly_unit_first(matchWord, colIndex);
            default -> {
                if (DEBUG) logger.log("invalid mode");
                yield null;
            }
        };
    }

    // 将由行组成的数据变为由一个个单元格组成的二维数组
    private ArrayList<ArrayList<String>> convert2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        for (String line : currentLines) {
            data.add(new ArrayList<>(Arrays.asList(line.split(","))));
        }
        return data;
    }

    public ArrayList<Integer> matchCol_unit_all(String matchWord, int colIndex, boolean fuzzy) {
        // convert the line into the 2 dimension array
        ArrayList<Integer> matchResults = new ArrayList<>();
        matchWord = fuzzy ? matchWord.toLowerCase() : matchWord;
        ArrayList<ArrayList<String>> data2D = convert2D();
        for (int i = 0; i < currentLines.size(); i++) {
            String item = fuzzy ? data2D.get(i).get(colIndex).toLowerCase() : data2D.get(i).get(colIndex);
            if (item.equals(matchWord)) {
                if (DEBUG)
                    logger.log("matching word: " + matchWord + " in column " + colIndex + " in line " + i);
                matchResults.add(i);
            }
        }
        return matchResults;
    }


    public ArrayList<Integer> matchCol_fuzzy_unit_all(String matchWord, int colIndex) {
        return matchCol_unit_all(matchWord, colIndex, true);
    }

    public ArrayList<Integer> matchCol_exactly_unit_all(String matchWord, int colIndex) {
        return matchCol_unit_all(matchWord, colIndex, false);
    }

    public ArrayList<Integer> matchCol_fuzzy_unit_first(String matchWord, int colIndex) {
        ArrayList<Integer> allMatches = matchCol_fuzzy_unit_all(matchWord, colIndex);
        if (allMatches.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allMatches.subList(0, Math.min(1, allMatches.size()))); // 返回第一个匹配项
    }

    public ArrayList<Integer> matchCol_exactly_unit_first(String matchWord, int colIndex) {
        ArrayList<Integer> allMatches = matchCol_exactly_unit_all(matchWord, colIndex);
        if (allMatches.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(allMatches.subList(0, Math.min(1, allMatches.size()))); // 返回第一个匹配项
    }


    public static void main(String[] args) {
        try {
            // 创建 CSVReader 实例
            CSVReader reader = new CSVReader();
            // 模拟读取文件
            reader.currentLines = Arrays.asList(
                    "apple,banana,cherry",
                    "banana,orange,grape",
                    "apple,kiwi,banana",
                    "grape,apple,banana"
            );

            // 示例 1：匹配所有行中包含“banana”的行（精确匹配，mode = 1）
            logger.log("1");
            ArrayList<Integer> result1 = reader.matchLine("banana", 1);
            logger.log("匹配所有行中包含 'banana' 的行（精确匹配，mode = 1）: " + result1);

            // 示例 2：匹配所有行中包含“APPLE”的行（模糊匹配，mode = 2）
            logger.log("2");
            ArrayList<Integer> result2 = reader.matchLine("APPLE", 2);
            logger.log("匹配所有行中包含 'APPLE' 的行（模糊匹配，mode = 2）: " + result2);

            // 示例 3：匹配第一行包含单元格“kiwi”的行（精确单元匹配，mode = 3）
            logger.log("3");
            ArrayList<Integer> result3 = reader.matchLine("kiwi", 3);
            logger.log("匹配第一行包含单元格 'kiwi' 的行（精确单元匹配，mode = 3）: " + result3);

            // 示例 4：匹配所有行包含单元格“banana”的行（精确单元匹配，mode = 4）
            logger.log("4");
            ArrayList<Integer> result4 = reader.matchLine("banana", 4);
            logger.log("匹配所有行包含单元格 'banana' 的行（精确单元匹配，mode = 4）: " + result4);

            // 示例 5：在指定列中（第 1 列）模糊匹配“APPLE”单元格的行（mode = 1）
            logger.log("5");
            ArrayList<Integer> result5 = reader.matchCol("APPLE", 1, 1);
            logger.log("在指定列（第 1 列）模糊匹配 'APPLE' 的行（mode = 1）: " + result5);

            // 示例 6：在指定列中（第 2 列）精确匹配“grape”单元格的行（mode = 2）
            logger.log("6");
            ArrayList<Integer> result6 = reader.matchCol("grape", 2, 2);
            logger.log("在指定列（第 2 列）精确匹配 'grape' 的行（mode = 2）: " + result6);

            // 示例 7：在指定列中（第 0 列）模糊匹配第一个出现的“apple”单元格的行（mode = 3）
            logger.log("7");
            ArrayList<Integer> result7 = reader.matchCol("apple", 0, 3);
            logger.log("在指定列（第 0 列）模糊匹配第一个出现的 'apple' 的行（mode = 3）: " + result7);

            // 示例 8：在指定列中（第 2 列）精确匹配所有“banana”单元格的行（mode = 4）
            logger.log("8");
            ArrayList<Integer> result8 = reader.matchCol("banana", 2, 4);
            logger.log("在指定列（第 2 列）精确匹配所有 'banana' 的行（mode = 4）: " + result8);
            logger.log("");
            logger.log("DONE!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
