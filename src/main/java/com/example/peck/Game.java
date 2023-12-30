package com.example.peck;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Game extends Application {

    private final int TILE_SIZE = 22; // 22x22 pixels
    private final int GRID_WIDTH = 28; // 28x31 Tiles
    private final int GRID_HEIGHT = 31;
    private ImageView pacmanUp, pacmanDown, pacmanLeft, pacmanRight, current;
    private Image bigdot, smalldot, wall, nowall;
    private GridPane gameBoard;
    private int pacmanX = 13;
    private int pacmanY = 17;
    private enum Direction {UP, DOWN, LEFT, RIGHT, NONE}
    private Direction direction = Direction.NONE;


    @Override
    public void start(Stage primaryStage) throws IOException {

        loadImages();
        gameBoard = drawMap();

        Scene scene = new Scene(gameBoard, GRID_WIDTH*TILE_SIZE, GRID_HEIGHT*TILE_SIZE);
        scene.setOnKeyPressed(event -> handleInput(event.getCode()));
        primaryStage.setTitle("Pac-Man Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    public void startGame() {
        new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 200_000_000) {
                    movePacman();
                    gameBoard = drawPacman(gameBoard);
                    lastUpdate = now;
                }
            }
        }.start();
    }

    public void handleInput(KeyCode keyCode) {
        switch (keyCode) {
            case UP -> direction = Direction.UP;
            case DOWN -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.LEFT;
            case RIGHT -> direction = Direction.RIGHT;
        }
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
        pacmanDown = new ImageView("down.gif");
        pacmanUp = new ImageView("up.gif");
        pacmanLeft = new ImageView("left.gif");
        pacmanRight = new ImageView("right.gif");
        current = new ImageView("none.png");
        bigdot = new Image("bigdot.png");
        smalldot = new Image("smalldot.png");
        wall = new Image("wall.png");
        nowall = new Image("nowall.png");
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
                ImageView imageView = new ImageView();
                GridPane.setColumnIndex(imageView, col);
                GridPane.setRowIndex(imageView, row);
                switch (level[(row * GRID_WIDTH) + col]) {
                    case '1' -> imageView.setImage(wall);
                    case '0' -> imageView.setImage(smalldot);
                    case 'p' -> imageView.setImage(bigdot);
                    default -> imageView.setImage(nowall);
                }
                gp.getChildren().add(imageView);
            }
        }
        return gp;
    }

    public GridPane drawPacman(GridPane gridPane) {

        ImageView imageView = switch (direction) {
            case UP -> pacmanUp;
            case DOWN -> pacmanDown;
            case LEFT -> pacmanLeft;
            case RIGHT -> pacmanRight;
            case NONE -> current;
        };

        gridPane.getChildren().remove(current);
        GridPane.setRowIndex(imageView, pacmanY);
        GridPane.setColumnIndex(imageView, pacmanX);
        current = imageView;
        gridPane.getChildren().add(current);

        return gridPane;
    }

    public static void main(String[] args) {
        launch();
    }
}