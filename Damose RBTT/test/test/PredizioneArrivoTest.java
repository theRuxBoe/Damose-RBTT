package com.damose.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import backend.model.PredizioneArrivo;

public class PredizioneArrivoTest {

    @Test
    public void testCostruttoreStatico() {
        PredizioneArrivo p = new PredizioneArrivo("79315", "078", "078_1234", LocalTime.parse("10:33:00"));
        assertEquals("078", p.getRouteId());
        assertFalse(p.isRealTime());
        assertEquals(0, p.getDelay());
    }

    @Test
    public void testCostruttoreRealtime() {
        PredizioneArrivo p = new PredizioneArrivo("79315", "078", "078_1234",
                LocalTime.parse("10:38:00"), true, 5);

        assertTrue(p.isRealTime());
        assertEquals(5, p.getDelay());
        assertEquals(LocalTime.parse("10:38:00"), p.getArrivalTime());
    }

    @Test
    public void testEqualsEHashCode() {
        PredizioneArrivo p1 = new PredizioneArrivo("79315", "078", "078_1234", LocalTime.parse("10:33:00"));
        PredizioneArrivo p2 = new PredizioneArrivo("79315", "078", "078_1234", LocalTime.parse("10:33:00"));
        PredizioneArrivo p3 = new PredizioneArrivo("70000", "105", "105_1", LocalTime.parse("10:40:00"));

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testToString() {
        PredizioneArrivo p = new PredizioneArrivo("79315", "078", "078_1234", LocalTime.parse("10:33:00"));
        assertTrue(p.toString().contains("078"));
    }
}
