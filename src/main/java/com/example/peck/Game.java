package com.example.peck;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
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

public class Game extends Application {

    private final int TILE_SIZE = 22; // 22x22 pixels
    private final int GRID_WIDTH = 28; // 28x31 Tiles
    private final int GRID_HEIGHT = 31;
    private final int ENTITY_SIZE = TILE_SIZE;
    private ImageView upImageView, downImageView, leftImageView, rightImageView, ghost2, ghost3, ghost1, ghost4, current;
    private GridPane grid;
    private int pacmanX = 13;
    private int pacmanY = 17;
    private enum Direction {UP, DOWN, LEFT, RIGHT, NONE}
    private Direction direction = Direction.NONE;


    @Override
    public void start(Stage primaryStage) throws IOException {
        // ACTION ENUM für Pacman/Spieler mit Up/Down/left/right?
        loadImages();
        grid = drawMap();
        Scene scene = new Scene(grid, GRID_WIDTH*TILE_SIZE, GRID_HEIGHT*TILE_SIZE);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> direction = Direction.UP;
                case DOWN -> direction = Direction.DOWN;
                case LEFT -> direction = Direction.LEFT;
                case RIGHT -> direction = Direction.RIGHT;
            }
        });
        primaryStage.setTitle("Pac-Man Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 200_000_000) {
                    movePacman();
                    grid = drawPacman(grid);
                    lastUpdate = now;
                }
            }
        }.start();
    }

    public void movePacman() {
        switch (direction) {
            case UP -> pacmanY = (pacmanY - 1 + GRID_HEIGHT) % GRID_HEIGHT;
            case DOWN -> pacmanY = (pacmanY + 1) % GRID_HEIGHT;
            case LEFT -> pacmanX = (pacmanX - 1 + GRID_WIDTH) % GRID_WIDTH;
            case RIGHT -> pacmanX = (pacmanX + 1) % GRID_WIDTH;
        }
    }

    private void loadImages() {
        //GIF 22px
        downImageView = new ImageView("down.gif");
        upImageView = new ImageView("up.gif");
        leftImageView = new ImageView("left.gif");
        rightImageView = new ImageView("right.gif");
        // ghost1 = new ImageView("");
        // ghost2 = new ImageView("");
        // ghost3 = new ImageView("");
        // ghost4 = new ImageView("");
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
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE);
                if(level[(row * GRID_WIDTH) + col] == '1'){
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

    public GridPane drawPacman(GridPane gridPane) {
        try {
            if (current != null) {
                gridPane.getChildren().remove(current);
            }
        } catch (Exception e) {
            // Handle exception if needed
        }

        ImageView imageView = switch (direction) {
            case UP -> upImageView;
            case DOWN -> downImageView;
            case LEFT -> leftImageView;
            case RIGHT -> rightImageView;
            case NONE -> upImageView;
        };

        GridPane.setRowIndex(imageView, pacmanY);
        GridPane.setColumnIndex(imageView, pacmanX);

        if (imageView != null) {
            current = imageView;
            gridPane.getChildren().add(current);
        }

        return gridPane;
    }


    public static void main(String[] args) {
        launch();
    }
}