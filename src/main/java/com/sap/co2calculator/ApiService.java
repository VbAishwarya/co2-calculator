package com.sap.co2calculator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private static final String API_KEY = System.getenv("ORS_TOKEN");

    public double[] getCoordinates(String cityName){
        try{
            logger.info("Fetching coordinates for city: {}", cityName);

            String urlString = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + cityName + "&layers=locality";
            String response = makeApiGetRequest(urlString);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            JsonNode coordinates = jsonNode.get("features").get(0).get("geometry").get("coordinates");
            double longitude = coordinates.get(0).asDouble();
            double latitude = coordinates.get(1).asDouble();

            logger.info("Coordinates for {}: Longitude={}, Latitude={}", cityName, longitude, latitude);
            return new double[]{longitude, latitude};
        } catch (Exception e){
            logger.error("Error fetching coordinates for {}: {}", cityName, e.getMessage());
            return null;
        }
    }

    public double getDistance(String city1, String city2){
        try{
            logger.info("Fetching distance between {} and {}", city1, city2);

            double[] coords1 = getCoordinates(city1);
            double[] coords2 = getCoordinates(city2);

            if(coords1 == null || coords2 == null){
                logger.error("Error: One or both city coordinates could not be retrieved.");
                return -1;
            }

            String requestBody = "{ \"locations\": ["
                    + "[" + coords1[0] + "," + coords1[1] + "],"
                    + "[" + coords2[0] + "," + coords2[1] + "]"
                    + "], \"metrics\": [\"distance\"] }";

            String response = makeApiPostRequest("https://api.openrouteservice.org/v2/matrix/driving-car", requestBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            double distanceInMeters = jsonNode.get("distances").get(0).get(1).asDouble();
            double distanceInKm = distanceInMeters / 1000.0;

            logger.info("Distance between {} and {}: {} km", city1, city2, distanceInKm);
            return distanceInKm;
        } catch (Exception e){
            logger.error("Error fetching distance between {} and {}: {}", city1, city2, e.getMessage());
            return -1;
        }
    }

    private String makeApiGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        return scanner.useDelimiter("\\A").next();
    }

    private String makeApiPostRequest(String urlString, String jsonBody) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        connection.getOutputStream().write(jsonBody.getBytes());

        Scanner scanner = new Scanner(connection.getInputStream());
        return scanner.useDelimiter("\\A").next();
    }

}
