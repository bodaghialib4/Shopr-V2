
package com.uwetrottmann.shopr.model;

import com.google.android.gms.maps.model.LatLng;
import com.uwetrottmann.shopr.algorithm.model.Shop;

public class ShoprShop extends Shop{

    private int id;
    private String name;
    private LatLng location;

    public int id() {
        return id;
    }

    public ShoprShop id(int id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public ShoprShop name(String name) {
        this.name = name;
        return this;
    }

    public LatLng location() {
        return location;
    }

    public ShoprShop location(LatLng location) {
        this.location = location;
        return this;
    }
    
    @Override
    public double latitude() {
		return location.latitude;
    }
    
    @Override
    public double longitude() {
		return location.longitude;
    }

}
