package com.example.peck;

import javafx.scene.image.ImageView;

public class Blinky extends MovingObjects {
    private int targetX;
    private int targetY;
    private Pacman pacMan;

    private int firstMoves = 3;
    private int[] scatterPoint = {31, 0};


    public Blinky(Pacman pm) {
        super("ghosts/blinky.gif");
        this.correspondingChar = '1';
        this.pacMan = pm;
        updateTarget();
    }

    // Blinky moves by constantly updating its target and moving towards it.
    @Override
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

    // Update Blinky target position to Pacman's current position or his scatter point, if the scatter mode is on
    private void updateTarget() {
        if (!pacMan.scatterMode) {
            this.targetX = pacMan.getPosX();
            this.targetY = pacMan.getPosY();
        } else {
            this.targetX = scatterPoint[0];
            this.targetY = scatterPoint[1];
        }
    }

    // Calculate the best move using Pythagorean theorem
    private void calculateBestMove() {
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

    // Check if the direction is the opposite of the current direction
    private boolean isOppositeDirection(Direction newDirection, Direction currentDirection) {
        return (newDirection == Direction.UP && currentDirection == Direction.DOWN) ||
                (newDirection == Direction.DOWN && currentDirection == Direction.UP) ||
                (newDirection == Direction.LEFT && currentDirection == Direction.RIGHT) ||
                (newDirection == Direction.RIGHT && currentDirection == Direction.LEFT);
    }

    //Sets the ghost position to his starting position
    public void death() {
        this.posX = startPosX;
        this.posY = startPosY;
        firstMoves = 3;
    }
}
