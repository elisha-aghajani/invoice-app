package controllers;

import database.SQLiteDatabase;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController extends BaseController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
            event.consume();
        }
    }

    @FXML
    public void loginButtonAction() {
        if (!allFieldsValid()) {
            showError("Error: Missing field(s)");
            return;
        }

        if (!SQLiteDatabase.validate(usernameField.getText(), passwordField.getText())) {
            showError("Error: Invalid username or password");
            return;
        }

        String username = usernameField.getText();
        reset();

        Scene scene = sceneManager.loadScene("/scenes/app.fxml");
        AppController controller = (AppController) sceneManager.getCurrentController();
        controller.setUserId(SQLiteDatabase.getUserId(username));
        controller.setup();
        sceneManager.switchScene(scene);
    }

    @FXML
    public void registerButtonAction() {
        reset();
        Scene scene = sceneManager.loadScene("/scenes/register.fxml");
        sceneManager.switchScene(scene);
    }

    private void showError(String errorText) {
        errorLabel.setText(errorText);
    }

    private boolean allFieldsValid() {
        return !usernameField.getText().isEmpty()
                && !passwordField.getText().isEmpty();
    }

    private void reset() {
        clearAllFields();
        usernameField.requestFocus();
    }

    private void clearAllFields() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setText("");
    }
}
