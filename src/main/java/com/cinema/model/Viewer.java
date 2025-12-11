package com.cinema.model;

import com.cinema.util.DataManager;

public class Viewer extends UserAccount {

    public Viewer(String username, String password) {
        super(username, password, "VIEWER");
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) &&
                this.getPassword().equals(password);
    }

    @Override
    public void logout() {
        System.out.println("Viewer " + getUsername() + " logged out");
    }

    @Override
    public String getDashboardView() {
        return "/fxml/viewer-dashboard.fxml";
    }

    public boolean bookTicket(String movieId, String showTime, int row, int col) {
        return DataManager.bookSeat(movieId, showTime, row, col, this.getUsername());
    }
}
