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

    public char[] getLevelData() {
        return levelData;
    }
    private char[] levelData;

    //Moving Objects
    public Pacman pacman;
    private Inky inky;
    private Blinky blinky;
    private Pinky pinky;
    private Clyde clyde;
    private ImageView[] ghosts = new ImageView[4];
    private MovingObjects[] ghostObjects = new MovingObjects[4];//{inky, blinky, pinky, clyde};
    //Ghosts

    //Gameboard Constructor
    public Gameboard(String level, String skin) {
        this.pacman = new Pacman(skin);
        this.blinky = new Blinky(pacman);
        this.inky = new Inky();
        this.clyde = new Clyde();
        this.pinky = new Pinky();
        initializeImages();
        levelData = readLevel(level);
        this.gameBoard = drawMap(levelData);
        //Spass bitte bring mich um warum is das so !=!=!!??!?!
        InitializeArrays();
    }
    /**
     * Initialize arrays needed for
     */
    private void InitializeArrays() {
        ghosts[0] = inky_img;
        ghosts[1] = blinky_img;
        ghosts[2] = clyde_img;
        ghosts[3] = pinky_img;
        ghostObjects[0] = inky;
        ghostObjects[1] = blinky;
        ghostObjects[2] = clyde;
        ghostObjects[3] = pinky;
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
        this.pacman.move(this.levelData, this.tileViews);
        tileViews = this.pacman.tileView;
        levelData = this.pacman.levelData;
        moveGhosts();
        draw();
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
        int cnt = 0;
        for (ImageView ghostImgView : ghosts){
            gameBoard.getChildren().remove(ghostImgView);
            GridPane.setRowIndex(ghostImgView, ghostObjects[cnt].posY);
            GridPane.setColumnIndex(ghostImgView, ghostObjects[cnt].posX);
            gameBoard.getChildren().add(ghostImgView);
            cnt++;
        }

    }
    /**
     * Sets the beginning position of the Ghosts based on the level layout
     */
    public void setInitialGhostPositions() {
        this.blinky.setInitialPosition(levelData);
        this.clyde.setInitialPosition(levelData);
        this.inky.setInitialPosition(levelData);
        this.pinky.setInitialPosition(levelData);
    }
    /**
     * Moves ghosts in the grid
     */
    public void moveGhosts(){

        for (MovingObjects ghostobj : ghostObjects){
            ghostobj.move(this.levelData, this.tileViews);
            tileViews = ghostobj.tileView;
            levelData = ghostobj.levelData;
            if(checkIfEntityCollision(ghostobj)){
                this.pacman.death();
            }
        }
    }
    /**
     * Checks if pacman collides with a ghost
     */
    private boolean checkIfEntityCollision(MovingObjects ghosty) {
        return ghosty.getPosX() == pacman.getPosX() && ghosty.getPosY() == pacman.getPosY();
    }

    //Get the GridPane for Scene creation
    public GridPane getGameBoard() {
        return this.gameBoard;
    }
}
