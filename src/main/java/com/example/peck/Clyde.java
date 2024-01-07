package com.example.peck;

import javafx.scene.image.ImageView;

public class Clyde extends MovingObjects {

    public Clyde() {
        super("ghosts/clyde.gif");
        this.correspondingChar = '3';
    }

    @Override
    public void move(char[] levelData, ImageView[][] tileView) {

        this.levelData = levelData;
        this.tileView = tileView;

        this.direction = getRandomDirection();
        setNextPos();
    }

}
