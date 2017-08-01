package com.map;

/**
 * Created by Севастьян on 30.07.2017.
 */

public class Place {
    public String name;
    public double lat, lan;
    public Place() {

    }
    public Place(String name, double lat, double lan) {
        this.name = name;
        this.lat = lat;
        this.lan = lan;
    }
}