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
    private int posX;
    private int posY;
    private int startPosX;
    private int startPosY;
    private int[][] ghostPos;
    private final ImageView nowall_img = new ImageView("/map/blackTile.png");


    //Constructor
    public Pacman(String skin) {
        super(skin);
        this.correspondingChar = 'P';
        this.desiredDirection = Direction.NONE;
        this.score = 0;
        this.lives = 3;
        this.edible = Edible.SMALL;
    }

    //Set corresponding direction
    public void handleInput(KeyCode keyCode) {
        switch (keyCode) {
            case UP -> desiredDirection = Direction.UP;
            case DOWN -> desiredDirection = Direction.DOWN;
            case LEFT -> desiredDirection = Direction.LEFT;
            case RIGHT -> desiredDirection = Direction.RIGHT;
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

        int nextX = posX;
        int nextY = posY;

        switch (direction) {
            case UP -> nextY = Math.floorMod(posY - 1, Gameboard.GRID_HEIGHT);
            case DOWN -> nextY = Math.floorMod(posY + 1, Gameboard.GRID_HEIGHT);
            case LEFT -> nextX = Math.floorMod(posX - 1, Gameboard.GRID_WIDTH);
            case RIGHT -> nextX = Math.floorMod(posX + 1, Gameboard.GRID_WIDTH);
        }

        if (canMove(nextX, nextY)) {
            char nextPos = this.levelData[(nextY * Gameboard.GRID_WIDTH) + nextX];
            if (nextPos != 'E') {
                switch(nextPos) {
                    case 'B' -> edible = Edible.SMALL;
                    case 'S' -> edible = Edible.BIG;
                    case 'F' -> edible = Edible.CHERRY;
                }
                eat(nextX, nextY);
            }
            posX = nextX;
            posY = nextY;
        }
        death(this.ghostPos);
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
     * Checks if the Ghosts hit Pacman
     * Deducts one live if true
     * Are no lives left then game over
     */
    private void death(int[][] ghostPos) {
        for (int i = 0; i < ghostPos.length; i++) {
            if (ghostPos[i][0] == this.posX && ghostPos[i][1] == this.posY) {
                try {
                    System.out.println("P: " + this.posX + " " + this.posY + " G" + i + ": " + ghostPos[i][0] + " " + ghostPos[i][1]);
                    if (lives > 1) {
                        this.lives -= 1;
                        this.posX = startPosX;
                        this.posY = startPosY;
                    }
                    else Platform.exit();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }



    /**
     * When a small dot is eaten, update only the corresponding ImageView
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void eat(int x, int y) {
        updateScore();
        levelData[(y * Gameboard.GRID_WIDTH) + x] = 'E'; // Replace the SMALLDOT with empty space
        ImageView tileView = this.tileView[y][x];
        tileView.setImage(nowall_img.getImage()); // Update the image to nowall
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

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    //Set-Methods
    public void setPosX(int newPosX) {
        this.posX = newPosX;
    }

    public void setPosY(int newPosY) {
        this.posY = newPosY;
    }

    public void updateGhostPos(int[][] newPos) {
        this.ghostPos = newPos;
    }

    public void setStartPosX(int newPosX) {
        this.startPosX = newPosX;
    }

    public void setStartPosY(int newPosY) {
        this.startPosY = newPosY;
    }
}
