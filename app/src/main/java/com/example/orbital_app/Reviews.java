package com.example.orbital_app;

public class Reviews {
    private String user;
    private String userID;
    private int rating;
    private String review;
    private int upVote;
    private String place;
    private String id;



    public Reviews() {
    }


    public Reviews(String user, String userID, int rating, String review, int upVote, String place) {
        this.user = user;
        this.userID = userID;
        this.rating = rating;
        this.review = review;
        this.upVote = upVote;
        this.place = place;

    }

    public String getUserID() {
        return userID;
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


}
