package com.sap.co2calculator.DTO;

public class CoordinatesDTO {
    private double longitude;
    private double latitude;

    public CoordinatesDTO(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }
}
