package com.example.peck;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.application.Platform.*;

public class Menu extends Application {

    private static String levelFile = "/level.txt";
    private static String skinFolder = "standard";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(createMenu(primaryStage));
        primaryStage.setTitle("Pacman");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     *
     * @param stage needed for interactive Scene switch
     * @return returns a new Scene object, which can be shown in the Stage
     */
    private Scene createNewGame(Stage stage) {

        //Nodes
        Text titel = new Text("New Game");
        ChoiceBox<String> level = new ChoiceBox<>();
        level.getItems().addAll("/level.txt");
        level.setValue("Choose the level!");
        ChoiceBox<String> skin = new ChoiceBox<>();
        skin.getItems().addAll("green", "red", "standard");
        skin.setValue("Choose your pacman skin!");
        Button play = new Button("Play");
        Button menu = new Button("Back");


        //Interaction
        level.setOnAction(event -> levelFile = level.getValue());
        skin.setOnAction(event -> skinFolder = skin.getValue());
        play.setOnAction(event -> {
            PacmanGame pacmanGame = new PacmanGame(levelFile, skinFolder);
            stage.setScene(pacmanGame.getGameView());
            pacmanGame.startGame();
        });
        menu.setOnAction(event -> stage.setScene(createMenu(stage)));

        //Container & Design
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        root.getChildren().addAll(titel, level, skin, play, menu);
        return new Scene(root, 1280, 720);
    }

    /**
     *
     * @param stage needed for interactive Scene switch
     * @return returns a new Scene object, which can be shown in a Stage
     */
    private Scene createMenu(Stage stage) {
        //Main Container of the Menu
        BorderPane root = new BorderPane();

        //Secondary Container
        VBox vBox = new VBox();

        //Nodes
        Button button1 = new Button("New Game");
        Button button2 = new Button("Settings");
        Button button3 = new Button("Exit");

        //Interaction
        button1.setOnAction(event -> stage.setScene(createNewGame(stage)));
        button3.setOnAction(event -> exit());

        //Design
        button1.setPrefSize(200,50);
        button2.setPrefSize(200,50);
        button3.setPrefSize(200,50);
        vBox.getChildren().addAll(button1, button2, button3);
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        root.setCenter(vBox);
        root.setStyle("-fx-background-image: url('Menu/menuBackground.jpg');" + "-fx-background-size: cover;");
        return new Scene(root, 1280, 720);
    }

    public static void main(String[] args) {
        launch();
    }
}
