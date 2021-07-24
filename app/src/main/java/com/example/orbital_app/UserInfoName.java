package com.example.orbital_app;

public class UserInfoName {
    private String username;
    private String image;

    public UserInfoName() {
    }


    public UserInfoName(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public String getUserName() {
        return username;
    }

    public String getImage() {
        return image;
    }


}