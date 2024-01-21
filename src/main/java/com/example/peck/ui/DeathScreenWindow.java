package com.example.peck.ui;

import com.example.peck.Gameboard;
import com.example.peck.config.Fonts;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.peck.config.Constants.*;

/**
 * Represents the death screen window in the game.
 */
public class DeathScreenWindow extends Window {

    private Button menuButton, exitButton;

    /**
     * Constructs a DeathScreenWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the death screen.
     */
    public DeathScreenWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the death screen scene.
     *
     * @return A Scene object representing the death screen.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("win");

        Text text = new Text("You Lose!");
        text.setFont(Fonts.emulogicFont);
        text.setFill(Color.YELLOW);

        initializeButtons();

        VBox buttonBox = new VBox(100);
        buttonBox.getChildren().addAll(text, menuButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(buttonBox);

        Scene deathScene = new Scene(borderPane, GRID_WIDTH * TILE_SIZE,
                (GRID_HEIGHT * TILE_SIZE) + 40);

        deathScene.getStylesheets().add("styles.css");


        return deathScene;
    }

    /**
     * Initializes the buttons for the death screen.
     */
    @Override
    protected void initializeButtons() {
        menuButton = new Button("RETRY");
        menuButton.setFont(Fonts.emulogicFont);
        menuButton.getStyleClass().add("customButton");
        menuButton.setOnAction(event -> {
            GameMenuWindow gameMenuWindow = new GameMenuWindow(stage, getScene());
            stage.setScene(gameMenuWindow.getScene());
        });

        exitButton = new Button(" EXIT ");
        exitButton.setFont(Fonts.emulogicFont);
        exitButton.getStyleClass().add("customButton");
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
    }
}
