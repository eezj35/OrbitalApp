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

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
