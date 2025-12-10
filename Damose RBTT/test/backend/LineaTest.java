package com.damose.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import backend.model.Linea;

public class LineaTest {

    @Test
    public void testCostruttoreEGetter() {
        Linea l = new Linea("078", "78", "OP1", "TARANTELLI - CADUTI LIBERAZIONE", 3);

        assertEquals("078", l.getRouteId());
        assertEquals("78", l.getName());
        assertEquals("OP1", l.getAgencyId());
        assertEquals("TARANTELLI - CADUTI LIBERAZIONE", l.getDescription());
        assertEquals(3, l.getType());
    }

    @Test
    public void testEqualsEHashCode() {
        Linea l1 = new Linea("078", "78", "OP1", "TARANTELLI - CADUTI LIBERAZIONE", 3);
        Linea l2 = new Linea("078", "78", "OP1", "TARANTELLI - CADUTI LIBERAZIONE", 3);
        Linea l3 = new Linea("105", "105", "OP1", "CADUTI LIBERAZIONE - TARANTELLI", 3);

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    public void testToString() {
        Linea l = new Linea("078", "78", "OP1", "TARANTELLI - CADUTI LIBERAZIONE", 3);
        assertTrue(l.toString().contains("078"));
    }
}