package com.example.orbital_app;

public class UserInfoName {
    private String username;
    private boolean hasLeftReview = false;

    public UserInfoName() {
    }

    public UserInfoName(String username) {
        this.username = username;
    }
    public UserInfoName(String username, boolean hasLeftReview) {

        this.username = username;
        this.hasLeftReview = hasLeftReview;
    }

    public String getUserName() {
        return username;
    }

    public boolean isHasLeftReview() {
        return hasLeftReview;
    }
}