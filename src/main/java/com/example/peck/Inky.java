package com.example.peck;

import javafx.scene.image.ImageView;

public class Inky extends  MovingObjects {

    public Inky() {
        super("ghosts/inky.gif");
        this.correspondingChar = '4';
    }

    @Override
    public void move(char[] levelData, ImageView[][] tileView) {

        this.levelData = levelData;
        this.tileView = tileView;

        this.direction = getRandomDirection();
        setNextPos();
    }

}
