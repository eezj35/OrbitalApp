package com.example.orbital_app;

import java.util.ArrayList;

public class PrefActivities {
    private String activity1;
    private String activity2;
    private String activity3;
//    private ArrayList<String> activities;

    public PrefActivities(){}

    public PrefActivities(String activity1, String activity2, String activity3) {
        this.activity1 = activity1;
        this.activity2 = activity2;
        this.activity3 = activity3;

    }

    public String getActivity1() {
        return activity1;
    }

    public String getActivity2() {
        return activity2;
    }

    public String getActivity3() {
        return activity3;
    }
}
