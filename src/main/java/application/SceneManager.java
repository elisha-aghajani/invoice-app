package application;

import controllers.BaseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private final Stage primaryStage;
    private BaseController currentController;
    private final Map<String, Scene> scenes = new HashMap<>();

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BaseController getCurrentController() {
        return currentController;
    }

    public Scene loadScene(String fxmlFile) {
        boolean keyPresent = scenes.containsKey(fxmlFile);
        if (keyPresent) {
            Scene scene = scenes.get(fxmlFile);
            FXMLLoader loader = (FXMLLoader) scene.getUserData();
            this.currentController = loader.getController();
            return scene;
        } else {
            Scene createdScene = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();
                BaseController controller = loader.getController();
                this.currentController = controller;
                controller.setSceneManager(this);
                createdScene = new Scene(root);
                controller.setScene(createdScene);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            return createdScene;
        }

        /*Scene scene = scenes.computeIfAbsent(fxmlFile, file -> {
            Scene createdScene = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
                Parent root = loader.load();
                BaseController controller = loader.getController();
                controller.setSceneManager(this);
                createdScene = new Scene(root);
                controller.setScene(createdScene);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
            return createdScene;
        });

        return scene;*/
    }

    public void switchScene(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
