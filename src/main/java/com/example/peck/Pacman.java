package com.example.peck;

import com.example.peck.config.CurrentUser;
import com.example.peck.database.DatabaseHelper;
import com.example.peck.ui.DeathScreenWindow;
import com.example.peck.ui.PacmanGameWindow;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

import static com.example.peck.config.Constants.*;
import static com.example.peck.config.Variables.*;

public class Pacman extends MovingObjects {

    DeathScreenWindow deathScreenWindow;

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

    private MediaPlayer foodPlayer, sirenPlayer, deathPlayer;


    //Constructor
    public Pacman(String skin, DeathScreenWindow deathScreenWindow) {
        super(skin);
        this.deathScreenWindow = deathScreenWindow;
        this.correspondingChar = 'P';
        this.desiredDirection = Direction.NONE;
        this.score = CurrentUser.score;
        this.lives = 3;
        this.edible = Edible.SMALL;
        initializeMedia();
    }

    //Intilialize Media
    private void initializeMedia() {
        // Sound
        Media food = new Media(new File("src/main/resources/sounds/fruit.mp3").toURI().toString());
        foodPlayer = new MediaPlayer(food);
        applySoundSettings(foodPlayer);

        Media sound = new Media(new File("src/main/resources/sounds/siren.mp3").toURI().toString());
        sirenPlayer = new MediaPlayer(sound);
        applySoundSettings(sirenPlayer);
 
        Media death = new Media(new File("src/main/resources/sounds/death.mp3").toURI().toString());
        deathPlayer = new MediaPlayer(death);
        applySoundSettings(deathPlayer);
    }

    private void applySoundSettings(MediaPlayer player) {
        if (soundsMuted) {
            player.setVolume(0.0);
        } else {
            player.setVolume(soundsVolume);
        }
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
        char nextPos = this.levelData[(this.posY * GRID_WIDTH) + this.posX];
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

    @Override
    protected void updateTarget() {}

    private boolean canMoveInDirection(Direction direction) {

        int nextX = posX;
        int nextY = posY;

        switch (direction) {
            case UP -> nextY = Math.floorMod(posY - 1, GRID_HEIGHT);
            case DOWN -> nextY = Math.floorMod(posY + 1, GRID_HEIGHT);
            case LEFT -> nextX = Math.floorMod(posX - 1, GRID_WIDTH);
            case RIGHT -> nextX = Math.floorMod(posX + 1, GRID_WIDTH);
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
        deathPlayer.seek(deathPlayer.getStartTime());
        deathPlayer.play();

        if (lives > 1) {
            this.lives -= 1;
            this.posX = startPosX;
            this.posY = startPosY;
        } else {
            CurrentUser.score = 0;
            DatabaseHelper.updateHighScore(CurrentUser.username, score);
            PacmanGameWindow.timeline.stop();
            // Switch to the death screen
            Platform.runLater(() -> stage.setScene(deathScreenWindow.getScene()));
        }
    }



    /**
     * When a small dot is eaten, update only the corresponding ImageView
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void eat(int x, int y) {
        dotsEaten++;

        char aktElement = levelData[(y * GRID_WIDTH) + x];

        if (aktElement == 'F') {
            foodPlayer.seek(foodPlayer.getStartTime());
            foodPlayer.play();
        }
        else {
            Media chomp = new Media(new File("src/main/resources/sounds/chomp.mp3").toURI().toString());
            MediaPlayer chompPlayer = new MediaPlayer(chomp);
            applySoundSettings(chompPlayer);
            chompPlayer.seek(chompPlayer.getStartTime());
            chompPlayer.play();
        }

        updateScore();
        levelData[(y * GRID_WIDTH) + x] = 'E'; // Replace the SMALLDOT with empty space
        ImageView tileView = this.tileView[y][x];
        tileView.setImage(nowall_img.getImage()); // Update the image to nowall

        if(edible== Edible.BIG){
            scatterMode=true;
            scatterTimer=30;
            sirenPlayer.seek(sirenPlayer.getStartTime());
            sirenPlayer.play();
        }
    }

    public void placeFood() {
        levelData[(17 * GRID_WIDTH) + 14] = 'F'; // Replace the SMALLDOT with empty space
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
