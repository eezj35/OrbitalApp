package com.example.orbital_app;

public class UserInfoName {
    private String username;
    private String URI;

    public UserInfoName() {
    }

    public UserInfoName(String username, String URI) {

        this.username = username;
        this.URI = URI;
    }


    public String getUserName() {
        return username;
    }

    public String getURI() {
        return URI;
    }

}