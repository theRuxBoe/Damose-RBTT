package backend.predict;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import backend.model.PredizioneArrivo;
import backend.parser.GTFSStaticRepository;
import backend.realtime.GTFSRealTimeClient;
import backend.realtime.RealtimeSnapshot;
import backend.service.RouteMetricsDB;

class PredictionEngineTest {
	
    private static final String URL_TRIPS = "https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb";
    
    // Oggetti da testare
    private static GTFSRealTimeClient realClient;
    private static PredictionEngine engine;

    @BeforeAll
    static void setupGlobal() throws IOException {
        System.out.println("--- SETUP: Caricamento Dati Statici e Inizializzazione Client ---");
        
        // Inizializziamo i dati statici
        // Questo scaricherà lo ZIP se non c'è, o userà la cache locale.
        GTFSStaticRepository.initIfNeeded("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
        
        // 2. Creiamo il client
        realClient = new GTFSRealTimeClient(URL_TRIPS);
        
        // 3. Creiamo l'engine
        engine = new PredictionEngine(
            GTFSStaticRepository.getOrari(),
            GTFSStaticRepository.getCorse(),
            GTFSStaticRepository.getCalendarMap(),
            realClient
        );
    }
    
    @Test
    @Order(1)
    @DisplayName("Predizione Arrivi Termini con Engine integrato")
    void testPredictionEngineIntegration() throws IOException {
  
        String stopIdTermini = "80725"; 
        
        RealtimeSnapshot snap = realClient.fetchRealtime();
        
        // Chiediamo le previsioni
        List<PredizioneArrivo> previsioni = engine.predictNextArrivals(stopIdTermini, 10, snap);
        
        assertNotNull(previsioni, "La lista delle previsioni non deve essere null.");
        System.out.println("Trovate " + previsioni.size() + " previsioni per la fermata " + stopIdTermini);
        
        if (!previsioni.isEmpty()) {
            PredizioneArrivo prima = previsioni.get(0);
            System.out.println("Prossimo arrivo: " + prima.getRouteId() + " alle " + prima.getArrivalTime());
            
            // Verifica ordinamento temporale
            for (int i = 0; i < previsioni.size() - 1; i++) {
                assertFalse(previsioni.get(i).getArrivalTime().isAfter(previsioni.get(i+1).getArrivalTime()),
                    "Le previsioni devono essere ordinate cronologicamente.");
            }
        }
    }

    @Test
    @Order(2)
    @DisplayName("Aggiornamento Metriche Reali (Side Effect su File)")
    void testUpdateRouteMetricsReal() throws IOException {
        // Scarichiamo uno snapshot
        RealtimeSnapshot snapshot = realClient.fetchRealtime();
        
        // Istanziamo un RouteMetricsDB
        RouteMetricsDB db = new RouteMetricsDB();
        
        // Se lo snapshot ha dati, proviamo ad aggiornare le metriche
        if (snapshot != null && !snapshot.getAllTripUpdates().isEmpty()) {
            assertDoesNotThrow(() -> engine.analyzeService(snapshot, db),
                "L'aggiornamento delle metriche non dovrebbe lanciare eccezioni.");
            
            System.out.println("Metriche aggiornate nel DB locale basandosi sui dati reali.");
        } else {
            System.out.println("Nessun dato real-time disponibile al momento per testare le metriche (forse è notte?). Test saltato.");
        }
    }

}
