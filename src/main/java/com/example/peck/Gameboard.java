package com.example.peck;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Gameboard {

    //GridPane Attributes
    private final GridPane gameBoard;
    public static final int TILE_SIZE = 25;
    public static final int GRID_WIDTH = 30;
    public static final int GRID_HEIGHT = 31;

    //GridPane reference and ImageViews
    private ImageView[][] tileViews = new ImageView[GRID_HEIGHT][GRID_WIDTH];
    private ImageView pacmanUp_img, pacmanDown_img, pacmanLeft_img, pacmanRight_img, current_img,
            bigdot_img, smalldot_img, nowall_img, blinky_img, pinky_img, clyde_img, inky_img,
            railHorizontal_img, railUpRight_img, railUpLeft_img, railRightUp_img, railLeftUp_img, cherries_img, railVertical_img;

    //Level
    public static final char[] WALLS = {'H', 'R', 'L', 'U', 'D', 'V'};
    private char[] levelData;

    //Moving Objects
    public Pacman pacman;
    private Inky inky;
    private Blinky blinky;
    private Pinky pinky;
    private Clyde clyde;

    //Ghosts
    private int numberOfGhosts = 1;
    private int[][] ghostPos;
    private static final Random PNRG = new Random();
    Direction[] randomDirection = {Direction.UP, Direction.UP, Direction.UP, Direction.UP};
    boolean[] cage = {false, false, false, false};
    private int counter;



    //Gameboard Constructor
    public Gameboard(String level, String skin) {
        this.counter = 0;
        this.pacman = new Pacman(skin);
        this.blinky = new Blinky();
        this.inky = new Inky();
        this.clyde = new Clyde();
        this.pinky = new Pinky();
        initializeImages();
        levelData = readLevel(level);
        this.gameBoard = drawMap(levelData);
    }



    //GameBoard Methods (includes gameboard creation, game updates and ghost movement)
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
     * Initializes all image resources used in the game.
     */
    private void initializeImages() {
        String pacmanSkin = this.pacman.getSKIN();
        pacmanDown_img = loadAndScaleImage("pacman/" + pacmanSkin + "/pacmanDown.gif");
        pacmanUp_img = loadAndScaleImage("pacman/" + pacmanSkin + "/pacmanUp.gif");
        pacmanLeft_img = loadAndScaleImage("pacman/" + pacmanSkin + "/pacmanLeft.gif");
        pacmanRight_img = loadAndScaleImage("pacman/" + pacmanSkin + "/pacmanRight.gif");
        current_img = loadAndScaleImage("pacman/" + pacmanSkin + "/none.png");
        bigdot_img = loadAndScaleImage("map/bigDot.png");
        smalldot_img = loadAndScaleImage("map/smallDot.png");
        nowall_img = loadAndScaleImage("map/blackTile.png");
        blinky_img = loadAndScaleImage(this.blinky.getSKIN());
        pinky_img = loadAndScaleImage(this.pinky.getSKIN());
        clyde_img = loadAndScaleImage(this.clyde.getSKIN());
        inky_img = loadAndScaleImage(this.inky.getSKIN());
        railHorizontal_img = loadAndScaleImage("map/railHorizontal.png");
        railUpRight_img = loadAndScaleImage("map/railUpRight.png");
        railUpLeft_img = loadAndScaleImage("map/railUpLeft.png");
        railRightUp_img = loadAndScaleImage("map/railRightUp.png");
        railLeftUp_img = loadAndScaleImage("map/railLeftUp.png");
        cherries_img = loadAndScaleImage("map/cherry.png");
        railVertical_img = loadAndScaleImage("map/railVertical.png");
    }

    /**
     * Reads the level layout from a file and converts it to a character array.
     * @return A char array representing the level layout.
     */
    private char[] readLevel(String level) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = getClass().getResourceAsStream(level);
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            reader.lines().forEach(stringBuilder::append);
        }
        return stringBuilder.toString().toCharArray();
    }

    /**
     * Creates the initial game map (grid) based on level data.
     *
     * @return A GridPane representing the game map.
     */
    private GridPane drawMap(char[] levelData) {
        GridPane gp = new GridPane();
        Map<Character, ImageView> imageMap = new HashMap<>();
        imageMap.put('S', bigdot_img);
        imageMap.put('B', smalldot_img);
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
     * Updates the moving Objects whenever called
     */
    public void update() {
        moveGhost();
        this.pacman.updateGhostPos(this.ghostPos);
        this.pacman.move(this.levelData, this.tileViews);
        tileViews = this.pacman.tileView;
        levelData = this.pacman.levelData;
        draw();
        counter++;
    }

    /**
     * Draws Pacman and the ghosts at the current position on the game board.
     */
    private void draw() {
        ImageView newPacman = switch (this.pacman.direction) {
            case UP -> pacmanUp_img;
            case DOWN -> pacmanDown_img;
            case LEFT -> pacmanLeft_img;
            case RIGHT -> pacmanRight_img;
            default -> current_img;
        };

        gameBoard.getChildren().remove(current_img);
        GridPane.setRowIndex(newPacman, this.pacman.getPosY());
        GridPane.setColumnIndex(newPacman, this.pacman.getPosX());
        current_img = newPacman;
        gameBoard.getChildren().add(current_img);

        //Blinky - red
        gameBoard.getChildren().remove(blinky_img);
        GridPane.setRowIndex(blinky_img, ghostPos[0][1]);
        GridPane.setColumnIndex(blinky_img, ghostPos[0][0]);
        gameBoard.getChildren().add(blinky_img);

        //Clyde - yellow
        gameBoard.getChildren().remove(clyde_img);
        GridPane.setRowIndex(clyde_img, ghostPos[2][1]);
        GridPane.setColumnIndex(clyde_img, ghostPos[2][0]);
        gameBoard.getChildren().add(clyde_img);

        //blue
        gameBoard.getChildren().remove(inky_img);
        GridPane.setRowIndex(inky_img, ghostPos[3][1]);
        GridPane.setColumnIndex(inky_img, ghostPos[3][0]);
        gameBoard.getChildren().add(inky_img);
        //pink
        gameBoard.getChildren().remove(pinky_img);
        GridPane.setRowIndex(pinky_img, ghostPos[1][1]);
        GridPane.setColumnIndex(pinky_img, ghostPos[1][0]);
        gameBoard.getChildren().add(pinky_img);

    }

    /**
     * Sets pacmans initial position based on the level layout
     */
    public void setInitialPacmanPosition() {
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (levelData[(row * GRID_WIDTH) + col] == 'P') {
                    this.pacman.setPosX(col);
                    this.pacman.setStartPosX(col);
                    this.pacman.setPosY(row);
                    this.pacman.setStartPosY(row);
                    return;
                }
            }
        }
    }

    /**
     * Sets the beginning position of the Ghosts based on the level layout
     */
    public void setInitialGhostPositions() {
        ghostPos = new int[4][2];
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                char x = levelData[(row * GRID_WIDTH) + col];
                switch (x) {
                    case '1':
                        ghostPos[0][0] = col;
                        ghostPos[0][1] = row;
                        break;
                    case '2':
                        ghostPos[1][0] = col;
                        ghostPos[1][1] = row;
                        break;
                    case '3':
                        ghostPos[2][0] = col;
                        ghostPos[2][1] = row;
                        break;
                    case '4':
                        ghostPos[3][0] = col;
                        ghostPos[3][1] = row;
                        break;
                }

            }
        }
    }

    /**
     * Generates based on, in which directions the ghost can move,  a random value out of the enum Direction, except the value NONE
     *
     * @return the generated Direction
     */
    private Direction getRandomDirection(int ghostid) {
        List<Direction> directions = new ArrayList<>();
        char[] fields = {levelData[((ghostPos[ghostid][1] - 1) * GRID_WIDTH) + ghostPos[ghostid][0]],
                levelData[((ghostPos[ghostid][1] + 1) * GRID_WIDTH) + ghostPos[ghostid][0]],
                levelData[(ghostPos[ghostid][1] * GRID_WIDTH) + ghostPos[ghostid][0] - 1],
                levelData[(ghostPos[ghostid][1] * GRID_WIDTH) + ghostPos[ghostid][0] + 1]
        };
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != 'D') {
                directions.add(Direction.values()[i]);
            }
        }
        Direction d = directions.get(PNRG.nextInt(directions.size()));
        return d;
    }

    /**
     * Moves the ghosts randomly
     */
    private void moveGhost() {
        if (counter > 100 * numberOfGhosts && numberOfGhosts < ghostPos.length) {
            numberOfGhosts++;
        }
        for (int i = 0; i < numberOfGhosts; i++) {
            int nextX = ghostPos[i][0];
            int nextY = ghostPos[i][1];

            switch (randomDirection[i]) {
                case UP -> nextY = Math.floorMod(ghostPos[i][1] - 1, GRID_HEIGHT);
                case DOWN -> nextY = Math.floorMod(ghostPos[i][1] + 1, GRID_HEIGHT);
                case LEFT -> nextX = Math.floorMod(ghostPos[i][0] - 1, GRID_WIDTH);
                case RIGHT -> nextX = Math.floorMod(ghostPos[i][0] + 1, GRID_WIDTH);
            }
            if (this.canMove(nextX, nextY)) {

                ghostPos[i][0] = nextX;
                ghostPos[i][1] = nextY;

            } else {

                if (ghostPos[i][1] == 11 && ghostPos[i][0] == 14 || ghostPos[i][0] == 15) {
                    randomDirection[i] = getRandomDirection(i);
                    while (randomDirection[i] == Direction.DOWN) {
                        randomDirection[i] = getRandomDirection(i);
                    }
                }
                randomDirection[i] = getRandomDirection(i);
                moveGhost();
            }
        }
    }

    /**
     *
     * @param nextX next pos on X
     * @param nextY next pos on Y
     * @return when movement is possible returns true
     */
    //Duplicate because the ghost movement happens solely in this class
    public boolean canMove(int nextX, int nextY) {
        char positionChar = this.levelData[(nextY * GRID_WIDTH) + nextX];
        for (char wall : WALLS) {
            if (positionChar == wall) {
                return false; // The position matches a wall character
            }
        }
        return true; // No match found, movement is possible
    }

    //Get the GridPane for Scene creation
    public GridPane getGameBoard() {
        return this.gameBoard;
    }
}
