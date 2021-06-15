package com.example.orbital_app;


import java.util.ArrayList;

public class Locations {
    private String name;
    private String image;
    private int rating;
    private String cost;
    private String state;
    private int id;
    private String generalLoc;
    private String openingHours;
    private String briefDsc;
    private String link = "";
    private String postal;
    private ArrayList<String> activities;

    public Locations(){}



    public Locations(String name, String image, int rating, String cost, String state, int id, String generalLoc,
                     String openingHours, String briefDsc, String link, String postal, ArrayList<String> activities) {
        this.name = name;
        this.image = image;
        this.rating = rating;
        this.cost = cost;
        this.state = state;
        this.id = id;
        this.generalLoc = generalLoc;
        this.openingHours = openingHours;
        this.briefDsc = briefDsc;
        this.link = link;
        this.postal = postal;
        this.activities = activities;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getBriefDsc() {
        return briefDsc;
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

    public String getPostal() {
        return postal;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }
}
