package backend.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.LocalTime;

class PredizioneArrivoTest {

    private final String stopId = "80001";
    private final String routeId = "780";
    private final String tripId = "T123";
    private final String direction = "Trastevere";
    private final LocalTime arrivalTime = LocalTime.of(14, 30, 0); // 14:30:00
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void testToStringDatiStatici() {
        // Caso: realTime = false (Dovrebbe scrivere "schedulato")
        PredizioneArrivo pred = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, false, 0, now);
        
        String expected = "Linea 780 - 14:30:00 (schedulato)";
        assertEquals(expected, pred.toString(), "Per i dati non real-time dovrebbe apparire 'schedulato'");
    }

    @Test
    void testToStringInOrario() {
        // Caso: realTime = true, delay = 0 (Dovrebbe scrivere "in orario")
        PredizioneArrivo pred = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 0, now);
        
        String expected = "Linea 780 - 14:30:00 (in orario)";
        assertEquals(expected, pred.toString());
    }

    @Test
    void testToStringRitardoInSecondi() {
        // Caso: ritardo inferiore a 60 secondi (+45s)
        PredizioneArrivo pred = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 45, now);
        
        // Nota: Il tuo codice usa il carattere Unicode "−" (U+2212) o "+"
        String expected = "Linea 780 - 14:30:00 (+45s)";
        assertEquals(expected, pred.toString());
    }

    @Test
    void testToStringAnticipoInSecondi() {
        // Caso: anticipo (ritardo negativo) inferiore a 60 secondi (-30s)
        PredizioneArrivo pred = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, -30, now);
        
        // Attenzione: il codice usa "−" (carattere minus lungo), non il trattino "-" della tastiera
        String expected = "Linea 780 - 14:30:00 (−30s)";
        assertEquals(expected, pred.toString());
    }

    @Test
    void testToStringRitardoInMinuti() {
        // Caso: ritardo di 120 secondi (Dovrebbe arrotondare a +2m)
        PredizioneArrivo pred = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 120, now);
        
        String expected = "Linea 780 - 14:30:00 (+2m)";
        assertEquals(expected, pred.toString());
    }

    @Test
    void testToStringArrotondamentoMinuti() {
        // Caso: ritardo di 89 secondi (89 / 60 = 1.48 -> arrotondato a 1m)
        PredizioneArrivo pred1 = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 89, now);
        assertEquals("Linea 780 - 14:30:00 (+1m)", pred1.toString());

        // Caso: ritardo di 91 secondi (91 / 60 = 1.51 -> arrotondato a 2m)
        PredizioneArrivo pred2 = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 91, now);
        assertEquals("Linea 780 - 14:30:00 (+2m)", pred2.toString());
    }

    @Test
    void testEqualsAndHashCode() {

        PredizioneArrivo p1 = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 0, now);
        PredizioneArrivo p2 = new PredizioneArrivo(stopId, routeId, tripId, direction, arrivalTime, true, 0, now);
        
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
