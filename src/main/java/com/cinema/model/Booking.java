package com.cinema.model;

import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String username;
    private String movieId;
    private String showTime;
    private int seatRow;
    private int seatCol;
    private LocalDateTime bookingTime;
    private double price;

    public Booking(String bookingId, String username, String movieId, String showTime,
                   int seatRow, int seatCol, double price) {
        this.bookingId = bookingId;
        this.username = username;
        this.movieId = movieId;
        this.showTime = showTime;
        this.seatRow = seatRow;
        this.seatCol = seatCol;
        this.price = price;
        this.bookingTime = LocalDateTime.now();
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }

    public int getSeatRow() { return seatRow; }
    public void setSeatRow(int seatRow) { this.seatRow = seatRow; }

    public int getSeatCol() { return seatCol; }
    public void setSeatCol(int seatCol) { this.seatCol = seatCol; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSeatNumber() {
        return (char)('A' + seatRow) + String.valueOf(seatCol + 1);
    }
}
