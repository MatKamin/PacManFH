package com.example.peck;

import javafx.scene.image.ImageView;

import java.util.Random;

import static com.example.peck.config.Constants.*;

/**
 * Abstract class representing moving objects in the game.
 */
public abstract class MovingObjects {

    //General Attributes
    public static Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    public Direction direction;
    protected final String SKIN;
    protected char correspondingChar;
    protected char[] levelData;
    protected ImageView[][] tileView;
    protected int posX;
    protected int posY;
    protected int startPosX;
    protected int startPosY;

    protected int targetX;
    protected int targetY;
    protected int firstMoves;

    //Constructor
    public MovingObjects(String skin) {
        this.direction = Direction.NONE;
        this.SKIN = skin;
    }


    public void move(char[] levelData, ImageView[][] tileView) {
        this.levelData = levelData;
        this.tileView = tileView;

        //Prevents the ghost from staying in the box
        if (firstMoves > 0) {
            this.direction = Direction.UP;
            setNextPos();
            firstMoves--;
        } else {
            updateTarget(); // Update target position every move
            calculateBestMove(); // Calculate the best move using Pythagorean theorem
            setNextPos(); // Set the next position based on the best move
        }
    }

    protected abstract void updateTarget();

    //Movement
    protected boolean canMove(int nextX, int nextY) {
        char positionChar = this.levelData[(nextY * GRID_WIDTH) + nextX];
        for (char wall : WALLS) {
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
            case UP -> nextY = Math.floorMod(posY - 1, GRID_HEIGHT);
            case DOWN -> nextY = Math.floorMod(posY + 1, GRID_HEIGHT);
            case LEFT -> nextX = Math.floorMod(posX - 1, GRID_WIDTH);
            case RIGHT -> nextX = Math.floorMod(posX + 1, GRID_WIDTH);
        }
        if(canMove(nextX,nextY)){
            this.posX = nextX;
            this.posY = nextY;
        }
    }

    public void setInitialPosition(char[] lvlData) {
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (lvlData[(row * GRID_WIDTH) + col] == this.correspondingChar) {
                    this.posX = col;
                    this.startPosX = col;
                    this.posY = row;
                    this.startPosY = row;
                    return;
                }
            }
        }
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

    public void death() {
    }


    private boolean isOppositeDirection(Direction newDirection, Direction currentDirection) {
        return (newDirection == Direction.UP && currentDirection == Direction.DOWN) ||
                (newDirection == Direction.DOWN && currentDirection == Direction.UP) ||
                (newDirection == Direction.LEFT && currentDirection == Direction.RIGHT) ||
                (newDirection == Direction.RIGHT && currentDirection == Direction.LEFT);
    }

    protected void calculateBestMove() {
        double shortestDistance = Double.MAX_VALUE;
        Direction bestDirection = Direction.NONE; // Default to no movement

        // Check each direction and calculate distance to the target
        for (Direction dir : MovingObjects.directions) {
            // Skip the opposite direction of the current movement
            if (isOppositeDirection(dir, this.direction)) {
                continue;
            }

            int nextX = this.posX;
            int nextY = this.posY;

            // Adjust next position based on direction
            switch (dir) {
                case UP -> nextY--;
                case DOWN -> nextY++;
                case LEFT -> nextX--;
                case RIGHT -> nextX++;
            }

            // Calculate distance using Pythagorean theorem
            double distance = Math.sqrt(Math.pow(nextX - targetX, 2) + Math.pow(nextY - targetY, 2));

            // Checks if the next field is not the box, if true skip this direction
            if ((nextX == 14 && nextY == 12) || (nextX == 15 && nextY == 12)) {
                continue;
            }

            // Check if movement is possible and if the calculated distance is shorter
            if (canMove(nextX, nextY) && distance < shortestDistance) {
                shortestDistance = distance;
                bestDirection = dir;
            }
        }

        this.direction = bestDirection; // Set the best direction as the new direction
    }

}
