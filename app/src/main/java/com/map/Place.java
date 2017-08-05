package com.map;

/**
 * Created by Севастьян on 30.07.2017.
 */

public class Place {

    public double lat, lan;
    public String name, about;
    public float rating;
    public Place() {

    }
    public Place(String name, double lat, double lan) {
        this.name = name;
        this.lat = lat;
        this.lan = lan;
    }
    public Place(String name, String about, float rating, double lat, double lan) {
        this.rating=rating;
        this.about=about;
        this.name = name;
        this.lat = lat;
        this.lan = lan;
    }
}