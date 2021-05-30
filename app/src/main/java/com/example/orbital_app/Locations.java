package com.example.orbital_app;


public class Locations {
    private String name;
    private String image;
    private int rating;
    private String cost;
    private String state;
    private int id;
    private String generalLoc;
    private String openingHours;


    public Locations(){}

    public Locations(String name, String image, int rating, String cost, String state, int id, String generalLoc, String openingHours) {
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.cost = cost;
        this.state = state;
        this.id = id;
        this.generalLoc = generalLoc;
        this.openingHours = openingHours;
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

    public String getCost() {
        return cost;
    }

    public String getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public String getGeneralLoc() {
        return generalLoc;
    }

    public String getOpeningHours() {
        return openingHours;
    }
}
