package com.example.peck.ui;

import com.example.peck.config.Fonts;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.peck.config.Constants.*;

/**
 * Represents the main menu window of the application.
 */
public class MainMenuWindow extends Window {

    private Text title;
    private Button loginButton, registerButton;

    /**
     * Constructs a MainMenuWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the main menu.
     */
    public MainMenuWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the main menu scene with title and buttons.
     *
     * @return A Scene object representing the main menu.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");

        title = new Text("Pac-Man");
        title.setFont(Fonts.pacmanFont);
        title.getStyleClass().add("title");

        StackPane titleContainer = new StackPane(title);
        titleContainer.getStyleClass().add("titleContainer");
        borderPane.setTop(titleContainer);
        BorderPane.setAlignment(titleContainer, Pos.TOP_CENTER);

        initializeButtons();

        VBox buttonBox = new VBox(45, loginButton, registerButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(buttonBox);

        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("styles.css");

        return scene;
    }

    /**
     * Not needed here
     * @param scene
     */
    @Override
    protected void setupEscapeKey(Scene scene) {}

    /**
     * Initializes the buttons for the main menu.
     */
    @Override
    protected void initializeButtons() {
        loginButton = new Button("LOGIN");
        loginButton.setFont(Fonts.emulogicFont);
        loginButton.getStyleClass().add("customButton");
        loginButton.setOnAction(event -> {
            LoginWindow loginWindow = new LoginWindow(stage, getScene());
            stage.setScene(loginWindow.getScene());
        });

        registerButton = new Button("REGISTER");
        registerButton.setFont(Fonts.emulogicFont);
        registerButton.getStyleClass().add("customButton");
        registerButton.setOnAction(event -> {
            RegistrationWindow registrationWindow = new RegistrationWindow(stage, getScene());
            stage.setScene(registrationWindow.getScene());
        });
    }
}
