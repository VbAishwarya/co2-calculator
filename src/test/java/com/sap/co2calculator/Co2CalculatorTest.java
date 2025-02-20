package com.sap.co2calculator;

import com.sap.co2calculator.Exception.CalculationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class Co2CalculatorTest {

    private final Co2Calculator calculator = new Co2Calculator();

    @Test
    void testValidEmissionCalculation() throws CalculationException {
        double distance = 100.0;
        double co2 = calculator.calculateEmission(distance, "diesel-car-medium");
        assertEquals(17.1, co2, 0.01);
    }

    @Test
    void testUnknownTransportMethodReturnsCalculationException() {
        Exception exception = assertThrows(CalculationException.class, () -> calculator.calculateEmission(100.0, "unknown-method"));
        assertEquals("Invalid transportation method: unknown-method", exception.getMessage());
    }

    @Test
    void testZeroDistanceReturnsZeroEmission() throws CalculationException {
        double co2 = calculator.calculateEmission(0.0, "diesel-car-medium");
        assertEquals(0.0, co2);
    }

    @Test
    void testInvalidDistanceReturnsCalculationException() {
        Exception exception = assertThrows(CalculationException.class, () -> calculator.calculateEmission(-10.0, "diesel-car-medium"));
        assertEquals("Invalid distance: -10.0 km. Distance cannot be negative.", exception.getMessage());
    }

}


