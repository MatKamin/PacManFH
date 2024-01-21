package com.example.peck.ghosts;

import com.example.peck.MovingObjects;
import com.example.peck.Pacman;


/**
 * Represents the ghost Clyde in the game.
 */
public class Clyde extends MovingObjects {

    private Pacman pacMan;

    private int[] scatterPoint = {0, 31};


    /**
     * Constructs a Clyde instance with a reference to Pacman.
     *
     * @param pm The Pacman instance Clyde will interact with.
     */
    public Clyde(Pacman pm) {
        super("ghosts/clyde.gif");
        this.correspondingChar = '3';
        this.pacMan = pm;
        firstMoves = 2;
    }

    /**
     * Updates Clyde's target position based on Pacman's position, distance from Clyde,
     * or scatter point if the scatter mode is on.
     */
    @Override
    protected void updateTarget() {
        if (!pacMan.scatterMode) {
            int distancePacManX = Math.abs(Math.abs(this.posX - pacMan.getPosX()));
            int distancePacManY = Math.abs(Math.abs(this.posY - pacMan.getPosY()));
            if (distancePacManX <= 8 && distancePacManY <= 8) {
                this.targetX = scatterPoint[0];
                this.targetY = scatterPoint[1];
            } else {
                this.targetX = pacMan.getPosX();
                this.targetY = pacMan.getPosY();
            }
        } else {
            this.targetX = scatterPoint[0];
            this.targetY = scatterPoint[1];
        }
    }


    /**
     * Resets Clyde to its starting position upon death.
     */
    public void death() {
        this.posX = startPosX;
        this.posY = startPosY;
        firstMoves = 2;
    }
}
