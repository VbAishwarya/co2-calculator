package com.sap.co2calculator;

import java.util.Map;

public class Co2Calculator {

    private static final Map<String, Double> CO2_EMISSIONS = Map.ofEntries(
            Map.entry("diesel-car-small", 142.0),
            Map.entry("petrol-car-small", 154.0),
            Map.entry("plugin-hybrid-car-small", 73.0),
            Map.entry("electric-car-small", 50.0),
            Map.entry("diesel-car-medium", 171.0),
            Map.entry("petrol-car-medium", 192.0),
            Map.entry("plugin-hybrid-car-medium", 110.0),
            Map.entry("electric-car-medium", 58.0),
            Map.entry("diesel-car-large", 209.0),
            Map.entry("petrol-car-large", 282.0),
            Map.entry("plugin-hybrid-car-large", 126.0),
            Map.entry("electric-car-large", 73.0),
            Map.entry("bus-default", 27.0),
            Map.entry("train-default", 6.0)
    );

    public double calculateEmission(double distance, String method){
        double emissionFactor = CO2_EMISSIONS.getOrDefault(method, 0.0);
        return (distance * emissionFactor) / 1000;
    }
}
