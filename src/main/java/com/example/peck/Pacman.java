package com.example.peck;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class Pacman extends MovingObjects {

    //Pacman Attributes
    public enum Edible {SMALL, BIG, CHERRY, GHOST}
    public Edible edible;
    private Direction desiredDirection;
    private int score;
    private int lives;
    private final ImageView nowall_img = new ImageView("/map/blackTile.png");
    private final ImageView cherries_img = new ImageView("/map/cherry.png");
    public int dotsEaten = 0;

    public boolean scatterMode= false;

    public int scatterTimer=0;

    public Font deathFont = Font.loadFont(getClass().getResourceAsStream("/fonts/emulogic.ttf"), 64);


    //Constructor
    public Pacman(String skin) {
        super(skin);
        this.correspondingChar = 'P';
        this.desiredDirection = Direction.NONE;
        this.score = 0;
        this.lives = 3;
        this.edible = Edible.SMALL;
    }

    //Movement
    @Override
    public void move(char[] levelData, ImageView[][] tileView) {

        this.levelData = levelData;
        this.tileView = tileView;

        if (canMoveInDirection(desiredDirection)) {
            direction = desiredDirection;
        }
        setNextPos();
        char nextPos = this.levelData[(this.posY * Gameboard.GRID_WIDTH) + this.posX];
        if (nextPos != 'E') {
            switch(nextPos) {
                case 'B' -> edible = Edible.SMALL;
                case 'S' -> edible = Edible.BIG;
                case 'F' -> edible = Edible.CHERRY;
            }
            eat(this.posX, this.posY);
        }
        //death(this.ghostPos);
    }

    private boolean canMoveInDirection(Direction direction) {

        int nextX = posX;
        int nextY = posY;

        switch (direction) {
            case UP -> nextY = Math.floorMod(posY - 1, Gameboard.GRID_HEIGHT);
            case DOWN -> nextY = Math.floorMod(posY + 1, Gameboard.GRID_HEIGHT);
            case LEFT -> nextX = Math.floorMod(posX - 1, Gameboard.GRID_WIDTH);
            case RIGHT -> nextX = Math.floorMod(posX + 1, Gameboard.GRID_WIDTH);
            case NONE -> {
                return false;
            }
        }

        return canMove(nextX, nextY);
    }

    /**
     * Handles keyboard input to change the direction of Pacman.
     *
     * @param keyCode The KeyCode of the pressed key.
     */
    public void handleInput(KeyCode keyCode) {
        switch (keyCode) {
            case UP,W -> desiredDirection = Direction.UP;
            case DOWN,S -> desiredDirection = Direction.DOWN;
            case LEFT,A -> desiredDirection = Direction.LEFT;
            case RIGHT,D -> desiredDirection = Direction.RIGHT;
        }
    }

    /**
     * Checks if the Ghosts hit Pacman
     * Deducts one live if true
     * Are no lives left then game over
     */

    public void death(Stage stage) {
        Media death = new Media(new File("src/main/resources/sounds/death.mp3").toURI().toString());
        MediaPlayer deathPlayer = new MediaPlayer(death);
        deathPlayer.play();

        if (lives > 1) {
            this.lives -= 1;
            this.posX = startPosX;
            this.posY = startPosY;
        }
        else stage.setScene(deathScreen(stage));
    }

    private Scene deathScreen(Stage stage) {
        PacmanGame.timeline.stop();
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("win");

        Text text = new Text("You Lose!");
        text.setFont(deathFont);
        text.setFill(Color.YELLOW);

        Button menuButton = new Button("RETRY");
        menuButton.setFont(deathFont);
        menuButton.getStyleClass().add("customButton");

        Button exitButton = new Button(" EXIT ");
        exitButton.setFont(deathFont);
        exitButton.getStyleClass().add("customButton");

        VBox buttonBox = new VBox(100);
        buttonBox.getChildren().addAll(text, menuButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(buttonBox);

        Scene deathScene = new Scene(borderPane, Gameboard.GRID_WIDTH * Gameboard.TILE_SIZE,
                (Gameboard.GRID_HEIGHT * Gameboard.TILE_SIZE) + 40);

        deathScene.getStylesheets().add("styles.css");

        menuButton.setOnAction(event -> {
            stage.setScene(Menu.menuScene);
        });

        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        return deathScene;
    }


    /**
     * When a small dot is eaten, update only the corresponding ImageView
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void eat(int x, int y) {
        dotsEaten++;

        // Sound
        Media chomp = new Media(new File("src/main/resources/sounds/chomp.mp3").toURI().toString());
        MediaPlayer chompPlayer = new MediaPlayer(chomp);

        Media food = new Media(new File("src/main/resources/sounds/fruit.mp3").toURI().toString());
        MediaPlayer foodPlayer = new MediaPlayer(food);

        char aktElement = levelData[(y * Gameboard.GRID_WIDTH) + x];

        if (aktElement == 'F') foodPlayer.play();
        else chompPlayer.play();

        updateScore();
        levelData[(y * Gameboard.GRID_WIDTH) + x] = 'E'; // Replace the SMALLDOT with empty space
        ImageView tileView = this.tileView[y][x];
        tileView.setImage(nowall_img.getImage()); // Update the image to nowall

        if(edible== Edible.BIG){
            scatterMode=true;
            scatterTimer=30;
        }
    }

    public void placeFood() {
        levelData[(17 * Gameboard.GRID_WIDTH) + 14] = 'F'; // Replace the SMALLDOT with empty space
        ImageView tileView = this.tileView[17][14];
        tileView.setImage(cherries_img.getImage()); // Update the image to nowall
    }

    //Get-Methods
    public void updateScore() {
        switch (this.edible) {
            case SMALL -> score++;
            case BIG -> score += 10;
            case CHERRY -> score += 100;
            case GHOST -> score +=50;
        }
    }

    public int getScore() {
        return this.score;
    }

    public int getLives() {
        return this.lives;
    }

}
