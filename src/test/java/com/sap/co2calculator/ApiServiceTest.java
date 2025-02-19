package com.sap.co2calculator;

import com.sap.co2calculator.DTO.CoordinatesDTO;
import com.sap.co2calculator.DTO.DistanceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiServiceTest {

    private ApiService apiService;

    @BeforeEach
    void setUp() {
        apiService = new ApiService();
    }

    @Test
    void testGetCoordinatesSuccess() {
        CoordinatesDTO coords = apiService.getCoordinates("Los Angeles");
        assertNotNull(coords);
        System.out.println("Coordinates: " + coords.getLongitude() + ", " + coords.getLatitude());
    }

    @Test
    void testGetCoordinatesInvalidCity() {
        CoordinatesDTO coords = apiService.getCoordinates("InvalidCity");
        assertNull(coords);
    }

    @Test
    void testGetDistanceSuccess() {
        DistanceResponseDTO distance = apiService.getDistance("Los Angeles", "New York");
        assertNotNull(distance);
        assertTrue(distance.getDistanceKm() > 0);
    }
}
