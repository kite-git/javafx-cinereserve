package com.cinema.controller;

import com.cinema.CineReserveApp;
import com.cinema.model.Admin;
import com.cinema.model.Movie;
import com.cinema.util.DataManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class AdminController {

    @FXML private ListView<Movie> movieListView;
    @FXML private TextField titleField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField showTimeField;
    @FXML private ListView<String> showTimesList;
    @FXML private Button addMovieButton;
    @FXML private Button updateMovieButton;
    @FXML private Button deleteMovieButton;
    @FXML private Button addShowTimeButton;
    @FXML private Button removeShowTimeButton;
    @FXML private Button logoutButton;
    @FXML private Label statusLabel;
    @FXML private Label welcomeLabel;
    @FXML private Button aboutButton;
    @FXML private TextField posterField;
    @FXML private ImageView posterPreview;

    private Movie selectedMovie;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome Admin, " + DataManager.getCurrentUser().getUsername() + "!");
        loadMovies();
        setupMovieSelection();
        clearForm();
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
                    setText(movie.getTitle() + " (" + movie.getMovieId() + ")");
                }
            }
        });
    }

    private void setupMovieSelection() {
        movieListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMovie = newSelection;
                populateForm(newSelection);
                updateMovieButton.setDisable(false);
                deleteMovieButton.setDisable(false);
            } else {
                selectedMovie = null;
                updateMovieButton.setDisable(true);
                deleteMovieButton.setDisable(true);
            }
        });
    }

    private void populateForm(Movie movie) {
        titleField.setText(movie.getTitle());
        genreField.setText(movie.getGenre());
        durationField.setText(String.valueOf(movie.getDuration()));
        descriptionArea.setText(movie.getDescription());
        showTimesList.setItems(FXCollections.observableArrayList(movie.getShowTimes()));

        if (movie.getPoster() != null && movie.getPoster().startsWith("images/")) {
            String filename = movie.getPoster().substring("images/".length());
            posterField.setText(filename);
        } else {
            posterField.setText("");
        }

        try {
            Image image = new Image(getClass().getResourceAsStream("/" + movie.getPoster()));
            posterPreview.setImage(image);
        } catch (Exception e) {
            posterPreview.setImage(null);
        }
    }


    private void clearForm() {
        titleField.clear();
        genreField.clear();
        durationField.clear();
        descriptionArea.clear();
        showTimeField.clear();
        showTimesList.getItems().clear();
        posterField.clear();
        posterPreview.setImage(null);
        selectedMovie = null;
        updateMovieButton.setDisable(true);
        deleteMovieButton.setDisable(true);
        movieListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddMovie() {
        if (!validateForm()) {
            return;
        }

        try {
            String movieId = "M" + System.currentTimeMillis();
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            String description = descriptionArea.getText().trim();
            String poster = posterField.getText().trim();

            Movie newMovie = new Movie(movieId, title, genre, duration, description, "images/" + poster);

            for (String showTime : showTimesList.getItems()) {
                newMovie.addShowTime(showTime);
            }

            Admin admin = (Admin) DataManager.getCurrentUser();
            admin.addMovie(newMovie);

            loadMovies();
            clearForm();
            statusLabel.setText("Movie added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid duration!");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleUpdateMovie() {
        if (selectedMovie == null || !validateForm()) {
            return;
        }

        try {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            String description = descriptionArea.getText().trim();
            String poster = posterField.getText().trim();

            Movie updatedMovie = new Movie(selectedMovie.getMovieId(), title, genre, duration, description, "images/" + poster);
            updatedMovie.setSeats(selectedMovie.getSeats());

            for (String showTime : showTimesList.getItems()) {
                updatedMovie.addShowTime(showTime);
            }

            try {
                Image img = new Image(getClass().getResourceAsStream("/images/" + poster));
                posterPreview.setImage(img);
            } catch (Exception e) {
                posterPreview.setImage(null);
            }

            Admin admin = (Admin) DataManager.getCurrentUser();
            admin.updateMovie(selectedMovie.getMovieId(), updatedMovie);

            loadMovies();
            clearForm();
            statusLabel.setText("Movie updated successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid duration!");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleDeleteMovie() {
        if (selectedMovie == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Movie");
        alert.setHeaderText("Are you sure you want to delete this movie?");
        alert.setContentText("Movie: " + selectedMovie.getTitle());

        if (alert.showAndWait().get() == ButtonType.OK) {
            Admin admin = (Admin) DataManager.getCurrentUser();
            admin.removeMovie(selectedMovie.getMovieId());

            loadMovies();
            clearForm();
            statusLabel.setText("Movie deleted successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        }
    }

    @FXML
    private void handleAddShowTime() {
        String showTime = showTimeField.getText().trim();
        if (!showTime.isEmpty() && !showTimesList.getItems().contains(showTime)) {
            showTimesList.getItems().add(showTime);
            showTimeField.clear();
        }
    }

    @FXML
    private void handleRemoveShowTime() {
        String selectedShowTime = showTimesList.getSelectionModel().getSelectedItem();
        if (selectedShowTime != null) {
            showTimesList.getItems().remove(selectedShowTime);
        }
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter a movie title!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (genreField.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter a genre!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        try {
            Integer.parseInt(durationField.getText().trim());
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid duration!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter a description!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (showTimesList.getItems().isEmpty()) {
            statusLabel.setText("Please add at least one show time!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        return true;
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
