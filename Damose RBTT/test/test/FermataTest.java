package com.damose.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import backend.model.Fermata;

public class FermataTest {

    @Test
    public void testCostruttoreEGetter() {
        Fermata f = new Fermata("79315", "TARANTELLI", 41.8398, 12.4695);

        assertEquals("79315", f.getStopId());
        assertEquals("TARANTELLI", f.getName());
        assertEquals(41.8398, f.getLat());
        assertEquals(12.4695, f.getLon());
    }

    @Test
    public void testEqualsEHashCode() {
        Fermata f1 = new Fermata("79315", "TARANTELLI", 41.8398, 12.4695);
        Fermata f2 = new Fermata("79315", "TARANTELLI", 41.8398, 12.4695);
        Fermata f3 = new Fermata("70000", "ALTRO", 42.0, 13.0);

        assertEquals(f1, f2);
        assertNotEquals(f1, f3);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void testToString() {
        Fermata f = new Fermata("79315", "TARANTELLI", 41.8398, 12.4695);
        assertTrue(f.toString().contains("TARANTELLI"));
    }
}
