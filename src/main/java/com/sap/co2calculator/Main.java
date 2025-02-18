package com.sap.co2calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("Application started");

        CliParser cliParser = new CliParser(args);

        if(!cliParser.isValid()){
            logger.error("Missing required arguments.");
            System.out.println("Error: Missing required arguments.");
            System.out.println("Usage: --start <city> --end <city> --transportation-method <method>");
            return;
        }

        String startCity = cliParser.getStartCity();
        String endCity = cliParser.getEndCity();
        String transportMethod = cliParser.getTransportMethod();

        logger.info("User input: Start={} End={} Transport={}", startCity, endCity, transportMethod);

        ApiService apiService = new ApiService();
        double distance = apiService.getDistance(startCity, endCity);

        if(distance != -1) {
            System.out.println("Distance between " + startCity + "and" + endCity + ": " + distance + "km");

            Co2Calculator co2Calculator = new Co2Calculator();
            System.out.println("Your trip from " + startCity + " to " + endCity + " using " + transportMethod + " is being calculated...");
            double co2Emission = co2Calculator.calculateEmission(distance, transportMethod);
            System.out.println("Your trip caused " + co2Emission + "kg of CO2-equivalent.");
        }else {
            System.out.println("Error: Could not fetch distance.");
        }
        logger.info("Application finished successfully.");
    }
}