package com.example.peck;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Game extends Application {

    private static final int TILE_SIZE = 25;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 31;

    private static final int SCORE_AREA_HEIGHT = 40;
    private static final int LIVES_AREA_HEIGHT = 40;

    private static final int WIDTH = GRID_WIDTH * TILE_SIZE;
    private static final int HEIGHT = (GRID_HEIGHT * TILE_SIZE) + SCORE_AREA_HEIGHT + LIVES_AREA_HEIGHT;

    private static final char[] WALLS = {'H', 'R', 'L', 'U', 'D', 'F', 'V'};


    private ImageView[][] tileViews = new ImageView[GRID_HEIGHT][GRID_WIDTH];
    private static final String LEVEL_FILE = "/level.txt";

    private ImageView pacmanUp_img, pacmanDown_img, pacmanLeft_img, pacmanRight_img, current_img, bigdot_img, smalldot_img, nowall_img, blinky_img, pinky_img, clyde_img, inky_img, railHorizontal_img, railUpRight_img, railUpLeft_img, railRightUp_img, railLeftUp_img, cherries_img, railVertical_img;
    private GridPane gameBoard;
    private char[] levelData;
    private int pacmanX = 13;
    private int pacmanY = 17;

    private int score = 0;
    private Label scoreLabel;

    private enum Direction {UP, DOWN, LEFT, RIGHT, NONE}
    private Direction direction = Direction.NONE;

    /**
     * Starts the JavaFX application, setting up the stage and scene.
     *
     * @param primaryStage The primary stage for this application.
     * @throws IOException If reading level data fails.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        initializeImages();
        levelData = readLevel();
        setInitialPacmanPosition();
        gameBoard = drawMap();

        gameBoard.setLayoutY(SCORE_AREA_HEIGHT); // Shift the game board down

        VBox root = new VBox(); // Create a container for the entire layout

        // Create and configure the score display
        scoreLabel = new Label("Score: " + score);
        scoreLabel.setMinHeight(SCORE_AREA_HEIGHT);
        scoreLabel.setAlignment(Pos.CENTER);

        // Add the scoreLabel and gameBoard to the root container
        root.getChildren().addAll(scoreLabel, gameBoard);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add("styles.css"); // Apply external CSS
        scene.setOnKeyPressed(event -> handleInput(event.getCode()));

        primaryStage.setTitle("Pac-Man Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        startGame();
    }

    /**
     * Initializes all image resources used in the game.
     */
    private void initializeImages() {
        pacmanDown_img = loadAndScaleImage("pacmanDown.gif");
        pacmanUp_img = loadAndScaleImage("pacmanUp.gif");
        pacmanLeft_img = loadAndScaleImage("pacmanLeft.gif");
        pacmanRight_img = loadAndScaleImage("pacmanRight.gif");
        current_img = loadAndScaleImage("none.png");
        bigdot_img = loadAndScaleImage("bigdot.png");
        smalldot_img = loadAndScaleImage("smalldot.png");
        nowall_img = loadAndScaleImage("nowall.png");
        blinky_img = loadAndScaleImage("blinky.gif");
        pinky_img = loadAndScaleImage("pinky.gif");
        clyde_img = loadAndScaleImage("clyde.gif");
        inky_img = loadAndScaleImage("inky.gif");
        railHorizontal_img = loadAndScaleImage("railHorizontal.png");
        railUpRight_img = loadAndScaleImage("railUpRight.png");
        railUpLeft_img = loadAndScaleImage("railUpLeft.png");
        railRightUp_img = loadAndScaleImage("railRightUp.png");
        railLeftUp_img = loadAndScaleImage("railLeftUp.png");
        cherries_img = loadAndScaleImage("cherries.png");
        railVertical_img = loadAndScaleImage("railVertical.png");
    }

    /**
     * Scales down the resource image
     * @param imagePath Path to image file
     * @return ImageView of the Image
     */
    private ImageView loadAndScaleImage(String imagePath) {

        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Reads the level layout from a file and converts it to a character array.
     *
     * @return A char array representing the level layout.
     * @throws IOException If the level file is not found or cannot be read.
     */
    private char[] readLevel() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(LEVEL_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            if (inputStream == null) {
                throw new IOException("Level file not found");
            }
            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(stringBuilder::append);
            return stringBuilder.toString().toCharArray();
        }
    }


    /**
     * Creates the initial game map (grid) based on level data.
     *
     * @return A GridPane representing the game map.
     */
    private GridPane drawMap() {
        GridPane gp = new GridPane();
        Map<Character, ImageView> imageMap = new HashMap<>();
        imageMap.put('S', bigdot_img);
        imageMap.put('B', smalldot_img);
        imageMap.put('1', blinky_img);
        imageMap.put('2', pinky_img);
        imageMap.put('3', clyde_img);
        imageMap.put('4', inky_img);
        imageMap.put('E', nowall_img);
        imageMap.put('H', railHorizontal_img);
        imageMap.put('R', railUpRight_img);
        imageMap.put('V', railVertical_img);
        imageMap.put('F', cherries_img);
        imageMap.put('L', railUpLeft_img);
        imageMap.put('U', railRightUp_img);
        imageMap.put('D', railLeftUp_img);


        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                ImageView imageView = new ImageView();
                ImageView imageFromMap = imageMap.getOrDefault(levelData[(row * GRID_WIDTH) + col], nowall_img);
                if (imageFromMap != null) {
                    imageView.setImage(imageFromMap.getImage());
                }
                imageView.setFitWidth(TILE_SIZE);
                imageView.setFitHeight(TILE_SIZE);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                GridPane.setColumnIndex(imageView, col);
                GridPane.setRowIndex(imageView, row);
                gp.getChildren().add(imageView);

                tileViews[row][col] = imageView; // Store the reference to the ImageView
            }
        }
        return gp;
    }

    /**
     * Uodates the score
     */
    private void updateScore() {
        score++;
        scoreLabel.setText("Score: " + score);
    }

    /**
     * When a small dot is eaten, update only the corresponding ImageView
     * @param x x coordinate
     * @param y y coordinate
     */
    private void eatSmallDot(int x, int y) {
        updateScore();
        levelData[(y * GRID_WIDTH) + x] = 'E'; // Replace the SMALLDOT with empty space
        ImageView tileView = tileViews[y][x];
        tileView.setImage(nowall_img.getImage()); // Update the image to nowall
    }

    /**
     * Starts the main game loop using an AnimationTimer.
     */
    private void startGame() {
        new AnimationTimer() {
            long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 200_000_000) {
                    movePacman();
                    drawPacman();
                    lastUpdate = now;
                }
            }
        }.start();
    }

    /**
     * Handles keyboard input to change the direction of Pacman.
     *
     * @param keyCode The KeyCode of the pressed key.
     */
    private void handleInput(KeyCode keyCode) {
        switch (keyCode) {
            case UP -> direction = Direction.UP;
            case DOWN -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.LEFT;
            case RIGHT -> direction = Direction.RIGHT;
        }
    }

    /**
     * Updates Pacman's position based on the current direction.
     */
    private void movePacman() {
        int nextX = pacmanX;
        int nextY = pacmanY;

        switch (direction) {
            case UP -> nextY = Math.floorMod(pacmanY - 1, GRID_HEIGHT);
            case DOWN -> nextY = Math.floorMod(pacmanY + 1, GRID_HEIGHT);
            case LEFT -> nextX = Math.floorMod(pacmanX - 1, GRID_WIDTH);
            case RIGHT -> nextX = Math.floorMod(pacmanX + 1, GRID_WIDTH);
        }

        if (canMove(nextX, nextY)) {
            char nextPos = levelData[(nextY * GRID_WIDTH) + nextX];
            if (nextPos == 'B') {
                eatSmallDot(nextX, nextY);
            }
            pacmanX = nextX;
            pacmanY = nextY;
        }
    }

    /**
     * Sets Pacmans initial position based on the level.txt
     */
    private void setInitialPacmanPosition() {
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (levelData[(row * GRID_WIDTH) + col] == 'P') {
                    pacmanX = col;
                    pacmanY = row;
                    return;
                }
            }
        }
    }

    /**
     * Draws Pacman at the current position on the game board.
     */
    private void drawPacman() {
        ImageView newPacman = switch (direction) {
            case UP -> pacmanUp_img;
            case DOWN -> pacmanDown_img;
            case LEFT -> pacmanLeft_img;
            case RIGHT -> pacmanRight_img;
            default -> pacmanRight_img;
        };

        gameBoard.getChildren().remove(current_img);
        GridPane.setRowIndex(newPacman, pacmanY);
        GridPane.setColumnIndex(newPacman, pacmanX);
        current_img = newPacman;
        gameBoard.getChildren().add(current_img);
    }

    /**
     * Checks if Pacman can move to a specified position.
     *
     * @param nextX The x-coordinate of the next position.
     * @param nextY The y-coordinate of the next position.
     * @return True if the move is possible, false otherwise.
     */
    private boolean canMove(int nextX, int nextY) {
        char positionChar = levelData[(nextY * GRID_WIDTH) + nextX];
        for (char wall : WALLS) {
            if (positionChar == wall) {
                return false; // The position matches a wall character
            }
        }
        return true; // No match found, movement is possible
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
