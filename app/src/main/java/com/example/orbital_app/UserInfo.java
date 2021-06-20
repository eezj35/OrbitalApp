package com.example.orbital_app;

public class UserInfo {
    private String house;
    private Boolean isCheck;

    public UserInfo(){}

    public UserInfo(String house, Boolean isCheck) {
        this.house = house;
        this.isCheck = isCheck;
    }

    public String getHouse() {
        return house;
    }

    public Boolean getIsCheck() {return isCheck; }

}
