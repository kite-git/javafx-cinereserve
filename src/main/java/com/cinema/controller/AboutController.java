package com.cinema.controller;

import com.cinema.CineReserveApp;
import com.cinema.util.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {
    @FXML
    private Button backButton;

    @FXML
    private void handleBack() {
        System.out.println("Back");
        try {
            if (DataManager.getCurrentUser() != null) {
                String userRole = DataManager.getCurrentUser().getUserType();

                if ("ADMIN".equals(userRole)) {
                    loadAdminDashboard();
                } else {
                    loadViewerDashboard();
                }
            } else {
                loadLoginPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }
    private void loadViewerDashboard() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewer-dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void loadAdminDashboard() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin-dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void loadLoginPage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-new.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}

