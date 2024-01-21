package com.example.peck.ghosts;

import com.example.peck.Direction;
import com.example.peck.MovingObjects;
import com.example.peck.Pacman;

/**
 * Represents the ghost Inky in the game.
 */
public class Inky extends MovingObjects {

    private Pacman pacMan;

    private Blinky blinky;

    private int[] scatterPoint = {31, 31};



    /**
     * Constructs an Inky instance with references to Pacman and Blinky.
     *
     * @param pm The Pacman instance Inky will interact with.
     * @param blinky The Blinky instance used to calculate Inky's target position.
     */
    public Inky(Pacman pm, Blinky blinky) {
        super("ghosts/inky.gif");
        this.correspondingChar = '4';
        this.pacMan = pm;
        this.blinky = blinky;
        firstMoves = 2;
    }

    /**
     * Updates Inky's target position based on Pacman's position and Blinky's position,
     * or scatter point if scatter mode is on.
     */
    protected void updateTarget() {
        if (!pacMan.scatterMode) {
            this.targetX = pacMan.getPosX();
            this.targetY = pacMan.getPosY();
            Direction direction1 = pacMan.direction;
            switch (direction1) {
                case UP -> this.targetY -= 2;
                case DOWN -> this.targetY += 2;
                case LEFT -> this.targetX -= 2;
                case RIGHT -> this.targetX += 2;

            }
            int difX = Math.abs(Math.abs(blinky.getPosX() - targetX));
            int difY = Math.abs(Math.abs(blinky.getPosY() - targetY));

            this.targetX += difX;
            this.targetY += difY;
        } else {
            this.targetX = scatterPoint[0];
            this.targetY = scatterPoint[1];
        }
    }

    /**
     * Resets Inky to its starting position upon death.
     */
    public void death() {
        this.posX = startPosX;
        this.posY = startPosY;
        firstMoves = 2;
    }
}
