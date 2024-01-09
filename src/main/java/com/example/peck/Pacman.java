package com.example.peck;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Pacman extends MovingObjects {

    //Pacman Attributes
    private enum Edible {SMALL, BIG, CHERRY}
    private Edible edible;
    private Direction desiredDirection;
    private int score;
    private int lives;
    private final ImageView nowall_img = new ImageView("/map/blackTile.png");
    private final ImageView cherries_img = new ImageView("/map/cherry.png");
    public int dotsEaten = 0;


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

    public void death() {
        if (lives > 1) {
            this.lives -= 1;
            this.posX = startPosX;
            this.posY = startPosY;
        }
        else Platform.exit();
    }


    /**
     * When a small dot is eaten, update only the corresponding ImageView
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void eat(int x, int y) {
        dotsEaten++;
        updateScore();
        levelData[(y * Gameboard.GRID_WIDTH) + x] = 'E'; // Replace the SMALLDOT with empty space
        ImageView tileView = this.tileView[y][x];
        tileView.setImage(nowall_img.getImage()); // Update the image to nowall
    }

    public void placeFood() {
        levelData[(17 * Gameboard.GRID_WIDTH) + 14] = 'F'; // Replace the SMALLDOT with empty space
        ImageView tileView = this.tileView[17][14];
        tileView.setImage(cherries_img.getImage()); // Update the image to nowall
    }

    //Get-Methods
    private void updateScore() {
        switch (this.edible) {
            case SMALL -> score++;
            case BIG -> score += 10;
            case CHERRY -> score += 100;
        }
    }

    public int getScore() {
        return this.score;
    }

    public int getLives() {
        return this.lives;
    }

}
