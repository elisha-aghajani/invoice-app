package application;

import database.SQLiteDatabase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Invoice Application");
        primaryStage.setResizable(false);

        SceneManager sceneManager = new SceneManager(primaryStage);
        Scene scene = sceneManager.loadScene("/scenes/login.fxml");
        sceneManager.switchScene(scene);
    }

    public static void main(String[] args) {
        if (!SQLiteDatabase.initDatabase()) {
            return;
        }
        launch(args);
    }
}