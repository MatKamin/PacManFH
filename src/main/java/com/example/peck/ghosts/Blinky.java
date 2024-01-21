package com.example.peck.ghosts;

import com.example.peck.MovingObjects;
import com.example.peck.Pacman;

/**
 * Represents the ghost Blinky in the game.
 */
public class Blinky extends MovingObjects {

    private Pacman pacMan;
    private int[] scatterPoint = {31, 0};

    /**
     * Creates a Blinky instance with a reference to Pacman.
     *
     * @param pm The Pacman instance Blinky will interact with.
     */
    public Blinky(Pacman pm) {
        super("ghosts/blinky.gif");
        firstMoves = 3;
        this.correspondingChar = '1';
        this.pacMan = pm;
        updateTarget();
    }

    /**
     * Updates Blinky's target position based on Pacman's position or scatter point.
     */
    protected void updateTarget() {
        if (!pacMan.scatterMode) {
            targetX = pacMan.getPosX();
            targetY = pacMan.getPosY();
        } else {
            targetX = scatterPoint[0];
            targetY = scatterPoint[1];
        }
    }

    /**
     * Resets Blinky to its starting position upon death.
     */
    public void death() {
        this.posX = startPosX;
        this.posY = startPosY;
        firstMoves = 3;
    }
}
