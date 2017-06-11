package com.contract.entities.geolocation;

/**
 * Created by julbarra on 5/31/17.
 */
public class Geolocation {

    private String type;

    private double[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
