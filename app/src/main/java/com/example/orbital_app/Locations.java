package com.example.orbital_app;

public class Locations {
    private String name;
    private final String image;

    public Locations(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

}
