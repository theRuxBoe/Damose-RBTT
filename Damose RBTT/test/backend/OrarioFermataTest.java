package com.damose.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import backend.model.OrarioFermata;

public class OrarioFermataTest {

    @Test
    public void testCostruttoreEGetter() {
        OrarioFermata o = new OrarioFermata("078_1234", "79315", "10:33:00", "10:34:00");

        assertEquals("078_1234", o.getTripId());
        assertEquals("79315", o.getStopId());
        assertEquals("10:33:00", o.getArrivalTime());
        assertEquals("10:34:00", o.getDepartureTime());
    }

    @Test
    public void testEqualsEHashCode() {
        OrarioFermata o1 = new OrarioFermata("078_1234", "79315", "10:33:00", "10:34:00");
        OrarioFermata o2 = new OrarioFermata("078_1234", "79315", "10:33:00", "10:34:00");
        OrarioFermata o3 = new OrarioFermata("105_1", "70000", "10:00:00", "10:01:00");

        assertEquals(o1, o2);
        assertNotEquals(o1, o3);
    }

    @Test
    public void testToString() {
        OrarioFermata o = new OrarioFermata("078_1234", "79315", "10:33:00", "10:34:00");
        assertNotNull(o.toString());
    }
}
