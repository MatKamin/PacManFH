package com.example.peck.ui;

import com.example.peck.config.CurrentUser;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.example.peck.config.Fonts.*;
import static com.example.peck.config.Variables.*;

/**
 * Represents the settings window of the application.
 */
public class SettingsWindow extends Window{
    /**
     * Constructs a SettingsWindow.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before the settings screen.
     */
    public SettingsWindow(Stage stage, Scene previousScene) {
        super(stage, previousScene);
    }

    /**
     * Creates the settings scene with various configuration options.
     *
     * @return A Scene object representing the settings screen.
     */
    @Override
    protected Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");

        Text title = new Text("Settings");
        title.setFont(pacmanFont);
        title.getStyleClass().add("title");

        StackPane titleContainer = new StackPane(title);
        titleContainer.getStyleClass().add("titleContainer");
        borderPane.setTop(titleContainer);

        VBox settingsContainer = createSettingsContainer();
        borderPane.setCenter(settingsContainer);

        Scene scene = new Scene(borderPane, 750, 821);
        scene.getStylesheets().add("styles.css");

        setupEscapeKey(scene);

        return scene;
    }

    /**
     * Creates and returns a VBox containing the settings options.
     *
     * @return VBox containing the settings options.
     */
    private VBox createSettingsContainer() {
        CheckBox muteCheckBox = createMuteCheckBox();
        VBox volumeContainer = createVolumeContainer();
        VBox skinContainer = createSkinContainer();

        VBox settingsContainer = new VBox(100);
        settingsContainer.setAlignment(Pos.CENTER);
        settingsContainer.getChildren().addAll(muteCheckBox, volumeContainer, skinContainer);

        return settingsContainer;
    }

    /**
     * Creates and returns a CheckBox for muting sounds.
     *
     * @return CheckBox for muting sounds.
     */
    private CheckBox createMuteCheckBox() {
        CheckBox muteCheckBox = new CheckBox("Mute Sounds");
        muteCheckBox.setSelected(soundsMuted);
        muteCheckBox.setFont(Font.font("Emulogic", 24));
        muteCheckBox.setTextFill(Color.web("#FFFF00"));
        muteCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            soundsMuted = newValue;
        });
        return muteCheckBox;
    }

    /**
     * Creates and returns a VBox containing volume control components.
     *
     * @return VBox containing volume slider and label.
     */
    private VBox createVolumeContainer() {
        Label volumeLabel = new Label("Volume");
        volumeLabel.setFont(Font.font("Emulogic", 24));
        volumeLabel.setTextFill(Color.web("#FFFF00"));

        Slider volumeSlider = new Slider(0, 100, soundsVolume * 100);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(50);
        volumeSlider.setMinorTickCount(5);
        volumeSlider.setBlockIncrement(10);
        volumeSlider.setMaxWidth(300);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100.0; // Convert to range 0.0 - 1.0
            soundsVolume = volume;
        });

        VBox volumeContainer = new VBox(10); // Spacing between label and slider
        volumeContainer.setAlignment(Pos.CENTER);
        volumeContainer.getChildren().addAll(volumeLabel, volumeSlider);

        return volumeContainer;
    }

    /**
     * Creates and returns a VBox containing the skin selector components.
     *
     * @return VBox containing skin selector ComboBox and label.
     */
    private VBox createSkinContainer() {
        Label skinLabel = new Label("Skinpack");
        skinLabel.setFont(Font.font("Emulogic", 24));
        skinLabel.setTextFill(Color.web("#FFFF00"));

        ComboBox<String> skinSelector = new ComboBox<>();
        skinSelector.getItems().addAll(getSkinFolderNames());
        skinSelector.getSelectionModel().select(CurrentUser.skinFolder);

        skinSelector.setPrefWidth(200); // Set preferred width to make it larger
        skinSelector.setStyle("-fx-font-family: 'Emulogic'; -fx-font-size: 16px; -fx-background-color: #2121DE; -fx-text-fill: #FFFF00;");
        skinSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            CurrentUser.skinFolder = newValue;
        });

        VBox skinContainer = new VBox(10); // Spacing between label and ComboBox
        skinContainer.setAlignment(Pos.CENTER);
        skinContainer.getChildren().addAll(skinLabel, skinSelector);

        return skinContainer;
    }

    /**
     * Retrieves and returns a list of skin folder names.
     *
     * @return List of skin folder names.
     */
    private List<String> getSkinFolderNames() {
        List<String> skinNames = new ArrayList<>();
        String path = "/pacman";
        URL dirURL = getClass().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("jar")) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar;
            try {
                jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path.substring(1))) { //filter according to the path
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            // if it is a subdirectory, we just return the directory name
                            entry = entry.substring(0, checkSubdir);
                        }
                        result.add(entry);
                    }
                }
                skinNames.addAll(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        skinNames.remove(0);
        System.out.println(skinNames);
        return skinNames;
    }
}
