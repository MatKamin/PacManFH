package com.example.peck;

import javafx.scene.image.ImageView;

public abstract class MovingObjects {

    //General Attributes
    protected Direction direction;
    protected final String SKIN;
    protected char correspondingChar;
    protected char[] levelData;
    protected ImageView[][] tileView;

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

    //get-Methods
    public final String getSKIN() {
        return this.SKIN;
    }

    public final Direction getDirection() {
        return this.direction;
    }
    public final char getCorrespondingChar() {
        return this.correspondingChar;
    }

}
