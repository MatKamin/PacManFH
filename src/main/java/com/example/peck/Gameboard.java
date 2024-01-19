package com.example.peck;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
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

    public Font winFont = Font.loadFont(getClass().getResourceAsStream("/fonts/emulogic.ttf"), 64);

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
    private ImageView[] scaredGhosts = new ImageView[4];
    private MovingObjects[] ghostObjects = new MovingObjects[4];//{inky, blinky, pinky, clyde};
    //Ghosts
    private int ghostnumber=1;
    private boolean scared = false;
    private int counter=0;

    //Gameboard Constructor
    public Gameboard(String level, String skin) throws IOException {
        this.pacman = new Pacman(skin);
        this.blinky = new Blinky(pacman);
        this.inky = new Inky(pacman, blinky);
        this.clyde = new Clyde(pacman);
        this.pinky = new Pinky(pacman);
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
        ghosts[0] = blinky_img;
        ghosts[1] =  inky_img;
        ghosts[2] = clyde_img;
        ghosts[3] = pinky_img;
        ghostObjects[0] = blinky;
        ghostObjects[1] = inky;
        ghostObjects[2] = clyde;
        ghostObjects[3] = pinky;
        scaredGhosts[0] = loadAndScaleImage("ghosts/scared.gif");
        scaredGhosts[1] = loadAndScaleImage("ghosts/scared.gif");
        scaredGhosts[2] = loadAndScaleImage("ghosts/scared.gif");
        scaredGhosts[3] = loadAndScaleImage("ghosts/scared.gif");

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
    private char[] readLevel(String level) throws IOException {
        if (level.isEmpty()) {
            throw new IllegalArgumentException("Level file path is empty.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = getClass().getResourceAsStream(level);

        if (inputStream == null) {
            throw new IllegalArgumentException("Invalid level file: " + level + " does not exist");
        }

        if (inputStream.available() == 0) {
            throw new IllegalArgumentException("Empty level file: " + level);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.lines().forEach(stringBuilder::append);

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
     * Updates the moving objects whenever called
     */
    public void update(Stage stage) {
        // Moves pacman and the ghosts
        this.pacman.move(this.levelData, this.tileViews);
        tileViews = this.pacman.tileView;
        levelData = this.pacman.levelData;
        pacmanCollision(stage);
        moveGhosts(stage);

        // Check if Pac-Man won
        if (won()) stage.setScene(winScreen(stage));

        // Place food when a certain amount of dots has been eaten
        if (this.pacman.dotsEaten == 75 || this.pacman.dotsEaten == 175) placeFood();

        // Draws the objects onto the screen and raises the counter, which acts as a reference to passed time
        draw();
        counter++;

        // Determines the number of active ghosts
        if(counter%(40*ghostnumber)==0 && ghostnumber<ghostObjects.length) ghostnumber++;

        //Timer for Scatter mode
        if(pacman.scatterTimer>=0){
            if(pacman.scatterTimer==0){
                pacman.scatterMode=false;
            }
            else {
                pacman.scatterTimer--;
            }
        }
    }

    /**
     * This Method checks if Pac-Man has already won!
     * @return
     * true: if all dots are eaten
     * false: if there are still dots left
     */
    private boolean won() {
        for (ImageView[] tileView : tileViews) {
            for (ImageView imageView : tileView) {
                String fullUrl = imageView.getImage().getUrl();
                String url = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);
                if (url.equals("bigDot.png") || url.equals("smallDot.png")) return false;
            }
        }
        return true;
    }

    /**
     * This Method creates the Win-Scene, which is shown when the Player wins
     * @param stage The stage in which the scene is shown
     * @return the finished scene which is shown
     */
    private Scene winScreen(Stage stage) {
        PacmanGame.timeline.stop();
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("win");

        Text text = new Text("You Win!");
        text.setFont(winFont);
        text.setFill(Color.YELLOW);

        Button menuButton = new Button("GO BACK");
        menuButton.setFont(winFont);
        menuButton.getStyleClass().add("customButton");

        Button exitButton = new Button("EXIT");
        exitButton.setFont(winFont);
        exitButton.getStyleClass().add("customButton");

        VBox buttonBox = new VBox(100);
        buttonBox.getChildren().addAll(text, menuButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(buttonBox);

        Scene winScene = new Scene(borderPane, GRID_WIDTH * TILE_SIZE, (GRID_HEIGHT * TILE_SIZE) + 40);

        winScene.getStylesheets().add("styles.css");

        menuButton.setOnAction(event -> stage.setScene(Menu.menuScene));

        exitButton.setOnAction(e -> Platform.exit());

        return winScene;
    }

    /**
     * This Method trys to place a cherry on the field [17][14]
     * Only if the field is empty, a cherry is placed
     */
    private void placeFood() {
        String fullUrl = tileViews[17][14].getImage().getUrl();
        String url = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);

        if (url.equals("blackTile.png")) {
            this.pacman.placeFood();
        }
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

        // moves pacman
        gameBoard.getChildren().remove(current_img);
        GridPane.setRowIndex(newPacman, this.pacman.getPosY());
        GridPane.setColumnIndex(newPacman, this.pacman.getPosX());
        current_img = newPacman;
        gameBoard.getChildren().add(current_img);

        // moves ghosts
        int cnt = 0;
        if(scared) {
            removeGhostView(scaredGhosts);
        } else removeGhostView(ghosts);

        for (ImageView ghostImgView : getGhostView()){
            GridPane.setRowIndex(ghostImgView, ghostObjects[cnt].posY);
            GridPane.setColumnIndex(ghostImgView, ghostObjects[cnt].posX);
            gameBoard.getChildren().add(ghostImgView);
            cnt++;
        }

    }
    //determines which ghostview is used
    private ImageView[] getGhostView() {
        if (this.pacman.scatterMode) {
            scared = true;
            return scaredGhosts;
        }
        scared = false;
        return ghosts;
    }

    //removes old view
    private void removeGhostView(ImageView[] ghostView) {
        for (ImageView ghostImgView : ghostView) {
            gameBoard.getChildren().remove(ghostImgView);
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
    public void moveGhosts(Stage stage){

        for (int i=0;i<ghostnumber;i++){
            ghostObjects[i].move(this.levelData, this.tileViews);
            tileViews = ghostObjects[i].tileView;
            levelData = ghostObjects[i].levelData;
            if(checkIfEntityCollision(ghostObjects[i])){
                if(!pacman.scatterMode) {
                    this.pacman.death(stage);
                    this.blinky.death();
                    this.clyde.death();
                    this.pinky.death();
                    this.inky.death();
                    this.ghostnumber = 1;
                    this.counter = 0;
                }
                else {
                    switch (i){
                        case 0: this.blinky.death();
                        break;
                        case 1: this.inky.death();
                            break;
                        case 2: this.clyde.death();
                            break;
                        case 3: this.pinky.death();
                            break;
                    }
                    Media death = new Media(new File("src/main/resources/sounds/ieatghost.mp3").toURI().toString());
                    MediaPlayer deathGhost = new MediaPlayer(death);
                    deathGhost.play();
                    this.pacman.edible= Pacman.Edible.GHOST;
                    this.pacman.updateScore();
                }
            }
        }
    }
    /**
     * Checks if pacman collides with a ghost
     */
    private boolean checkIfEntityCollision(MovingObjects ghosty) {
        return ghosty.getPosX() == pacman.getPosX() && ghosty.getPosY() == pacman.getPosY();
    }

    private void pacmanCollision(Stage stage) {
        for (MovingObjects gh:ghostObjects)
            if (pacman.getPosX() == gh.getPosX() && pacman.getPosY() == gh.getPosY()) {
                if(!this.pacman.scatterMode) {
                    this.pacman.death(stage);
                    this.blinky.death();
                    this.clyde.death();
                    this.pinky.death();
                    this.inky.death();
                    this.ghostnumber = 1;
                    this.counter = 0;
                } else {
                    for (MovingObjects ghost : ghostObjects) {
                        if(ghost.getPosY() == this.pacman.getPosY() && ghost.getPosX() == this.pacman.getPosX()) {
                            ghost.death();
                            Media death = new Media(new File("src/main/resources/sounds/ieatghost.mp3").toURI().toString());
                            MediaPlayer deathGhost = new MediaPlayer(death);
                            deathGhost.play();
                            this.pacman.edible = Pacman.Edible.GHOST;
                            this.pacman.updateScore();
                        }
                    }
                }
            }
    }

    //Get the GridPane for Scene creation
    public GridPane getGameBoard() {
        return this.gameBoard;
    }
}
