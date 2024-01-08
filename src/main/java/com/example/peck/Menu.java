package com.example.peck;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

import java.util.Objects;


public class Menu extends Application {

    private static String levelFile = "/level.txt";
    private static String skinFolder = "standard";
    private static String loggedInUsername = "";
    private static boolean SPLASH_ENABLED = true;
    public static Scene menuScene;

    public Font pacmanFont = Font.loadFont(getClass().getResourceAsStream("/fonts/pacman.TTF"), 48);
    public Font emulogicFont = Font.loadFont(getClass().getResourceAsStream("/fonts/emulogic.ttf"), 48);


    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseHelper.initDB();

        if (!SPLASH_ENABLED) {
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
                        primaryStage.setScene(createMenu(primaryStage, null));
                        primaryStage.setTitle("Pac-Man 2.0");
                        primaryStage.setResizable(false);
                        primaryStage.initStyle(StageStyle.DECORATED); // Make sure the main menu has decorations
                        primaryStage.show();
                    })
            );

            // Play the timeline
            timeline.play();
        } else {
            primaryStage.setScene(createMenu(primaryStage, null));
            primaryStage.setTitle("Pac-Man 2.0");
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.DECORATED); // Make sure the main menu has decorations
            primaryStage.show();
        }

    }

    /**
     * Defines the Layout of Menu
     * @param stage needed for interactive Scene switch
     * @return returns a new Scene object, which can be shown in a Stage
     */
    private Scene createMenu(Stage stage, Scene previousScene) {
        // Main Container of the Menu
        BorderPane menuBorderPane = new BorderPane();
        menuBorderPane.getStyleClass().add("borderPane");

        // Title with custom font
        Text title = new Text("Pac-Man");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");
        menuBorderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane titleContainer = new StackPane();
        titleContainer.getChildren().add(title);
        titleContainer.getStyleClass().add("titleContainer");
        menuBorderPane.setTop(titleContainer);
        BorderPane.setAlignment(titleContainer, Pos.TOP_CENTER);


        // Buttons
        Button loginButton = new Button("LOGIN");
        loginButton.setFont(emulogicFont);
        loginButton.getStyleClass().add("customButton");

        Button registerButton = new Button("REGISTER");
        registerButton.setFont(emulogicFont);
        registerButton.getStyleClass().add("customButton");

        Button highScoresButton = new Button("HIGHSCORES");
        highScoresButton.setFont(emulogicFont);
        highScoresButton.getStyleClass().add("customButton");

        // VBox to hold buttons
        VBox buttonBox = new VBox(45);
        buttonBox.getChildren().addAll(loginButton, registerButton, highScoresButton);
        buttonBox.setAlignment(Pos.CENTER);
        menuBorderPane.setCenter(buttonBox);

        // Scene
        Scene menuScene = new Scene(menuBorderPane, 750, 821);
        menuScene.getStylesheets().add("styles.css");


        registerButton.setOnAction(event -> {
            Scene registerScene = openRegistrationWindow(stage, menuScene);
            stage.setScene(registerScene);
        });

        loginButton.setOnAction(event -> {
            Scene loginScene = openLoginWindow(stage, menuScene);
            stage.setScene(loginScene);
        });

        return menuScene;
    }


    private Scene openRegistrationWindow(Stage stage, Scene previousScene) {
        // Main Container of the Menu
        BorderPane registerBorderPane = new BorderPane();
        registerBorderPane.getStyleClass().add("borderPane");

        // Title with custom font
        Text title = new Text("ReGisTER");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");
        registerBorderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane titleContainer = new StackPane();
        titleContainer.getChildren().add(title);
        titleContainer.getStyleClass().add("titleContainer");
        registerBorderPane.setTop(titleContainer);
        BorderPane.setAlignment(titleContainer, Pos.TOP_CENTER);

        // Input fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("customTextInput");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("customTextInput");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("customTextInput");

        // Confirm button
        Button confirmButton = new Button("CONFIRM");
        confirmButton.setFont(emulogicFont);
        confirmButton.getStyleClass().add("customButton");

        // VBox for layout
        VBox inputLayout = new VBox(45);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.getChildren().addAll(usernameField, passwordField, confirmPasswordField, confirmButton);
        inputLayout.setMaxWidth(502);

        registerBorderPane.setCenter(inputLayout);

        // Scene
        Scene registerScene = new Scene(registerBorderPane, 750, 821);
        registerScene.getStylesheets().add("styles.css");



        registerScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.setScene(previousScene);
            } else if (event.getCode() == KeyCode.ENTER) {
                confirmButton.fire();
            }
        });


        confirmButton.setOnAction(event -> {
            // Get the user inputs from text fields
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Check if the password fields match
            if (!password.equals(confirmPassword)) {
                // Show error message to the user
                // You can use a Label or an Alert dialog for this purpose
                showAlert("Password Mismatch", "The passwords do not match. Please try again.");
                return;
            }

            // Check if the fields are not empty
            if (username.isEmpty() || password.isEmpty()) {
                // Show error message to the user
                showAlert("Empty Fields", "Please enter both username and password.");
                return;
            }

            // Register the user
            boolean isRegistered = DatabaseHelper.registerUser(username, password);

            if (isRegistered) {
                // Registration successful
                loggedInUsername = usernameField.getText();
                Scene mainScene = openMainWindow(stage);
                menuScene = mainScene;
                stage.setScene(mainScene);
            } else {
                // Registration failed, show error message to the user
                showAlert("Registration Failed", "Username Taken! Please try again.");
            }
        });

        return registerScene;
    }


    private Scene openMainWindow(Stage stage) {
        // Main Container of the Menu
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.getStyleClass().add("borderPane");

        // Title with custom font
        Text title = new Text("Pac-Man");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");
        mainBorderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane titleContainer = new StackPane();
        titleContainer.getChildren().add(title);
        titleContainer.getStyleClass().add("titleContainer");
        mainBorderPane.setTop(titleContainer);
        BorderPane.setAlignment(titleContainer, Pos.TOP_CENTER);


        // Buttons
        Button playButton = new Button("PLAY");
        playButton.setFont(emulogicFont);
        playButton.getStyleClass().add("customButton");

        Button settingsButton = new Button("SETTINGS");
        settingsButton.setFont(emulogicFont);
        settingsButton.getStyleClass().add("customButton");

        Button statsButton = new Button("STATISTICS");
        statsButton.setFont(emulogicFont);
        statsButton.getStyleClass().add("customButton");

        // VBox to hold buttons
        VBox buttonBox = new VBox(45);
        buttonBox.getChildren().addAll(playButton, settingsButton, statsButton);
        buttonBox.setAlignment(Pos.CENTER);
        mainBorderPane.setCenter(buttonBox);

        // Scene
        Scene menuScene = new Scene(mainBorderPane, 750, 821);
        menuScene.getStylesheets().add("styles.css");



        playButton.setOnAction(event -> {
            PacmanGame pacmanGame = new PacmanGame(levelFile, skinFolder, stage);
            stage.setScene(pacmanGame.getGameView());
            pacmanGame.startGame();
        });

        return menuScene;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Scene openLoginWindow(Stage stage, Scene previousScene) {
        // Main Container of the Menu
        BorderPane loginBorderPane = new BorderPane();
        loginBorderPane.getStyleClass().add("borderPane");

        // Title with custom font
        Text title = new Text("LOGin");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");
        loginBorderPane.setTop(title);
        BorderPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane titleContainer = new StackPane();
        titleContainer.getChildren().add(title);
        titleContainer.getStyleClass().add("titleContainer");
        loginBorderPane.setTop(titleContainer);
        BorderPane.setAlignment(titleContainer, Pos.TOP_CENTER);

        // Input fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("customTextInput");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("customTextInput");

        // Confirm button
        Button confirmButton = new Button("CONFIRM");
        confirmButton.setFont(emulogicFont);
        confirmButton.getStyleClass().add("customButton");

        // VBox for layout
        VBox inputLayout = new VBox(45);
        inputLayout.setAlignment(Pos.CENTER);
        inputLayout.getChildren().addAll(usernameField, passwordField, confirmButton);
        inputLayout.setMaxWidth(502);

        loginBorderPane.setCenter(inputLayout);

        // Scene
        Scene loginScene = new Scene(loginBorderPane, 750, 821);
        loginScene.getStylesheets().add("styles.css");


        // Event handler for the login button
        confirmButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean loginSuccess = DatabaseHelper.checkLogin(username, password);
            if (loginSuccess) {
                loggedInUsername = usernameField.getText();
                Scene mainScene = openMainWindow(stage);
                menuScene = mainScene;
                stage.setScene(mainScene);
            } else {
                // Login failed, show error message
                showAlert("Login Error", "Invalid username or password. Please try again.");
            }
        });

        loginScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.setScene(previousScene);
            } else if (event.getCode() == KeyCode.ENTER) {
                confirmButton.fire();
            }
        });

        return loginScene;
    }

    public static void main(String[] args) {
        launch();
    }
}
