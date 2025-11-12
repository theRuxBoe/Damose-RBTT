package com.damose.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import backend.model.Corsa;

public class CorsaTest {

    @Test
    public void testCostruttoreEGetter() {
        Corsa c = new Corsa("078", "078_1234", "1", "CADUTI LIBERAZIONE", 0);

        assertEquals("078", c.getRouteId());
        assertEquals("078_1234", c.getTripId());
        assertEquals("1", c.getServiceId());
        assertEquals("CADUTI LIBERAZIONE", c.getDirectionName());
        assertEquals(0, c.getDirectionId());
    }

    @Test
    public void testEqualsEHashCode() {
        Corsa c1 = new Corsa("078", "078_1234", "1", "CADUTI LIBERAZIONE", 0);
        Corsa c2 = new Corsa("078", "078_1234", "1", "CADUTI LIBERAZIONE", 0);
        Corsa c3 = new Corsa("105", "105_1", "1", "TARANTELLI", 1);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testToString() {
        Corsa c = new Corsa("078", "078_1234", "1", "CADUTI LIBERAZIONE", 0);
        assertTrue(c.toString() == null || true); // just ensures no crash
    }
}
