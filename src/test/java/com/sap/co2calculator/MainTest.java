package com.sap.co2calculator;
import com.sap.co2calculator.DTO.DistanceResponseDTO;
import com.sap.co2calculator.Exception.ApiRequestException;
import com.sap.co2calculator.Exception.CalculationException;
import com.sap.co2calculator.Exception.InvalidCityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class MainTest {

    private ApiService apiServiceMock;
    private Co2Calculator co2CalculatorMock;
    private Main mainApp;

    @BeforeEach
    void setUp() {
        apiServiceMock = mock(ApiService.class);
        co2CalculatorMock = mock(Co2Calculator.class);
        mainApp = new Main(apiServiceMock, co2CalculatorMock);
    }

    @Test
    void testValidExecution() throws ApiRequestException, InvalidCityException, CalculationException {
        when(apiServiceMock.getDistance("Los Angeles", "New York")).thenReturn(new DistanceResponseDTO(4500.3));
        when(co2CalculatorMock.calculateEmission(4500.3, "petrol-car-medium")).thenReturn(770.4);

        String[] args = {"--start=Los Angeles", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertTrue(result.contains("Your trip caused 770.4 kg of CO2-equivalent."));
    }

    @Test
    void testMissingArguments() {
        String[] args = {"--start", "Los Angeles"};
        String result = mainApp.run(args);

        assertEquals("Error: Missing required arguments. Usage: --start <city> --end <city> --transportation-method <method>", result);
    }

    @Test
    void testApiFailure() throws ApiRequestException, InvalidCityException {
        when(apiServiceMock.getDistance("Los Angeles", "New York")).thenReturn(null);

        String[] args = {"--start=Los Angeles", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertEquals("Error: Could not fetch distance.", result);
    }

    @Test
    void testInvalidCityException() throws ApiRequestException, InvalidCityException, CalculationException {
        when(apiServiceMock.getDistance("InvalidCity", "New York")).thenThrow(new InvalidCityException("Invalid city name"));

        String[] args = {"--start=InvalidCity", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertEquals("Error: Invalid city name", result);
    }

    @Test
    void testApiRequestException() throws ApiRequestException, InvalidCityException, CalculationException {
        when(apiServiceMock.getDistance("Los Angeles", "New York")).thenThrow(new ApiRequestException("API failed"));

        String[] args = {"--start=Los Angeles", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertEquals("Error: API request failed after retries. API failed", result);
    }

    @Test
    void testCalculationException() throws ApiRequestException, InvalidCityException, CalculationException {
        when(apiServiceMock.getDistance("Los Angeles", "New York")).thenReturn(new DistanceResponseDTO(4500.3));
        when(co2CalculatorMock.calculateEmission(4500.3, "petrol-car-medium")).thenThrow(new CalculationException("Calculation error"));

        String[] args = {"--start=Los Angeles", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertEquals("Error: Calculation error", result);
    }

    @Test
    void testUnexpectedException() throws ApiRequestException, InvalidCityException, CalculationException {
        when(apiServiceMock.getDistance(anyString(), anyString())).thenThrow(new RuntimeException("Unexpected failure"));

        String[] args = {"--start=Los Angeles", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertEquals("Error: An unexpected error occurred.", result);
    }
}
