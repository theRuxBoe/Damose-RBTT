package backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import backend.model.Fermata;
import backend.model.Linea;
import backend.model.PredizioneArrivo;
import backend.model.RisultatoLinea;
import backend.realtime.GTFSRealTimeClient;

class TransitServiceImplTest {
	
	private GTFSRealTimeClient tripClient;
    private GTFSRealTimeClient vehicleClient;
    private GTFSRealTimeClient alertClient;
	private TransitServiceImpl service;
	
	@BeforeEach
    void setUp() throws IOException {
        tripClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb");
        vehicleClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb");
        alertClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_service_alerts_feed.pb");

        service = new TransitServiceImpl(tripClient, vehicleClient, alertClient);
    }
	
	@Test
    @Order(1)
    @DisplayName("Ricerca Linea: deve trovare una linea esistente (es. 170)")
    void testGetLineaEsistente() {
        assertDoesNotThrow(() -> {
            Linea linea = service.getLinea("170");
            assertNotNull(linea);
            assertEquals("170", linea.getRouteId());
        });
    }

    @Test
    @Order(2)
    @DisplayName("Ricerca Linea: deve lanciare eccezione per linea inesistente")
    void testGetLineaInesistente() {
        assertThrows(java.util.NoSuchElementException.class, () -> {
            service.getLinea("NAVICELLA_SPAZIALE_999");
        });
    }

    @Test
    @Order(3)
    @DisplayName("Trova Fermate per Linea: verifica sequenza fermate")
    void testTrovaFermatePerLinea() {

        List<Fermata> percorso = service.trovaFermatePerLinea("777", "AGRICOLTURA");

        assertNotNull(percorso);
        assertFalse(percorso.isEmpty(), "Il percorso non dovrebbe essere vuoto per una linea attiva");
        
        assertNotNull(percorso.get(0).getName());
        assertNotNull(percorso.get(0).getStopId());
    }

    @Test
    @Order(4)
    @DisplayName("Ricerca Generica: deve trovare sia linee che fermate")
    void testRicercaGenerica() {

        List<TransitServiceImpl.WrapperGenerico> risultati = service.ricercaGenerica("TERMINI");

        assertFalse(risultati.isEmpty(), "La ricerca 'Termini' dovrebbe produrre risultati");
        
        // Controlliamo che ci siano Wrapper corretti
        boolean trovatoFermata = risultati.stream().anyMatch(w -> w.getType().equals("Fermata"));
        assertTrue(trovatoFermata, "Dovrebbe aver trovato almeno una fermata con 'Termini'");
    }
    
    @Test
    @Order(5)
    @DisplayName("Trova Linee passanti per una Fermata (ID)")
    void testTrovaLineePerIdFermata() {
        String stopId = service.cercaFermate("TERMINI").get(0).getStopId();
        
        List<RisultatoLinea> lineePassanti = service.trovaLineePerIdFermata(stopId);
        assertNotNull(lineePassanti);
    }

    // --- TEST LOGICA REAL-TIME / PREDIZIONE (Con Fallback) ---

    @Test
    @Order(6)
    @DisplayName("Predizione Arrivi: Fallback su Statico quando RealTime fallisce")
    void testPredizioneArriviFallback() {
        
        // Cerchiamo una fermata valida
        String stopId = service.cercaFermate("Termini").get(0).getStopId();
        
        List<PredizioneArrivo> previsioni = service.prediciArriviPerFermata(stopId, 5);
        
        assertNotNull(previsioni);
        if (!previsioni.isEmpty()) {
            PredizioneArrivo p = previsioni.get(0);
            // Se siamo in fallback, realTime deve essere false
            assertFalse(p.isRealTime(), "Con client offline, la predizione deve essere basata sull'orario schedulato (isRealTime = false)");
            System.out.println("Test Fallback OK: Trovato arrivo schedulato: " + p);
        } else {
            System.out.println("Nessun arrivo previsto (statico o real-time), ma nessun crash.");
        }
    }

    @Test
    @Order(7)
    @DisplayName("Integrazione Valutazione Linea")
    void testValutazioneLinea() {
        RatingRoute rating = service.getValutazioneLinea("170");
        assertNotNull(rating, "La valutazione non deve mai essere null (default SUFFICIENTE o 0 -> MEDIOCRE)");
    }

}
