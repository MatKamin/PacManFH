package com.example.peck.util;

import javafx.scene.control.Alert;

/**
 * This class provides utility methods for displaying alert dialogs in a JavaFX application.
 */
public class AlertUtil {

    /**
     * Display an alert dialog with the given title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to be displayed in the alert dialog.
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
