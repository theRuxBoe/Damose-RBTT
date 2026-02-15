package backend.realtime;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class RealtimeServiceTest {
	
    private static final String URL_TRIPS = "https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb";
    private static final String URL_VEHICLES = "https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb";
    private static final String URL_ALERTS = "https://romamobilita.it/sites/default/files/rome_rtgtfs_service_alerts_feed.pb";

    private static RealtimeService realtimeService;

    @BeforeAll
    static void setup() throws IOException {
        System.out.println("--- SETUP: Inizializzazione dei 3 Client GTFS ---");


        GTFSRealTimeClient clientTrips = new GTFSRealTimeClient(URL_TRIPS);
        GTFSRealTimeClient clientVehicles = new GTFSRealTimeClient(URL_VEHICLES);
        GTFSRealTimeClient clientAlerts = new GTFSRealTimeClient(URL_ALERTS);


        realtimeService = new RealtimeService(clientTrips, clientVehicles, clientAlerts);
    }

    @Test
    @Order(1)
    @DisplayName("Test Connettività Multipla")
    void testConnectivity() {
        // Verifica che almeno uno dei servizi sia raggiungibile
        boolean isConnected = realtimeService.checkAllConnectivity();
        
        System.out.println("Stato connessione ai server ATAC: " + (isConnected ? "ONLINE" : "OFFLINE"));
        assertTrue(isConnected, "Impossibile connettersi ai server di Roma Mobilità.");
    }

    @Test
    @Order(2)
    @DisplayName("Test Merging: Fusione dei 3 Feed")
    void testGetMergedSnapshot() throws IOException {
        System.out.println("Scaricamento e fusione dei dati in corso...");

        // Questo metodo internamente chiama fetchRealtime() su tutti e 3 i client
        RealtimeSnapshot mergedSnapshot = realtimeService.fetchCombinedSnapshot();

        assertNotNull(mergedSnapshot, "Lo snapshot unificato non deve essere null");

        int numTripUpdates = mergedSnapshot.getAllTripUpdates().size();
        int numVehicles = mergedSnapshot.getVehiclePositionMapByTripId().size(); 
        int numAlerts = mergedSnapshot.getAllAlerts().size(); 

        System.out.println("--- RISULTATO DEL MERGING ---");
        System.out.println("Trip Updates trovati: " + numTripUpdates);
        System.out.println("Veicoli tracciati:    " + numVehicles);
        System.out.println("Allarmi di servizio:  " + numAlerts);

        assertNotNull(mergedSnapshot.getAllTripUpdates());
        assertNotNull(mergedSnapshot.getVehiclePositionMapByTripId());
        assertNotNull(mergedSnapshot.getAllAlerts());
        
    }

}
