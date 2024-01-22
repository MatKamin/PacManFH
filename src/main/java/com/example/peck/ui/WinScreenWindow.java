package com.example.peck.ui;

import com.example.peck.config.*;
import com.example.peck.config.Fonts;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.peck.config.Constants.*;
import static com.example.peck.config.CurrentUser.levelFile;
import static com.example.peck.config.CurrentUser.skinFolder;
import static com.example.peck.util.AlertUtil.showAlert;


/**
 * Represents the win screen window displayed when a player wins the game.
 */
public class WinScreenWindow extends Window {
    private Button nextLevelButton, exitButton;

    /**
     * Constructs a WinScreenWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the win screen.
     */
    public WinScreenWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }


    /**
     * Creates the win screen scene with a victory message and navigation buttons.
     *
     * @return A Scene object representing the win screen.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("win");

        Text text = new Text("You Win!");
        text.setFont(Fonts.emulogicFont);
        text.setFill(Color.YELLOW);

        initializeButtons();

        VBox buttonBox = new VBox(100);
        buttonBox.getChildren().addAll(text, nextLevelButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(buttonBox);

        Scene winScene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        winScene.getStylesheets().add("styles.css");

        return winScene;
    }


    /**
     * Initializes the buttons for the win screen.
     * Includes a button to go back to the game menu and another to exit the application.
     */
    @Override
    protected void initializeButtons() {

        nextLevelButton = new Button("NEXT LEVEL");
        nextLevelButton.setFont(Fonts.emulogicFont);
        nextLevelButton.getStyleClass().add("customButton");
        nextLevelButton.setOnAction(event -> {
            // Determine the next level file based on the current level
            String currentLevelFile = CurrentUser.levelFile;
            String nextLevelFile = getNextLevelFile(currentLevelFile);

            // Start a new game with the next level and the same score
            try {
                startNewGame(nextLevelFile, CurrentUser.score);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        exitButton = new Button("EXIT");
        exitButton.setFont(Fonts.emulogicFont);
        exitButton.getStyleClass().add("customButton");
        exitButton.setOnAction(e -> Platform.exit());
    }

    /**
     * Gets the file path for the next level based on the current level file.
     *
     * @param currentLevelFile The file path of the current level.
     * @return The file path of the next level.
     */
    private String getNextLevelFile(String currentLevelFile) {
        // Extract the level number from the current level file
        String[] parts = currentLevelFile.split("_");
        int currentLevelNumber = Integer.parseInt(parts[1].split("\\.")[0]);

        // Determine the next level number and construct the next level file path
        int nextLevelNumber = currentLevelNumber + 1;
        return "/levels/level_" + nextLevelNumber + ".txt";
    }

    /**
     * Starts a new game with the specified level file and score.
     *
     * @param levelFile The file path of the level to start.
     * @param score     The score to carry over to the new game.
     */
    private void startNewGame(String levelFile, int score) throws IOException {
        try {
            PacmanGameWindow pacmanGameWindow = new PacmanGameWindow(levelFile, skinFolder, stage);
            stage.setScene(pacmanGameWindow.getGameView());
            pacmanGameWindow.startGame();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to start the game: " + e.getMessage());
        }
    }

}

