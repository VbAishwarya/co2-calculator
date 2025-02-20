package com.sap.co2calculator;

import com.sap.co2calculator.Exception.CalculationException;

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

    public double calculateEmission(double distance, String transportMethod) throws CalculationException {
        Double emissionFactor = CO2_EMISSIONS.get(transportMethod);

        if (emissionFactor == null) {
            throw new CalculationException("Invalid transportation method: " + transportMethod);
        }
        if (distance < 0) {
            throw new CalculationException("Invalid distance: " + distance + " km. Distance cannot be negative.");
        }
        if (emissionFactor < 0) {
            throw new CalculationException("Invalid CO₂ factor for " + transportMethod + ". Emission factor cannot be negative.");
        }
        double co2Emission = (distance * emissionFactor) / 1000;

        if (co2Emission < 0) {
            throw new CalculationException("Calculated CO2 emission is negative. Check input values.");
        }
        return co2Emission;
    }
}
