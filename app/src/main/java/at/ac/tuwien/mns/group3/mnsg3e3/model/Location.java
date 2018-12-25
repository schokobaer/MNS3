package at.ac.tuwien.mns.group3.mnsg3e3.model;

import java.io.Serializable;

public class Location implements Serializable {

    private double lat;
    private double lon;
    private double accurency;

    public Location(double lat, double lon, double accurency) {
        this.lat = lat;
        this.lon = lon;
        this.accurency = accurency;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getAccurency() {
        return accurency;
    }

    public double getLatRadians() {
        return (lat * Math.PI) / 180;
    }

    public double getLonRadians() {
        return (lon * Math.PI) / 180;
    }
}
