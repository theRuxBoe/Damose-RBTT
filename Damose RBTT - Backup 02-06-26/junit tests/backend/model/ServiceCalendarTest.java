package backend.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class ServiceCalendarTest {

    @Test
    void testServiceActivity() {
        ServiceCalendar calendar = new ServiceCalendar("WEEKEND_SERVICE");
        LocalDate sabato = LocalDate.of(2026, 2, 14);
        LocalDate domenica = LocalDate.of(2026, 2, 15);
        LocalDate lunedi = LocalDate.of(2026, 2, 16);

        calendar.addDate(sabato);
        calendar.addDate(domenica);

        // Assert
        assertTrue(calendar.isActiveOn(sabato), "Dovrebbe essere attivo di sabato");
        assertTrue(calendar.isActiveOn(domenica), "Dovrebbe essere attivo di domenica");
        assertFalse(calendar.isActiveOn(lunedi), "NON dovrebbe essere attivo di lunedì");
    }
}
