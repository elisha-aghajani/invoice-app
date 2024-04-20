package controllers;

import database.SQLiteDatabase;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.Constants;
import utils.Password;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RegisterController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    public void registerButtonAction() {
        errorLabel.setText("");
        errorLabel.setTextFill(Constants.RED);

        if (!allFieldsValid()) {
            showError("Error: Missing field(s)");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Error: Password's do not match");
            return;
        }

        if (SQLiteDatabase.isUser(usernameField.getText())) {
            showError("Error: Username already exists");
            return;
        }

        if (!SQLiteDatabase.insertUser(usernameField.getText(), passwordField.getText(), emailField.getText())) {
            showError("Error: Failed to register");
            return;
        }

        reset();
        errorLabel.setTextFill(Constants.GREEN);
        showError("Successfully registered!");
    }

    @FXML
    public void backButtonAction() {
        reset();
        Scene scene = sceneManager.loadScene("/scenes/login.fxml");
        sceneManager.switchScene(scene);
    }

    private void showError(String errorText) {
        errorLabel.setText(errorText);
    }

    private boolean allFieldsValid() {
        return !usernameField.getText().isEmpty()
                && !passwordField.getText().isEmpty()
                && !confirmPasswordField.getText().isEmpty()
                && !emailField.getText().isEmpty();
    }

    private void reset() {
        clearAllFields();
        usernameField.requestFocus();
    }

    private void clearAllFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        emailField.clear();
        errorLabel.setText("");
    }
}
