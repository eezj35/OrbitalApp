package com.example.orbital_app;

public class Reviews {
    private String user;
    private int rating;
    private String review;
    private int upVote;
    private String place;
    private String id;
    private String uri;


    public Reviews(){}

    public Reviews(String user, int rating, String review, int upVote, String place, String uri) {
        this.user = user;
        this.rating = rating;
        this.review = review;
        this.upVote = upVote;
        this.place = place;
        this.uri = uri;
    }

    public String getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public int getUpVote() {
        return upVote;
    }

    public String getPlace() {
        return place;
    }

    public void setId(String s) {
        this.id = s;
    }

    public String getId() {
        return this.id;
    }

    public String getUri() {
        return uri;
    }
}
