package com.example.peck.ghosts;

import com.example.peck.Direction;
import com.example.peck.MovingObjects;
import com.example.peck.Pacman;

/**
 * Represents the ghost Pinky in the game.
 */
public class Pinky extends MovingObjects {

    private Pacman pacMan;


    private int[] scatterPoint = {0, 0};


    /**
     * Constructs a Pinky instance with a reference to Pacman.
     *
     * @param pm The Pacman instance Pinky will interact with.
     */
    public Pinky(Pacman pm) {
        super("ghosts/pinky.gif");
        this.correspondingChar = '2';
        this.pacMan = pm;
        firstMoves = 3;
    }

    /**
     * Updates Pinky's target position based on Pacman's position and direction,
     * or scatter point if the scatter mode is on.
     */
    protected void updateTarget() {
        if (!pacMan.scatterMode) {
            this.targetX = pacMan.getPosX();
            this.targetY = pacMan.getPosY();
            Direction direction1 = pacMan.direction;
            switch (direction1) {
                case UP -> this.targetY -= 4;
                case DOWN -> this.targetY += 4;
                case LEFT -> this.targetX -= 4;
                case RIGHT -> this.targetX += 4;

            }
        } else {
            this.targetX = scatterPoint[0];
            this.targetY = scatterPoint[1];
        }
    }

    /**
     * Resets Pinky to its starting position upon death.
     */
    public void death() {
        this.posX = startPosX;
        this.posY = startPosY;
        firstMoves = 3;
    }

}
