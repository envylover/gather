package com.example.gather;

import java.io.Serializable;

public class Friend implements Serializable {
    private transient String distance;
    public String location;
    public String name;
    public String userId;

    public Friend(String distance,String location, String name){
        this.distance = distance;
        this.location = location;
        this.name = name;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}