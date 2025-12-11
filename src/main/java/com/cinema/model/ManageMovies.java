package com.cinema.model;

public interface ManageMovies {
    void addMovie(Movie movie);
    void removeMovie(String movieId);
    void updateMovie(String movieId, Movie updatedMovie);
    Movie getMovie(String movieId);
}