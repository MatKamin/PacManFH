package com.example.peck;

import javafx.scene.image.ImageView;

public class Pinky extends MovingObjects {

    public Pinky() {
        super("ghosts/pinky.gif");
        this.correspondingChar = '2';
    }

    @Override
    public void move(char[] levelData, ImageView[][] tileView) {}

}
