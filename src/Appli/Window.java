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

    // Default constructor
    public Window() {
        this.width = 120;
        this.height = 10;
        this.title = "  Quiz System";
    }

    // Print the top part of the window
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

    // Print horizontal lines for the window
    private void printHorizontalLines() {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // Print the title of the window
    private void printTitle() {
        System.out.print("|");
        System.out.print(title);

        for (int i = 0; i < (width - 2 - title.length()); i++) {
            System.out.print(" ");
        }
        System.out.print("|");
        System.out.println();
    }

    // Print content in the window
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

    // Print an empty line in the window
    private void printEmptyLine() {
        System.out.print("|");
        for (int j = 0; j < (width - 2); j++) {
            System.out.print(" ");
        }
        System.out.print("|");
        System.out.println();
        currentLine++;
    }

    // Print the bottom part of the window
    public void bottom() {
        while (currentLine < height - 4) {
            printEmptyLine();
        }
        printHorizontalLines();

        // Print three empty lines
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    // Clear the window
    public void clear() {
        currentLine = 0; // Reset the current line count
    }
}
