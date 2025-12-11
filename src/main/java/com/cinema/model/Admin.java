package com.cinema.model;

import com.cinema.util.DataManager;

public class Admin extends UserAccount implements ManageMovies {

    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) &&
                this.getPassword().equals(password);
    }

    @Override
    public void logout() {
        System.out.println("Admin " + getUsername() + " logged out");
    }

    @Override
    public String getDashboardView() {
        return "/fxml/admin-dashboard.fxml";
    }

    @Override
    public void addMovie(Movie movie) {
        DataManager.addMovie(movie);
    }

    @Override
    public void removeMovie(String movieId) {
        DataManager.removeMovie(movieId);
    }

    @Override
    public void updateMovie(String movieId, Movie updatedMovie) {
        DataManager.updateMovie(movieId, updatedMovie);
    }

    @Override
    public Movie getMovie(String movieId) {
        return DataManager.getMovie(movieId);
    }
}
