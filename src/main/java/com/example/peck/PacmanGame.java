package com.example.peck;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PacmanGame {

    //Scene Elements
    private static final int LABEL_AREA_HEIGHT = 40;


    //Game State
    private Label scoreLabel;
    private Label livesLabel;


    //Components
    private final Gameboard gameboard;
    private final Scene gameView;


    //Constructor for a new game
    public PacmanGame(String level, String skin) {
        this.gameboard = new Gameboard(level, skin);
        this.gameView = initializeScene();
    }

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
        Scene scene = new Scene(gameVBox, Gameboard.GRID_WIDTH*Gameboard.TILE_SIZE, (Gameboard.GRID_HEIGHT*Gameboard.TILE_SIZE) +LABEL_AREA_HEIGHT);
        scene.getStylesheets().add("styles.css"); // Apply external CSS
        scene.setOnKeyPressed(event -> this.gameboard.pacman.handleInput(event.getCode()));

        return scene;
    }


    public void startGame() {
        this.gameboard.pacman.setInitialPosition(this.gameboard.getLevelData());
        this.gameboard.setInitialGhostPositions();
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(150), e -> gameUpdate()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void gameUpdate() {
        updateLabels();
        this.gameboard.update();
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + this.gameboard.pacman.getScore());
        livesLabel.setText("Lives: " + this.gameboard.pacman.getLives());
    }

    public Scene getGameView() {
        return this.gameView;
    }
}
