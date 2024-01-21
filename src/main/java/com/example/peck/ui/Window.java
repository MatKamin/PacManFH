package com.example.peck.ui;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Abstract base class for creating various windows in the application.
 * This class provides common functionalities needed across different windows.
 */
public abstract class Window {
    protected Stage stage;
    protected Scene previousScene;

    /**
     * Constructs a Window instance.
     *
     * @param stage The primary stage of the application.
     * @param previousScene The scene to be displayed before switching to this window's scene.
     */
    public Window(Stage stage, Scene previousScene) {
        this.stage = stage;
        this.previousScene = previousScene;
    }

    /**
     * Abstract method to create a scene for the window.
     * This method is intended to be overridden by subclasses to define their specific scenes.
     *
     * @return The created Scene object for this window.
     */
    protected abstract Scene createScene();

    /**
     * Initializes buttons for the window.
     * This method can be overridden by subclasses to add specific functionalities to buttons.
     */
    protected void initializeButtons() {}

    /**
     * Sets up a key event handler to handle the ESCAPE key press.
     * When the ESCAPE key is pressed, the scene switches back to the previous scene.
     *
     * @param scene The scene to which the key event handler is to be added.
     */
    protected void setupEscapeKey(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.setScene(previousScene);
            }
        });
    }

    /**
     * Returns the scene for this window by creating it using the createScene method.
     *
     * @return The Scene object created for this window.
     */
    public Scene getScene() {
        return createScene();
    }
}
