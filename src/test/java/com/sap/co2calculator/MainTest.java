package com.sap.co2calculator;
import com.sap.co2calculator.DTO.DistanceResponseDTO;
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
    void testValidExecution() {
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
    void testApiFailure() {
        when(apiServiceMock.getDistance("Los Angeles", "New York")).thenReturn(null);

        String[] args = {"--start=Los Angeles", "--end=New York", "--transportation-method=petrol-car-medium"};
        String result = mainApp.run(args);

        assertEquals("Error: Could not fetch distance.", result);
    }
}
