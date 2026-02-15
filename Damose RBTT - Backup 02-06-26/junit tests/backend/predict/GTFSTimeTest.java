package backend.predict;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GTFSTimeTest {
	
	@Test
    void testParseTimeUnder24Hours() {
        GTFSTime result = GTFSTime.parseGTFSTime("10:30:00");
        
        assertEquals(LocalTime.of(10, 30, 0), result.getTime());
        assertEquals(0, result.getDayOffset(), "Il dayOffset dovrebbe essere 0");
    }

    @Test
    void testParseTimeOver24Hours() {
        // 25:00:00 dovrebbe diventare 01:00:00 con offset +1
        GTFSTime result = GTFSTime.parseGTFSTime("25:00:00");
        
        assertEquals(LocalTime.of(1, 0, 0), result.getTime());
        assertEquals(1, result.getDayOffset(), "Il dayOffset dovrebbe essere 1");
    }

    @Test
    void testParseTimeMultipleDayOffset() {
        // 49:15:00 = 48h + 1h 15m -> 01:15:00 con offset +2
        GTFSTime result = GTFSTime.parseGTFSTime("49:15:00");
        
        assertEquals(LocalTime.of(1, 15, 0), result.getTime());
        assertEquals(2, result.getDayOffset(), "Il dayOffset dovrebbe essere 2");
    }
    
    @Test
    void testMidnight() {
        GTFSTime result = GTFSTime.parseGTFSTime("24:00:00");
        
        assertEquals(LocalTime.of(0, 0, 0), result.getTime());
        assertEquals(1, result.getDayOffset(), "24:00:00 dovrebbe essere offset 1");
    }

}
