package backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class RouteMetricsDBTest {
	
	private RouteMetricsDB db;
	private String TEST_ROUTE_ID = "170";

    @BeforeEach
    void setUp() throws IOException {

        db = new RouteMetricsDB();
    }
    
    @Test
    @Order(1)
    void testUpdateScoreInMemory() throws IOException {
    	int punteggioPredecente = db.getRouteScore(TEST_ROUTE_ID);
        int nuovoPunteggio = 75;
        
        // Supponiamo che il tuo metodo si chiami updateRouteScore o simile
        db.updateRouteScore(TEST_ROUTE_ID, nuovoPunteggio);
        
		assertEquals(punteggioPredecente+nuovoPunteggio, db.getRouteScore(TEST_ROUTE_ID), 
            "Il punteggio in memoria dovrebbe essere aggiornato immediatamente.");
    }

    @Test
    @Order(2)
    void testPersistence() throws IOException {
    	int punteggioPrecedente = db.getRouteScore(TEST_ROUTE_ID);
        int punteggioAtteso = 85;
        db.updateRouteScore(TEST_ROUTE_ID, punteggioAtteso);
        db.saveRouteMetricsDBIfDirty();
        
        // Simuliamo la chiusura e riapertura del DB (forzando il ricaricamento da file)
        RouteMetricsDB nuovoDb = new RouteMetricsDB();
        
        assertEquals(punteggioAtteso+punteggioPrecedente, nuovoDb.getRouteScore(TEST_ROUTE_ID), 
            "Il punteggio dovrebbe essere stato scritto su file e ricaricato correttamente.");
    }

    @Test
    void testRatingSyncAfterUpdate() throws IOException {

        db.updateRouteScore(TEST_ROUTE_ID, 90);
        assertEquals(RatingRoute.ECCELLENTE, db.getRating(TEST_ROUTE_ID));

        db.updateRouteScore(TEST_ROUTE_ID, -500);
        assertEquals(RatingRoute.PESSIMO, db.getRating(TEST_ROUTE_ID));
    }

    @Test
    void testGetRouteScoreSuccess() {
        try {
            int score = db.getRouteScore("170"); 
            assertTrue(score >= -1000 && score <= 1000);
        } catch (NoSuchElementException e) {
        	
        }
    }

    @Test
    void testGetRouteScoreNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            db.getRouteScore("LINEA_FANTASMA_999");
        });
    }

    @Test
    void testGetRatingIntegration() {
    	
        String routeId = "170"; 
        
        try {
            int score = db.getRouteScore(routeId);
            RatingRoute rating = db.getRating(routeId);
            
            if (score >= 50) assertEquals(RatingRoute.ECCELLENTE, rating);
            else if (score >= 10) assertEquals(RatingRoute.BUONO, rating);
        } catch (NoSuchElementException ignored) {}
    }

    @Test
    void testInvalidInput() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> db.getRouteScore(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> db.getRouteScore("   "))
        );
    }

}
