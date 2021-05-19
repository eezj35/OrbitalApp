package com.example.orbital_app;

public class Locations {
    private String name;
    private final String imageUrl;

    public Locations(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

}
