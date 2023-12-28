package com.example.pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Game extends Application {

    private final int TILE_SIZE = 25; // 16x16 pixels
    private final int GRID_SIZE = 31; // 31x31 Tiles
    private final int MAP_SIZE = TILE_SIZE * GRID_SIZE;
    private final int ENTITY_SIZE = TILE_SIZE - 5;
    private ImageView upImageView,downImageView,leftImageView,rightImageView, ghost2, ghost3, ghost1, ghost4;
    private GridPane grid;


    @Override
    public void start(Stage primaryStage) throws IOException {
        // ACTION ENUM für Pacman/Spieler mit Up/Down/left/right?
        loadImages();
        grid = drawMap();
        Scene scene = new Scene(grid, MAP_SIZE, MAP_SIZE);
        primaryStage.setTitle("Pac-Man Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadImages() {
        downImageView = loadImage("/down.gif");
        upImageView = loadImage("/up.gif");
        leftImageView = loadImage("/left.gif");
        rightImageView = loadImage("/right.gif");
        ghost1 = loadImage("");
        ghost2 = loadImage("");
        ghost3 = loadImage("");
        ghost4 = loadImage("");
    }
    private ImageView loadImage(String path) {
        return new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
    }

    private char[] readLevel() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/level.txt");
        if (inputStream == null) {
            throw new IOException("File not found");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString().toCharArray();
        } catch (IOException e) {
            throw new IOException("Error reading from file "+ e);
        }
    }
    private GridPane drawMap() throws IOException {
        char[] level = readLevel();
        GridPane gp = new GridPane();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE);
                if(level[(row * GRID_SIZE) + col] == '1'){
                    rectangle.setFill(Color.BLUE);
                }else {
                    rectangle.setFill(Color.BLACK);
                }
                Text text = new Text();
                text.setText(String.valueOf(col+row));
                StackPane stackPane = new StackPane(rectangle, text);
                gp.add(stackPane, col, row);
            }
        }
        return gp;
    }

    public static void main(String[] args) {
        launch();
    }
}