package com.example.peck;

import com.example.peck.database.DatabaseHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.peck.ui.MainMenuWindow;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;

import static com.example.peck.config.Constants.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper.initDB();
        MainMenuWindow mainMenuWindow = new MainMenuWindow(primaryStage, null);

        if (!SPLASH_DISABLED) {
            // Create the splash layout
            StackPane splashLayout = new StackPane();
            splashLayout.setAlignment(Pos.CENTER);

            // Load the first GIF
            Image firstGifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/splashscreen/pacman_chasing.gif")));
            ImageView gifView = new ImageView(firstGifImage);
            splashLayout.getChildren().add(gifView);

            // Create the splash stage
            Stage splashStage = new Stage();
            Scene splashScene = new Scene(splashLayout, 480, 106);
            splashStage.setScene(splashScene);
            splashStage.initStyle(StageStyle.UNDECORATED); // Set the splash stage to be undecorated
            splashStage.show();

            // Timeline for controlling the GIF display durations
            Timeline timeline = new Timeline(
                    // Pause for the duration of the first GIF
                    new KeyFrame(Duration.millis(4000), e -> {
                        // Load the second GIF
                        Image secondGifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/splashscreen/pacman_being_chased.gif")));
                        gifView.setImage(secondGifImage);
                    }),

                    // Pause for the duration of the second GIF
                    new KeyFrame(Duration.millis(4000 + 4040), e -> {
                        // Close the splash stage
                        splashStage.close();

                        // Now set up the main menu stage
                        primaryStage.setScene(mainMenuWindow.getScene());
                        primaryStage.setTitle("Pac-Man 2.0");
                        primaryStage.setResizable(false);
                        primaryStage.initStyle(StageStyle.DECORATED); // Make sure the main menu has decorations
                        primaryStage.show();
                    })
            );

            // Play the timeline
            timeline.play();
        } else {
            primaryStage.setScene(mainMenuWindow.getScene());
            primaryStage.setTitle("Pac-Man 2.0");
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.DECORATED); // Make sure the main menu has decorations
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

