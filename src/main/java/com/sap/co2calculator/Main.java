package com.sap.co2calculator;

import com.sap.co2calculator.DTO.DistanceResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final ApiService apiService;
    private final Co2Calculator co2Calculator;

    public Main(ApiService apiService, Co2Calculator co2Calculator) {
        this.apiService = apiService;
        this.co2Calculator = co2Calculator;
    }

    public String run(String[] args) {
        logger.info("Application started");
        DecimalFormat df = new DecimalFormat("#.#");
        CliParser cliParser = new CliParser(args);

        if (!cliParser.isValid()) {
            logger.error("Missing required arguments.");
            return "Error: Missing required arguments. Usage: --start <city> --end <city> --transportation-method <method>";
        }

        String startCity = cliParser.getStartCity();
        String endCity = cliParser.getEndCity();
        String transportMethod = cliParser.getTransportMethod();

        logger.info("User input: Start={} End={} Transport={}", startCity, endCity, transportMethod);

        DistanceResponseDTO distanceDTO = apiService.getDistance(startCity, endCity);
        if (distanceDTO == null) {
            return "Error: Could not fetch distance.";
        }

        double distance = distanceDTO.getDistanceKm(); // Now safe to call
        double co2Emission = co2Calculator.calculateEmission(distance, transportMethod);
        System.out.println("Distance: " + df.format(distance) + " km, CO2 Emission: " + df.format(co2Emission) + " kg");
        return "Your trip caused " + df.format(co2Emission) + " kg of CO2-equivalent. ";
    }

    public static void main(String[] args) {
        Main app = new Main(new ApiService(), new Co2Calculator());
        System.out.println(app.run(args));
    }
}