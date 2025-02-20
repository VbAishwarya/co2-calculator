package com.sap.co2calculator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.co2calculator.DTO.CoordinatesDTO;
import com.sap.co2calculator.DTO.DistanceRequestDTO;
import com.sap.co2calculator.DTO.DistanceResponseDTO;
import com.sap.co2calculator.Exception.ApiRequestException;
import com.sap.co2calculator.Exception.InvalidCityException;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoordinatesDTO getCoordinates(String cityName) throws InvalidCityException, ApiRequestException {
        try {
            logger.info("Fetching coordinates for city: {}", cityName);
            String encodedCity = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
            String urlString = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + encodedCity + "&layers=locality";
            String response = makeApiGetRequest(urlString);

            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode features = jsonNode.get("features");

            if (features == null || features.isEmpty()) {
                throw new InvalidCityException("City not found: " + cityName);
            }

            JsonNode coordinates = features.get(0).get("geometry").get("coordinates");
            double longitude = coordinates.get(0).asDouble();
            double latitude = coordinates.get(1).asDouble();

            logger.info("Coordinates for {}: Longitude={}, Latitude={}", cityName, longitude, latitude);
            return new CoordinatesDTO(longitude, latitude);
        } catch (IOException e) {
            throw new ApiRequestException("Failed to fetch coordinates for " + cityName + ": " + e.getMessage());
        }
    }

    public DistanceResponseDTO getDistance(String startCity, String endCity) throws InvalidCityException, ApiRequestException {
        try {
            logger.info("Fetching distance between {} and {}", startCity, endCity);
            DecimalFormat df = new DecimalFormat("#.#");
            CoordinatesDTO startCityCoords = getCoordinates(startCity);
            CoordinatesDTO endCityCoords = getCoordinates(endCity);

            if (startCityCoords == null || endCityCoords == null) {
                logger.error("Error: One or both city coordinates could not be retrieved.");
                throw new InvalidCityException("Error: One or both city coordinates could not be retrieved.");
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

            if (!jsonNode.has("distances")) {
                throw new ApiRequestException("Unexpected response format from distance API.");
            }

            double distanceInMeters = jsonNode.get("distances").get(0).get(1).asDouble();
            double distanceInKm = distanceInMeters / 1000.0;

            logger.info("Distance between {} and {}: {} km", startCity, endCity, df.format(distanceInKm));
            return new DistanceResponseDTO(distanceInKm);

        } catch (IOException e) {
            throw new ApiRequestException("Failed to fetch distance: " + e.getMessage());
        }
    }

    protected String makeApiGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        return scanner.useDelimiter("\\A").next();
    }

    protected String makeApiPostRequest(String urlString, String jsonBody) throws IOException {
        int maxRetries = 3;
        int retryDelay = 2000;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", API_KEY);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.getOutputStream().write(jsonBody.getBytes());

                int responseCode = connection.getResponseCode();

                if (responseCode == 504) {
                    logger.warn("API request failed with 504 Gateway Timeout. Attempt {}/{}", attempt, maxRetries);
                    if (attempt < maxRetries) {
                        Thread.sleep(retryDelay);
                        retryDelay *= 2;
                        continue;
                    }
                }

                Scanner scanner = new Scanner(connection.getInputStream());
                return scanner.useDelimiter("\\A").next();

            } catch (InterruptedException e) {
                logger.error("Thread interrupted during sleep: {}", e.getMessage());
                Thread.currentThread().interrupt(); // Restore the interrupted status
                throw new IOException("API request interrupted.");
            } catch (IOException e) {
                if (attempt == maxRetries) {
                    throw new IOException("Failed to fetch data after " + maxRetries + " attempts: " + e.getMessage());
                }
                logger.warn("API request failed (attempt {}/{}): {}", attempt, maxRetries, e.getMessage());
                try {
                    Thread.sleep(retryDelay);
                    retryDelay *= 2; // Exponential backoff
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new IOException("API request failed after all retries.");
    }
}
