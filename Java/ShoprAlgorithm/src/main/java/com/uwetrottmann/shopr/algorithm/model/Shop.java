
package com.uwetrottmann.shopr.algorithm.model;


public class Shop {

    private int id;
    private String name;
    private double latitude;
    private double longitude;

    public int id() {
        return id;
    }

    public Shop id(int id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Shop name(String name) {
        this.name = name;
        return this;
    }

    public double latitude() {
        return latitude;
    }

    public Shop latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }
    
    public double longitude() {
        return longitude;
    }

    public Shop longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

}
