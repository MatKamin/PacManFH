package com.example.peck;

import javafx.scene.image.ImageView;

public class Blinky extends MovingObjects {

    public Blinky() {
        super("ghosts/blinky.gif");
        this.correspondingChar = '1';
    }

    @Override
    public void move(char[] levelData, ImageView[][] tileView) {}

}
