package com.example.orbital_app;

import android.net.Uri;

public class UserProfilePic {

    private String URI;

    public UserProfilePic() {

    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public UserProfilePic(String URI) {
        this.URI = URI;
    }

    public String getURI() {
        return URI;
    }
}
