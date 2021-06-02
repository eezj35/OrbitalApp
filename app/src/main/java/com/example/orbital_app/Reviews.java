package com.example.orbital_app;

public class Reviews {
    private String user;
    private int rating;
    private String review;
    private int upVote;

    public Reviews(String user, int rating, String review, int upVote) {
        this.user = user;
        this.rating = rating;
        this.review = review;
        this.upVote = upVote;
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


}
