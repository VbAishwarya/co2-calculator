package com.sap.co2calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class Co2CalculatorTest {

    private final Co2Calculator calculator = new Co2Calculator();

    @Test
    void testValidEmissionCalculation() {
        double distance = 100.0;
        double co2 = calculator.calculateEmission(distance, "diesel-car-medium");
        assertEquals(17.1, co2, 0.01);
    }

    @Test
    void testUnknownTransportMethodReturnsZero() {
        double co2 = calculator.calculateEmission(100.0, "unknown-method");
        assertEquals(0.0, co2);
    }

    @Test
    void testZeroDistanceReturnsZeroEmission() {
        double co2 = calculator.calculateEmission(0.0, "diesel-car-medium");
        assertEquals(0.0, co2);
    }
}


