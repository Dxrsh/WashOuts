package com.example.washouts.models;

public class AddressModel {
    String location,house,landmark;

    public AddressModel() {
    }

    public AddressModel(String location, String house, String landmark) {
        this.location = location;
        this.house = house;
        this.landmark = landmark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
