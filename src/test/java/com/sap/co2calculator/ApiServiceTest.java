package com.sap.co2calculator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.co2calculator.DTO.CoordinatesDTO;
import com.sap.co2calculator.DTO.DistanceResponseDTO;
import com.sap.co2calculator.Exception.ApiRequestException;
import com.sap.co2calculator.Exception.InvalidCityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ApiServiceTest {

    private ApiService apiServiceSpy;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        apiServiceSpy = Mockito.spy(new ApiService()); // Create a spy instance
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetCoordinatesSuccess() throws IOException, InvalidCityException, ApiRequestException {
        String jsonResponse = """
                {
                    "features": [{
                        "geometry": {
                            "coordinates": [-118.25703, 34.05513]
                        }
                    }]
                }
                """;

        doReturn(jsonResponse).when(apiServiceSpy).makeApiGetRequest(anyString());

        CoordinatesDTO coords = apiServiceSpy.getCoordinates("Los Angeles");
        assertNotNull(coords);
        assertEquals(-118.25703, coords.getLongitude());
        assertEquals(34.05513, coords.getLatitude());
    }

    @Test
    void testGetCoordinatesInvalidCity() throws IOException {
        String jsonResponse = """
                {
                    "features": []
                }
                """;

        doReturn(jsonResponse).when(apiServiceSpy).makeApiGetRequest(anyString());
        Exception exception = assertThrows(InvalidCityException.class, () -> apiServiceSpy.getCoordinates("InvalidCity"));
        assertEquals("City not found: InvalidCity", exception.getMessage());
    }

    @Test
    void testGetDistanceSuccess() throws IOException, InvalidCityException, ApiRequestException {
        String jsonResponse = """
            {
                "distances": [[0, 4500300]]
            }
            """;

        doReturn(jsonResponse).when(apiServiceSpy).makeApiPostRequest(anyString(), anyString());
        doReturn(new CoordinatesDTO(-118.25703, 34.05513)).when(apiServiceSpy).getCoordinates("Los Angeles");
        doReturn(new CoordinatesDTO(-74.00597, 40.71427)).when(apiServiceSpy).getCoordinates("New York");

        DistanceResponseDTO distance = apiServiceSpy.getDistance("Los Angeles", "New York");
        assertNotNull(distance);
        assertEquals(4500.3, distance.getDistanceKm(), 0.1);
    }

    @Test
    void testGetDistanceApiFailure() throws InvalidCityException, ApiRequestException {
        when(apiServiceSpy.getDistance("Los Angeles", "New York")).thenThrow(new ApiRequestException("API Error"));

        Exception exception = assertThrows(ApiRequestException.class, () -> apiServiceSpy.getDistance("Los Angeles", "New York"));
        assertEquals("API Error", exception.getMessage());
    }

}
