package com.cinema.util;

import com.cinema.model.*;
import java.util.*;

public class DataManager {
    private static Map<String, UserAccount> users = new HashMap<>();
    private static Map<String, Movie> movies = new HashMap<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static UserAccount currentUser;

    public static void initializeSampleData() {
        // Initialize sample users
        users.put("viewer", new Viewer("viewer", "viewer123"));
        users.put("kite", new Viewer("kite", "kite123"));
        users.put("admin", new Admin("admin", "admin123"));

        Movie movie1 = new Movie("M001", "Jurassic World: Rebirth", "Action", 124, "Jurassic World description", "images/jurassic-world.jpg");
        movie1.addShowTime("10:00 AM");
        movie1.addShowTime("2:00 PM");
        movie1.addShowTime("6:00 PM");
        movies.put("M001", movie1);

        Movie movie2 = new Movie("M002", "The Fantastic Four: First Steps", "Action", 106, "The Fantastic Four description", "images/fantastic-four.jpg");
        movie2.addShowTime("11:00 AM");
        movie2.addShowTime("3:00 PM");
        movie2.addShowTime("7:00 PM");
        movies.put("M002", movie2);

        Movie movie3 = new Movie("M003", "Superman", "Action", 130, "Superman description", "images/superman.jpg");
        movie3.addShowTime("12:00 PM");
        movie3.addShowTime("4:00 PM");
        movie3.addShowTime("8:00 PM");
        movies.put("M003", movie3);
    }

    public static UserAccount authenticateUser(String username, String password, String userType) {
        UserAccount user = users.get(username);
        if (user != null && user.login(username, password) && user.getUserType().equals(userType)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    public static void addMovie(Movie movie) {
        movies.put(movie.getMovieId(), movie);
    }

    public static void removeMovie(String movieId) {
        movies.remove(movieId);
    }

    public static void updateMovie(String movieId, Movie updatedMovie) {
        movies.put(movieId, updatedMovie);
    }

    public static Movie getMovie(String movieId) {
        return movies.get(movieId);
    }

    public static List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }

    public static boolean bookSeat(String movieId, String showTime, int row, int col, String username) {
        Movie movie = movies.get(movieId);
        if (movie != null && movie.isSeatAvailable(row, col)) {
            movie.bookSeat(row, col);

            String bookingId = "B" + System.currentTimeMillis();
            Booking booking = new Booking(bookingId, username, movieId, showTime, row, col, 12.50);
            bookings.add(booking);

            return true;
        }
        return false;
    }

    public static List<Booking> getUserBookings(String username) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUsername().equals(username)) {
                userBookings.add(booking);
            }
        }
        return userBookings;
    }

    public static UserAccount getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserAccount user) {
        currentUser = user;
    }
}
