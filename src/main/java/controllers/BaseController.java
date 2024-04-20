package controllers;

import application.SceneManager;
import javafx.scene.Scene;

public class BaseController {

    protected SceneManager sceneManager;
    protected Scene scene;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
