package com.example.peck.ui;

import com.example.peck.config.CurrentUser;
import com.example.peck.database.DatabaseHelper;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.peck.config.Fonts.*;
import static com.example.peck.util.AlertUtil.*;
import static com.example.peck.util.PasswordValidator.*;

/**
 * Represents the registration window of the application.
 */
public class RegistrationWindow extends Window {

    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button confirmButton;

    /**
     * Constructs a RegistrationWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the registration screen.
     */
    public RegistrationWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the registration scene with input fields and confirm button.
     *
     * @return A Scene object representing the registration screen.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");

        Text title = new Text("Register");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");

        Text escape = new Text("Esc to return");
        escape.setFont(Font.font("Emulogic", 20));
        borderPane.setBottom(escape);
        escape.getStyleClass().add("escape");

        StackPane titleContainer = new StackPane(title);
        titleContainer.getStyleClass().add("titleContainer");
        borderPane.setTop(titleContainer);

        initializeInputFields();

        VBox inputLayout = new VBox(45, usernameField, passwordField, confirmPasswordField, confirmButton);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.setMaxWidth(502);

        borderPane.setCenter(inputLayout);


        Scene scene = new Scene(borderPane, 750, 821);
        scene.getStylesheets().add("styles.css");

        setupEscapeKey(scene);

        return scene;
    }

    /**
     * Initializes the buttons and their event handlers for the registration screen.
     */
    @Override
    protected void initializeButtons() {
        confirmButton = new Button("CONFIRM");
        confirmButton.setFont(emulogicFont);
        confirmButton.getStyleClass().add("customButton");
        confirmButton.setOnAction(event -> handleRegistration());
    }

    /**
     * Initializes the input fields for the registration screen.
     */
    private void initializeInputFields() {
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("customTextInput");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("customTextInput");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("customTextInput");

        initializeButtons();

        // Set onAction for both fields to trigger confirm button
        usernameField.setOnAction(event -> confirmButton.fire());
        passwordField.setOnAction(event -> confirmButton.fire());
        confirmPasswordField.setOnAction(event -> confirmButton.fire());
    }

    /**
     * Handles the registration process when the confirm button is clicked.
     */
    private void handleRegistration() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Empty Fields", "Please enter both username and password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Password Mismatch", "The passwords do not match. Please try again.");
            return;
        }

        if (password.length() < 8) {
            showAlert("Invalid Password", "Password is too short. It must be at least 8 characters.");
            return;
        }

        if (!password.matches(".*[0-9].*")) {
            showAlert("Invalid Password", "Password is missing a number.");
            return;
        }

        if (!password.matches(".*[a-z].*")) {
            showAlert("Invalid Password", "Password must contain at least one lowercase letter.");
            return;
        }

        if (!password.matches(".*[A-Z].*")) {
            showAlert("Invalid Password", "Password must contain at least one uppercase letter.");
            return;
        }

        if (!password.matches(".*[@#$%^&+=.].*")) {
            showAlert("Invalid Password", "Password must contain at least one special character (@, #, $, %, ^, &, +, =, .).");
            return;
        }



        boolean isRegistered = DatabaseHelper.registerUser(username, password);

        if (isRegistered) {
            CurrentUser.username = username;
            GameMenuWindow gameMenuWindow = new GameMenuWindow(stage, getScene());
            stage.setScene(gameMenuWindow.getScene());
        } else {
            showAlert("Registration Failed", "Username Taken! Please try again.");
        }
    }
}
