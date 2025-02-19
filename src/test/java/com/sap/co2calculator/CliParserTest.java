package com.sap.co2calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CliParserTest {

    @Test
    void testValidArgumentsWithSpaces() {
        String[] args = {"--start", "Hamburg", "--end", "Berlin", "--transportation-method", "diesel-car-medium"};
        CliParser parser = new CliParser(args);
        assertTrue(parser.isValid());
        assertEquals("Hamburg", parser.getStartCity());
        assertEquals("Berlin", parser.getEndCity());
        assertEquals("diesel-car-medium", parser.getTransportMethod());
    }

    @Test
    void testValidArgumentsWithEquals() {
        String[] args = {"--start=Hamburg", "--end=Berlin", "--transportation-method=diesel-car-medium"};
        CliParser parser = new CliParser(args);
        assertTrue(parser.isValid());
        assertEquals("Hamburg", parser.getStartCity());
        assertEquals("Berlin", parser.getEndCity());
        assertEquals("diesel-car-medium", parser.getTransportMethod());
    }

    @Test
    void testMissingArguments(){
        String[] args = {"--start", "Hamburg"};
        CliParser parser = new CliParser(args);
        assertFalse(parser.isValid());
        assertNull(parser.getEndCity());
        assertNull(parser.getTransportMethod());
    }
}
