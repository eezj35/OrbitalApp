package com.example.orbital_app;

public class UserInfoName {
    private String username;
    private String image;
    private String profilePic;

    public UserInfoName() {
    }

    public UserInfoName(String username, String image, String profilePic) {

        this.username = username;
        this.image = image;
        this.profilePic = profilePic;
    }


    public String getUserName() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public String getProfilePic() { return profilePic; }

}