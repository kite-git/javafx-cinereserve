package com.cinema.controller;

import com.cinema.CineReserveApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.cinema.model.*;
import com.cinema.util.DataManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;

import java.util.List;

public class ViewerController {

    @FXML private ListView<Movie> movieListView;
    @FXML private TextArea movieDetailsArea;
    @FXML private ComboBox<String> showTimeCombo;
    @FXML private GridPane seatGrid;
    @FXML private Button bookButton;
    @FXML private Button logoutButton;
    @FXML private Label statusLabel;
    @FXML private Label welcomeLabel;
    @FXML private Button aboutButton;
    @FXML private ImageView posterView;

    private Movie selectedMovie;
    private Button selectedSeatButton;
    private int selectedRow = -1;
    private int selectedCol = -1;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome, " + DataManager.getCurrentUser().getUsername() + "!");
        loadMovies();
        setupMovieSelection();
    }

    private void loadMovies() {
        List<Movie> movies = DataManager.getAllMovies();
        movieListView.setItems(FXCollections.observableArrayList(movies));

        movieListView.setCellFactory(listView -> new ListCell<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                } else {
                    setText(movie.getTitle() + " (" + movie.getGenre() + ")");
                }
            }
        });
    }

    private void setupMovieSelection() {
        movieListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMovie = newSelection;
                displayMovieDetails(newSelection);
                loadShowTimes(newSelection);
                clearSeatSelection();
            }
        });

        showTimeCombo.setOnAction(e -> {
            if (selectedMovie != null && showTimeCombo.getValue() != null) {
                displaySeats();
            }
        });
    }

    private void displayMovieDetails(Movie movie) {
        StringBuilder details = new StringBuilder();
        details.append("Title: ").append(movie.getTitle()).append("\n");
        details.append("Genre: ").append(movie.getGenre()).append("\n");
        details.append("Duration: ").append(movie.getDuration()).append(" minutes\n");
        details.append("Description: ").append(movie.getDescription()).append("\n");

        movieDetailsArea.setText(details.toString());

        try {
            Image image = new Image(getClass().getResourceAsStream("/" + movie.getPoster()));
            posterView.setImage(image);
        } catch (Exception e) {
            posterView.setImage(null);
        }
    }

    private void loadShowTimes(Movie movie) {
        showTimeCombo.setItems(FXCollections.observableArrayList(movie.getShowTimes()));
        showTimeCombo.getSelectionModel().clearSelection();
    }

    private void displaySeats() {
        seatGrid.getChildren().clear();

        if (selectedMovie == null) return;

        boolean[][] seats = selectedMovie.getSeats();

        for (int row = 0; row < seats.length; row++) {
            for (int col = 0; col < seats[row].length; col++) {
                Button seatButton = new Button();
                seatButton.setPrefSize(40, 40);
                seatButton.setText((char)('A' + row) + String.valueOf(col + 1));

                final int finalRow = row;
                final int finalCol = col;

                if (seats[row][col]) {
                    // Seat is booked
                    seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    seatButton.setDisable(true);
                } else {
                    // Seat is available
                    seatButton.setStyle("-fx-background-color: lightgreen;");
                    seatButton.setOnAction(e -> selectSeat(seatButton, finalRow, finalCol));
                }

                seatGrid.add(seatButton, col, row);
            }
        }

        seatGrid.setPadding(new Insets(10));
        seatGrid.setHgap(5);
        seatGrid.setVgap(5);
    }

    private void selectSeat(Button seatButton, int row, int col) {
        if (selectedSeatButton != null) {
            selectedSeatButton.setStyle("-fx-background-color: lightgreen;");
        }

        selectedSeatButton = seatButton;
        seatButton.setStyle("-fx-background-color: yellow;");
        selectedRow = row;
        selectedCol = col;

        bookButton.setDisable(false);
        statusLabel.setText("Selected seat: " + seatButton.getText());
    }

    private void clearSeatSelection() {
        selectedSeatButton = null;
        selectedRow = -1;
        selectedCol = -1;
        bookButton.setDisable(true);
        statusLabel.setText("");
    }

    @FXML
    private void handleBookTicket() {
        if (selectedMovie == null || showTimeCombo.getValue() == null || selectedRow == -1 || selectedCol == -1) {
            statusLabel.setText("Please select a movie, show time, and seat!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String seatNumber = (char)('A' + selectedRow) + String.valueOf(selectedCol + 1);
        showPaymentDialog(selectedMovie.getTitle(), showTimeCombo.getValue(), seatNumber);
    }

    private void showPaymentDialog(String movieTitle, String showTime, String seatNumber) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/payment.fxml"));
            Scene scene = new Scene(loader.load(), 600, 700);

            scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());


            Stage paymentStage = new Stage();
            paymentStage.setTitle("Payment Processing");
            paymentStage.setScene(scene);
            paymentStage.initModality(Modality.APPLICATION_MODAL);
            paymentStage.initOwner(CineReserveApp.getPrimaryStage());
            paymentStage.setResizable(false);

            PaymentController paymentController = loader.getController();
            paymentController.setBookingDetails(movieTitle, showTime, seatNumber,
                    DataManager.getCurrentUser().getUsername(),
                    new PaymentCallback() {
                        @Override
                        public void onPaymentSuccess() {
                            Viewer viewer = (Viewer) DataManager.getCurrentUser();
                            boolean success = viewer.bookTicket(selectedMovie.getMovieId(), showTime, selectedRow, selectedCol);

                            if (success) {
                                statusLabel.setText("Ticket booked and payment processed successfully!");
                                statusLabel.setStyle("-fx-text-fill: green;");
                                displaySeats(); // Refresh seat display
                                clearSeatSelection();
                            }
                        }

                        @Override
                        public void onPaymentCancelled() {
                            statusLabel.setText("Payment cancelled. Ticket not booked.");
                            statusLabel.setStyle("-fx-text-fill: orange;");
                        }
                    });

            paymentStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error opening payment dialog!");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            DataManager.getCurrentUser().logout();
            DataManager.setCurrentUser(null);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-new.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about-us.fxml"));
            Scene scene = new Scene(loader.load(), 610, 409);

            scene.getStylesheets().add(CineReserveApp.class.getResource("/fxml/styles.css").toExternalForm());

            Stage stage = (Stage) aboutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
