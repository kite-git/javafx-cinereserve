package com.cinema.controller;

import com.cinema.CineReserveApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.cinema.model.UserAccount;
import com.cinema.util.DataManager;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> userTypeCombo;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        userTypeCombo.getItems().addAll("VIEWER", "ADMIN");
        userTypeCombo.setValue("VIEWER");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String userType = userTypeCombo.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please fill in all fields!");
            return;
        }

        UserAccount user = DataManager.authenticateUser(username, password, userType);

        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(user.getDashboardView()));
                Scene scene = new Scene(loader.load(), 1000, 800);

                scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());


                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();

                statusLabel.setText("Login successful!");
            } catch (Exception e) {
                statusLabel.setText("Error loading dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("Invalid credentials or user type!");
        }
    }
}
