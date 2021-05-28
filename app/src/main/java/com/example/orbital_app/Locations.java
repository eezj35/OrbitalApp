package com.example.orbital_app;


public class Locations {
    private String name;
    private String image;
    private int rating;

    public Locations(){}

    public Locations(String name, String image, int rating) {
        this.name = name;
        this.image = image;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getRating() {
        return rating;
    }

}
