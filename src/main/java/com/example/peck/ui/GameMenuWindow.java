package com.example.peck.ui;

import com.example.peck.config.CurrentUser;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.peck.config.CurrentUser.*;
import static com.example.peck.config.Fonts.*;
import static com.example.peck.util.AlertUtil.*;

/**
 * Represents the main game menu window.
 */
public class GameMenuWindow extends Window{
    private Button playButton, settingsButton, highscoresButton;

    /**
     * Constructs a GameMenuWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the game menu.
     */
    public GameMenuWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the game menu scene with buttons and layout.
     *
     * @return A Scene object representing the game menu.
     */
    @Override
    protected Scene createScene() {
        // Main Container of the Menu
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");

        // Title with custom font
        Text title = new Text("Pac-Man");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");
        borderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        Text username = new Text("Hello " + CurrentUser.username + "!");
        username.setFont(Font.font("Emulogic", 24));
        username.getStyleClass().add("username");

        VBox titleContainer = new VBox(20);
        titleContainer.getChildren().addAll(title, username);
        titleContainer.getStyleClass().add("titleContainer");
        titleContainer.setAlignment(Pos.CENTER);
        borderPane.setTop(titleContainer);
        BorderPane.setAlignment(titleContainer, Pos.TOP_CENTER);

        initializeButtons();

        // VBox to hold buttons
        VBox buttonBox = new VBox(45);
        buttonBox.getChildren().addAll(playButton, settingsButton, highscoresButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(buttonBox);

        // Scene
        Scene scene = new Scene(borderPane, 750, 821);
        scene.getStylesheets().add("styles.css");

        return scene;
    }

    /**
     * Implementation not needed here
     * @param scene
     */
    @Override
    protected void setupEscapeKey(Scene scene) {}

    /**
     * Initializes the buttons for the game menu.
     */
    @Override
    protected void initializeButtons() {
        playButton = new Button("PLAY");
        playButton.setFont(emulogicFont);
        playButton.getStyleClass().add("customButton");
        playButton.setOnAction(event -> {
            try {
                PacmanGameWindow pacmanGameWindow = new PacmanGameWindow(levelFile, skinFolder, stage);
                stage.setScene(pacmanGameWindow.getGameView());
                pacmanGameWindow.startGame();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to start the game: " + e.getMessage());
            }
        });

        settingsButton = new Button("SETTINGS");
        settingsButton.setFont(emulogicFont);
        settingsButton.getStyleClass().add("customButton");
        settingsButton.setOnAction(event -> {
            SettingsWindow settingsWindow = new SettingsWindow(stage, getScene());
            stage.setScene(settingsWindow.getScene());
        });

        highscoresButton = new Button("HIGHSCORES");
        highscoresButton.setFont(emulogicFont);
        highscoresButton.getStyleClass().add("customButton");
        highscoresButton.setOnAction(event -> {
            HighscoreWindow highscoreWindow = new HighscoreWindow(stage, getScene());
            stage.setScene(highscoreWindow.getScene());
        });
    }
}
