package com.cinema.model;

import java.util.List;
import java.util.ArrayList;

public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private int duration;
    private String description;
    private String poster;
    private List<String> showTimes;
    private boolean[][] seats;

    public Movie(String movieId, String title, String genre, int duration, String description, String poster) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.description = description;
        this.poster = poster;
        this.showTimes = new ArrayList<>();
        this.seats = new boolean[8][10];
        initializeSeats();
    }

    private void initializeSeats() {
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = false;
            }
        }
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }

    public List<String> getShowTimes() { return showTimes; }
    public void setShowTimes(List<String> showTimes) { this.showTimes = showTimes; }

    public boolean[][] getSeats() { return seats; }
    public void setSeats(boolean[][] seats) { this.seats = seats; }

    public boolean isSeatAvailable(int row, int col) {
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length) {
            return !seats[row][col];
        }
        return false;
    }

    public void bookSeat(int row, int col) {
        if (row >= 0 && row < seats.length && col >= 0 && col < seats[0].length) {
            seats[row][col] = true;
        }
    }

    public void addShowTime(String showTime) {
        showTimes.add(showTime);
    }
}
