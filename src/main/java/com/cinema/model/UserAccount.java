package com.cinema.model;

public abstract class UserAccount {
    private String username;
    private String password;
    private String userType;

    public UserAccount(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public abstract boolean login(String username, String password);
    public abstract void logout();
    public abstract String getDashboardView();
}
