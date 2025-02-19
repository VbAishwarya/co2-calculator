package com.sap.co2calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ApiServiceTest {

    private ApiService apiServiceMock;

    @BeforeEach
    void setUp() {
        apiServiceMock = mock(ApiService.class);
    }

    @Test
    void testGetCoordinatesSuccess() throws IOException {
        when(apiServiceMock.getCoordinates("Hamburg")).thenReturn(new double[]{10.0, 53.5});

        double[] coords = apiServiceMock.getCoordinates("Hamburg");
        assertNotNull(coords);
        assertEquals(10.0, coords[0]);
        assertEquals(53.5, coords[1]);
    }

    @Test
    void testGetCoordinatesInvalidCity() throws IOException {
        when(apiServiceMock.getCoordinates("InvalidCity")).thenReturn(null);

        double[] coords = apiServiceMock.getCoordinates("InvalidCity");
        assertNull(coords);
    }

    @Test
    void testGetDistanceSuccess() throws IOException {
        when(apiServiceMock.getDistance("Hamburg", "Berlin")).thenReturn(289.0);

        double distance = apiServiceMock.getDistance("Hamburg", "Berlin");
        assertEquals(289.0, distance);
    }

//    @Test
//    void testGetDistanceApiFailure() throws IOException {
//        when(apiServiceMock.getDistance("Hamburg", "Berlin")).thenThrow(new IOException("API Error"));
//
//        Exception exception = assertThrows(IOException.class, () -> apiServiceMock.getDistance("Hamburg", "Berlin"));
//        assertEquals("API Error", exception.getMessage());
//    }

//    @Test
//    void testGetCoordinatesApiFailure() throws IOException {
//        when(apiServiceMock.getCoordinates("Hamburg")).thenThrow(new IOException("API Error"));
//
//        Exception exception = assertThrows(IOException.class, () -> apiServiceMock.getCoordinates("Hamburg"));
//        assertEquals("API Error", exception.getMessage());
//    }
}
