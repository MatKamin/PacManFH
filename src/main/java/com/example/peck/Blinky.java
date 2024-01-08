package com.example.peck;

import javafx.scene.image.ImageView;

public class Blinky extends MovingObjects {
    private int targetX;
    private int targetY;
    private Pacman pacMan;

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

        updateTarget(); // Update target position every move
        calculateBestMove(); // Calculate the best move using Pythagorean theorem

        setNextPos(); // Set the next position based on the best move
    }

    // Update Blinky target position to Pacman's current position
    private void updateTarget() {
        this.targetX = pacMan.getPosX();
        this.targetY = pacMan.getPosY();
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
}
