package com.cinema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.cinema.util.DataManager;

public class CineReserveApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        CineReserveApp.primaryStage = primaryStage;
        DataManager.initializeSampleData();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-new.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);

        scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());

        primaryStage.setTitle("CineReserve");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
