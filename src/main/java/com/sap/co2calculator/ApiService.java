package com.sap.co2calculator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.co2calculator.DTO.CoordinatesDTO;
import com.sap.co2calculator.DTO.DistanceRequestDTO;
import com.sap.co2calculator.DTO.DistanceResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private static final String API_KEY = System.getenv("ORS_TOKEN");

    public CoordinatesDTO getCoordinates(String cityName){
        try{
            logger.info("Fetching coordinates for city: {}", cityName);
            String encodedCity = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
            String urlString = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + encodedCity + "&layers=locality";
            String response = makeApiGetRequest(urlString);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            JsonNode coordinates = jsonNode.get("features").get(0).get("geometry").get("coordinates");
            double longitude = coordinates.get(0).asDouble();
            double latitude = coordinates.get(1).asDouble();

            logger.info("Coordinates for {}: Longitude={}, Latitude={}", cityName, longitude, latitude);
            return new CoordinatesDTO(longitude, latitude);
        } catch (Exception e){
            logger.error("Error fetching coordinates for {}: {}", cityName, e.getMessage());
            return null;
        }
    }

    public DistanceResponseDTO getDistance(String startCity, String endCity){
        try{
            logger.info("Fetching distance between {} and {}", startCity, endCity);
            DecimalFormat df = new DecimalFormat("#.#");
            CoordinatesDTO startCityCoords = getCoordinates(startCity);
            CoordinatesDTO endCityCoords = getCoordinates(endCity);

            if(startCityCoords == null || endCityCoords == null){
                logger.error("Error: One or both city coordinates could not be retrieved.");
                return null;
            }

            DistanceRequestDTO requestDTO = new DistanceRequestDTO(
                    Arrays.asList(
                            Arrays.asList(startCityCoords.getLongitude(), startCityCoords.getLatitude()),
                            Arrays.asList(endCityCoords.getLongitude(), endCityCoords.getLatitude())
                    )
            );

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(requestDTO);

            String response = makeApiPostRequest("https://api.openrouteservice.org/v2/matrix/driving-car", requestBody);
            JsonNode jsonNode = objectMapper.readTree(response);

            double distanceInMeters = jsonNode.get("distances").get(0).get(1).asDouble();
            double distanceInKm = distanceInMeters / 1000.0;

            logger.info("Distance between {} and {}: {} km", startCity, endCity, df.format(distanceInKm));
            return new DistanceResponseDTO(distanceInKm);
        } catch (Exception e){
            logger.error("Error fetching distance between {} and {}: {}", startCity, endCity, e.getMessage());
            return null;
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
