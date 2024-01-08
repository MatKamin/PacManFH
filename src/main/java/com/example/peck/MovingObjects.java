package com.example.peck;

import javafx.scene.image.ImageView;

import java.util.Random;

public abstract class MovingObjects {

    //General Attributes
    static Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    protected Direction direction;
    protected final String SKIN;
    protected char correspondingChar;
    protected char[] levelData;
    protected ImageView[][] tileView;
    protected int posX;
    protected int posY;
    protected int startPosX;
    protected int startPosY;

    //Constructor
    public MovingObjects(String skin) {
        this.direction = Direction.NONE;
        this.SKIN = skin;
    }


    //must-be-implemented by subclasses
    public abstract void move(char[] levelData, ImageView[][] tileView);

    //Movement
    protected boolean canMove(int nextX, int nextY) {
        char positionChar = this.levelData[(nextY * Gameboard.GRID_WIDTH) + nextX];
        for (char wall : Gameboard.WALLS) {
            if (positionChar == wall) {
                return false; // The position matches a wall character
            }
        }
        return true; // No match found, movement is possible
    }

    protected void setNextPos(){
        int nextX = this.posX;
        int nextY = this.posY;
        switch (this.direction) {
            case UP -> nextY = Math.floorMod(posY - 1, Gameboard.GRID_HEIGHT);
            case DOWN -> nextY = Math.floorMod(posY + 1, Gameboard.GRID_HEIGHT);
            case LEFT -> nextX = Math.floorMod(posX - 1, Gameboard.GRID_WIDTH);
            case RIGHT -> nextX = Math.floorMod(posX + 1, Gameboard.GRID_WIDTH);
        }
        if(canMove(nextX,nextY)){
            this.posX = nextX;
            this.posY = nextY;
        }
    }

    public void setInitialPosition(char[] lvlData) {
        for (int row = 0; row < Gameboard.GRID_HEIGHT; row++) {
            for (int col = 0; col < Gameboard.GRID_WIDTH; col++) {
                if (lvlData[(row * Gameboard.GRID_WIDTH) + col] == this.correspondingChar) {
                    this.posX = col;
                    this.startPosX = col;
                    this.posY = row;
                    this.startPosY = row;
                    return;
                }
            }
        }
    }

    protected Direction getRandomDirection(){
        return directions[new Random().nextInt(4)];
    }

    //get-Methods
    public final String getSKIN() {
        return this.SKIN;
    }


    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

}
