package com.sap.co2calculator.DTO;

public class DistanceResponseDTO {

    private double distanceKm;

    public DistanceResponseDTO(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
}
