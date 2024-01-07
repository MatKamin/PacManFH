package com.example.peck;

import javafx.scene.image.ImageView;

public class Blinky extends MovingObjects {

    private int posX;
    private int posY;
    private int targetX;
    private int targetY;
    private Pacman pacMan;
    public Blinky(Pacman pm) {
        super("ghosts/blinky.gif");
        this.correspondingChar = '1';
        this.pacMan = pm;
        getPacmanPos();
    }
    public void setPos(int posOnX, int posOnY){
        this.posX = posOnX;
        this.posY = posOnY;
    }
    //Blinky moves by looking at pacman position once, and moving towards it until it reaches it and repeats the process.
    @Override
    public void move(char[] levelData, ImageView[][] tileView) {
        //  If target reached, set new target
        if(posX == targetX && posY == targetY){
            getPacmanPos();
        }
        //move towards target || Pathfinding algorithm

    }

    // Set Blinky target position to Pacman position
    private void getPacmanPos() {
        this.targetX = pacMan.getPosX();
        this.targetY = pacMan.getPosY();
    }
}
