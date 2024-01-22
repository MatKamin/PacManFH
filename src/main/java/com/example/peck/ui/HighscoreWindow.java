package com.example.peck.ui;

import com.example.peck.HighScore;
import com.example.peck.database.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

import static com.example.peck.config.Constants.*;
import static com.example.peck.config.Fonts.*;

/**
 * Represents the high score window in the game.
 */
public class HighscoreWindow extends Window {
    private Button backButton;

    /**
     * Constructs a HighscoreWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the high score screen.
     */
    public HighscoreWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the high score scene with a list of top scores.
     *
     * @return A Scene object representing the high score screen.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");

        Text title = new Text("Highscores");
        title.setFont(Font.font("PacFont", 50));
        title.getStyleClass().add("title");

        StackPane titleContainer = new StackPane(title);
        titleContainer.getStyleClass().add("titleContainer");
        borderPane.setTop(titleContainer);

        VBox highScoresContainer = createHighScoresContainer();
        borderPane.setCenter(highScoresContainer);

        initializeButtons();

        StackPane buttonContainer = new StackPane(backButton);
        buttonContainer.setAlignment(Pos.CENTER);
        BorderPane.setMargin(buttonContainer, new Insets(10, 0, 20, 0));
        borderPane.setBottom(buttonContainer);

        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("styles.css");

        setupEscapeKey(scene);

        return scene;
    }

    /**
     * Initializes the buttons for the game menu.
     */
    @Override
    protected void initializeButtons() {
        backButton = new Button("BACK");
        backButton.setFont(emulogicFont);
        backButton.getStyleClass().add("customButton");
        backButton.setOnAction(event -> {
            GameMenuWindow gameMenuWindow = new GameMenuWindow(stage, getScene());
            stage.setScene(gameMenuWindow.getScene());
        });
    }

    /**
     * Creates and returns a VBox containing the high score entries.
     *
     * @return VBox containing the high score entries.
     */
    private VBox createHighScoresContainer() {
        VBox highScoresContainer = new VBox(39); // Spacing between entries
        highScoresContainer.setAlignment(Pos.CENTER);
        highScoresContainer.setPadding(new Insets(0, 61, 0, 61)); // Margins
        highScoresContainer.getStyleClass().add("vbox");

        List<HighScore> scores = DatabaseHelper.getHighScores();
        for (int i = 0; i < scores.size() && i < 5; i++) {
            HighScore score = scores.get(i);
            HBox lineContainer = createScoreLine(score, i);
            highScoresContainer.getChildren().add(lineContainer);
        }

        return highScoresContainer;
    }

    /**
     * Creates a line entry for a high score.
     *
     * @param score The high score to display.
     * @param index The index of the score in the list.
     * @return HBox representing the score line.
     */
    private HBox createScoreLine(HighScore score, int index) {
        Text indexAndName = new Text("#" + (index + 1) + " " + score.getUsername());
        indexAndName.setFont(Font.font("Emulogic", 24));
        indexAndName.setFill(index == 0 ? Color.web("#FD0000") : Color.web("#FFFF00"));

        Text scoreText = new Text(String.valueOf(score.getScore()));
        scoreText.setFont(Font.font("Emulogic", 24));
        scoreText.setFill(index == 0 ? Color.web("#FD0000") : Color.web("#FFFF00"));

        HBox leftContainer = new HBox(indexAndName);
        leftContainer.setAlignment(Pos.CENTER_LEFT);

        HBox rightContainer = new HBox(scoreText);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightContainer, Priority.ALWAYS); // Push the score to the right

        HBox lineContainer = new HBox(leftContainer, rightContainer);
        lineContainer.setPrefWidth(750 - 2 * 61); // Window width - margins
        return lineContainer;
    }

}
