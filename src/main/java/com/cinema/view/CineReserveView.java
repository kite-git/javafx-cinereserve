package com.cinema.view;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CineReserveView {

    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessMessage(String message) {
        showAlert("Success", message, Alert.AlertType.INFORMATION);
    }

    public static void showErrorMessage(String message) {
        showAlert("Error", message, Alert.AlertType.ERROR);
    }

    public static void showWarningMessage(String message) {
        showAlert("Warning", message, Alert.AlertType.WARNING);
    }

    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait().get() == javafx.scene.control.ButtonType.OK;
    }
}
