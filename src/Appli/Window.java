package Appli;

public class Window {
    private int width = 120;
    private int height = 20;
    private String title;
    private int currentLine = 0;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

    }

    // 默认构造函数
    public Window() {
        this.width = 120;
        this.height = 10;
        this.title = "  Quiz System";
    }

    public void top() {
        printHorizontalLines();
        printTitle();

        System.out.print("|");
        for (int i = 0; i < (width - 2); i++) {
            System.out.print("-");
        }
        System.out.print("|");

        System.out.println();
    }

    private void printHorizontalLines() {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private void printTitle() {
        System.out.print("|");
        System.out.print(title);

        for (int i = 0; i < (width - 2 - title.length()); i++) {
            System.out.print(" ");
        }
        System.out.print("|");
        System.out.println();
    }

    public void printContent(String content) {

        if (currentLine < (height - 4)) {
            System.out.print("| " + content);
            for (int i = 0; i < (width - 4 - content.length() + 1); i++) {
                System.out.print(" ");
            }
            System.out.print("|");
            System.out.println();

            currentLine++;
        } else {
            clear();
            printContent(content);
        }
    }

    private void printEmptyLine() {
        System.out.print("|");
        for (int j = 0; j < (width - 2); j++) {
            System.out.print(" ");
        }
        System.out.print("|");
        System.out.println();
        currentLine++;
    }


    public void bottom() {

        while (currentLine < height - 4) {
            printEmptyLine();
        }
        printHorizontalLines();

        // 空三行
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    // 清空窗口
    public void clear() {
        currentLine = 0; // 重置当前行计数

    }


}
