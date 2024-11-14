package Appli;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QuizSystemApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个按钮用于打印消息
        Button printButton = new Button("Print Hello World");
        // 创建一个按钮用于退出程序
        Button exitButton = new Button("Exit");

        // 创建一个标签用于显示消息
        Label label = new Label();

        // 设置打印按钮点击事件
        printButton.setOnAction(event -> {
            System.out.println("Hello, World!"); // 在控制台输出消息
            label.setText("Printed 'Hello, World!' to console."); // 更新标签消息
        });

        // 设置退出按钮点击事件
        exitButton.setOnAction(event -> {
            primaryStage.close(); // 关闭窗口
        });

        // 创建一个垂直布局
        VBox root = new VBox(10); // 10像素的间距
        root.getChildren().addAll(printButton, exitButton, label); // 将按钮和标签添加到布局中

        // 创建一个场景，设置大小为1024x978
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show(); // 显示窗口
    }

    public static void main(String[] args) {
        launch(args); // 启动应用
    }
}
