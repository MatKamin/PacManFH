package com.example.peck.ui;

import com.example.peck.config.CurrentUser;
import com.example.peck.database.DatabaseHelper;
import com.example.peck.util.AlertUtil;
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

/**
 * Represents the login window of the application.
 */
public class LoginWindow extends Window {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button confirmButton;

    /**
     * Constructs a LoginWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the login screen.
     */
    public LoginWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the login scene with input fields and confirm button.
     *
     * @return A Scene object representing the login screen.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");

        Text title = new Text("Login");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");

        Text escape = new Text("Esc to return");
        escape.setFont(Font.font("Emulogic", 20));
        borderPane.setBottom(escape);
        escape.getStyleClass().add("escape");


        StackPane titleContainer = new StackPane(title);
        titleContainer.getStyleClass().add("titleContainer");
        borderPane.setTop(titleContainer);

        VBox inputLayout = createInputLayout();
        borderPane.setCenter(inputLayout);

        Scene scene = new Scene(borderPane, 750, 821);
        scene.getStylesheets().add("styles.css");

        setupEscapeKey(scene);

        return scene;
    }

    /**
     * Initializes the buttons for the login screen.
     */
    @Override
    protected void initializeButtons() {
        confirmButton = new Button("CONFIRM");
        confirmButton.setFont(emulogicFont);
        confirmButton.getStyleClass().add("customButton");

        confirmButton.setOnAction(event -> handleLogin());
    }

    /**
     * Handles the login action when the confirm button is clicked.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean loginSuccess = DatabaseHelper.checkLogin(username, password);
        if (loginSuccess) {
            CurrentUser.username = username;
            GameMenuWindow gameMenuWindow = new GameMenuWindow(stage, getScene());
            stage.setScene(gameMenuWindow.getScene());
        } else {
            AlertUtil.showAlert("Login Error", "Invalid username or password. Please try again.");
        }
    }

    /**
     * Creates and returns the input layout for the login screen.
     *
     * @return VBox containing the input fields and confirm button.
     */
    private VBox createInputLayout() {
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("customTextInput");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("customTextInput");

        initializeButtons();

        // Set onAction for both fields to trigger confirm button
        usernameField.setOnAction(event -> confirmButton.fire());
        passwordField.setOnAction(event -> confirmButton.fire());

        VBox inputLayout = new VBox(45, usernameField, passwordField, confirmButton);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.setMaxWidth(502);

        return inputLayout;
    }
}

