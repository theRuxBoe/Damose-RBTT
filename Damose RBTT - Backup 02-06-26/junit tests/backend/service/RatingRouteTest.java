package backend.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RatingRouteTest {
	
	@ParameterizedTest(name = "Punteggio {0} deve essere {1}")
    @CsvSource({
        "60, ECCELLENTE",   
        "50, ECCELLENTE",   
        "49, BUONO",        
        "10, BUONO",        
        "9, SUFFICIENTE",   
        "0, SUFFICIENTE",    
        "-1, SUFFICIENTE",      
        "-10, SUFFICIENTE",    
        "-11, MEDIOCRE", 
        "-50, MEDIOCRE",
        "-70, PESSIMO"
    })
    @DisplayName("Verifica le soglie di punteggio per il Rating")
    void testFromScoreCorrectThresholds(int score, RatingRoute expectedRating) {
        assertEquals(expectedRating, RatingRoute.fromScore(score));
    }

    @Test
    @DisplayName("Verifica che le etichette testuali siano corrette")
    void testGetEtichetta() {
        assertAll(
            () -> assertEquals("Eccellente", RatingRoute.ECCELLENTE.getEtichetta()),
            () -> assertEquals("Buono", RatingRoute.BUONO.getEtichetta()),
            () -> assertEquals("Sufficiente", RatingRoute.SUFFICIENTE.getEtichetta()),
            () -> assertEquals("Mediocre", RatingRoute.MEDIOCRE.getEtichetta()),
            () -> assertEquals("Pessimo", RatingRoute.PESSIMO.getEtichetta())
        );
    }
}
