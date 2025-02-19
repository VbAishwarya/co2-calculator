package com.sap.co2calculator.DTO;

import java.util.List;

public class DistanceRequestDTO {

    private List<List<Double>> locations;
    private List<String> metrics;

    public DistanceRequestDTO(List<List<Double>> locations) {
        this.locations = locations;
        this.metrics = List.of("distance");
    }

    public List<List<Double>> getLocations() {
        return locations;
    }

    public List<String> getMetrics() {
        return metrics;
    }
}
