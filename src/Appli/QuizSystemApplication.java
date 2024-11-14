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

        LoadingStage loadingStage = new LoadingStage();
        loadingStage.start(primaryStage);


    }

    public static void main(String[] args) {
        launch(args); // 启动应用
    }
}
