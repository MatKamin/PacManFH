package com.example.peck.ui;

import com.example.peck.Gameboard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static com.example.peck.config.Constants.*;

/**
 * Represents the main game window for Pac-Man.
 */
public class PacmanGameWindow {

    //Scene Elements
    private static final int LABEL_AREA_HEIGHT = 40;
    public static Timeline timeline;

    private Label scoreLabel;
    private Label livesLabel;
    private final Gameboard gameboard;
    private final Scene gameView;
    private final Stage stage;


    /**
     * Constructs a PacmanGameWindow for a new game.
     *
     * @param level The level file path.
     * @param skin The skin folder name.
     * @param stage The primary stage of the application.
     * @throws IOException If there is an issue loading the level or skin.
     */
    public PacmanGameWindow(String level, String skin, Stage stage) throws IOException {
        WinScreenWindow winScreenWindow = new WinScreenWindow(stage, null);
        DeathScreenWindow deathScreenWindow = new DeathScreenWindow(stage, null);
        this.gameboard = new Gameboard(level, skin, winScreenWindow, deathScreenWindow);
        this.gameView = initializeScene();
        this.stage = stage;
    }

    /**
     * Initializes the game scene with all UI components.
     *
     * @return A Scene object representing the game view.
     */
    private Scene initializeScene() {
        //Containers
        VBox gameVBox = new VBox();
        gameVBox.getStyleClass().add("gameScene");
        HBox labelContainer = new HBox();
        labelContainer.setPadding(new Insets(5));

        // Create and configure the score and lives display
        scoreLabel = new Label("Score: " + this.gameboard.pacman.getScore());
        scoreLabel.getStyleClass().add("gameHUD");
        scoreLabel.setMinHeight(LABEL_AREA_HEIGHT);
        scoreLabel.setAlignment(Pos.CENTER);

        livesLabel = new Label("Lives: " + this.gameboard.pacman.getLives());
        livesLabel.getStyleClass().add("gameHUD");
        livesLabel.setMinHeight(LABEL_AREA_HEIGHT);
        livesLabel.setAlignment(Pos.CENTER);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        labelContainer.getChildren().addAll(scoreLabel, spacer, livesLabel);
        gameVBox.getChildren().addAll(labelContainer, this.gameboard.getGameBoard());

        //Create Scene and set style
        Scene scene = new Scene(gameVBox, WINDOW_WIDTH, (WINDOW_HEIGHT) + LABEL_AREA_HEIGHT);
        scene.getStylesheets().add("styles.css"); // Apply external CSS
        scene.setOnKeyPressed(event -> this.gameboard.pacman.handleInput(event.getCode()));

        return scene;
    }


    /**
     * Starts the game with initial setup and game loop.
     */
    public void startGame() {
        this.gameboard.pacman.setInitialPosition(this.gameboard.getLevelData());
        this.gameboard.setInitialGhostPositions();
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(130), e -> gameUpdate()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        timeline = gameLoop;
        gameLoop.play();
    }

    /**
     * Updates the game state and UI elements on each frame.
     */
    private void gameUpdate() {
        updateLabels();
        this.gameboard.update(stage);
    }

    /**
     * Updates the score and lives labels.
     */
    private void updateLabels() {
        scoreLabel.setText("Score: " + this.gameboard.pacman.getScore());
        livesLabel.setText("Lives: " + this.gameboard.pacman.getLives());
    }



    /**
     * Gets the game view Scene.
     *
     * @return The Scene object for the game view.
     */
    public Scene getGameView() {
        return this.gameView;
    }
}
