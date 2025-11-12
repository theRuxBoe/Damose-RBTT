package com.damose.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import backend.model.RisultatoLinea;

public class RisultatoLineaTest {

    @Test
    public void testCostruttoreEGetter() {
        RisultatoLinea r = new RisultatoLinea("078", "CADUTI LIBERAZIONE",
                Arrays.asList("10:33:00", "10:49:00", "11:05:00"));

        assertEquals("078", r.getRouteId());
        assertEquals("CADUTI LIBERAZIONE", r.getDirectionName());
        assertEquals(3, r.getArrivals().size());
    }

    @Test
    public void testEqualsEHashCode() {
        RisultatoLinea r1 = new RisultatoLinea("078", "CADUTI LIBERAZIONE",
                Arrays.asList("10:33:00", "10:49:00"));
        RisultatoLinea r2 = new RisultatoLinea("078", "CADUTI LIBERAZIONE",
                Arrays.asList("10:33:00", "10:49:00"));
        RisultatoLinea r3 = new RisultatoLinea("105", "TARANTELLI",
                Arrays.asList("09:00:00"));

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
    }

    @Test
    public void testToString() {
        RisultatoLinea r = new RisultatoLinea("078", "CADUTI LIBERAZIONE",
                Arrays.asList("10:33:00", "10:49:00", "11:05:00"));
        assertTrue(r.toString().contains("078"));
    }
}
