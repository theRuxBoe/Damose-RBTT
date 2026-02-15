package backend.realtime;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import backend.parser.GTFSStaticRepository;

class GTFSRealTimeClientTest {
	
	// URL reali di Roma Mobilità
    private static final String URL_TRIPS = "https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb";
    
    private static GTFSRealTimeClient realClient;

    @BeforeAll
    static void setupGlobal() throws IOException {
        realClient = new GTFSRealTimeClient(URL_TRIPS);
        
    }


    @Test
    @Order(1)
    @DisplayName("Test Connessione Client Real-Time")
    void testClientConnection() {
        System.out.println("Tentativo di connessione a Roma Mobilità...");
        
        boolean isOnline = realClient.checkConnectivity(); // O usa un metodo pubblico se checkConnection è privato
        
        // Se il server è giù, questo fallisce.
        assertTrue(isOnline, "Il client dovrebbe riuscire a connettersi a Roma Mobilità.");
    }

    @Test
    @Order(2)
    @DisplayName("Download e Parsing Snapshot Reale")
    void testFetchRealtime() throws IOException {
        RealtimeSnapshot snapshot = realClient.fetchRealtime();
        
        assertNotNull(snapshot, "Lo snapshot scaricato non deve essere null.");
        
        System.out.println("Snapshot scaricato!");
        System.out.println("Numero Trip Updates: " + snapshot.getAllTripUpdates().size());
        
    }

}
