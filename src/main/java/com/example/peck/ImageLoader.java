package com.example.peck;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.peck.config.Constants.TILE_SIZE;

public class ImageLoader {
    /**
     * Scales down the resource image
     * @param imagePath Path to image file
     * @return ImageView of the Image
     */
    public static ImageView loadAndScaleImage(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
