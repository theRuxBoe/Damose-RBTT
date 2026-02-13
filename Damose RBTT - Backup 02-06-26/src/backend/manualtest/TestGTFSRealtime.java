package backend.manualtest;

import backend.realtime.GTFSRealTimeClient;
import backend.realtime.RealtimeSnapshot;

/**
 * The Class TestGTFSRealtime.
 */
public class TestGTFSRealtime {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
        try {
            // ✔️ Feed GTFS-RT reale e pubblico (TripUpdates)
            String feedUrl = "https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb";

            GTFSRealTimeClient client = new GTFSRealTimeClient(feedUrl);

            System.out.println(">>> Scarico i dati GTFS-RT...");
            RealtimeSnapshot snapshot = client.fetchRealtime();

            System.out.println("-------------------------------------------------");
            System.out.println("Snapshot ricevuto!");
            System.out.println("Numero di trip con aggiornamenti: " + snapshot.getAllTripUpdates().size());
            System.out.println("-------------------------------------------------");

            // stampa un esempio di 5 tripUpdateInfo
            snapshot.getAllTripUpdates()
                    .entrySet()
                    .stream()
                    .limit(5)
                    .forEach(e -> {
                        System.out.println("Trip ID: " + e.getKey());
                        System.out.println("Info: " + e.getValue());
                        System.out.println("-------------------------------------------------");
                    });

            // Test della cache a 30s
            System.out.println("\n>>> Riprovo dopo 5 secondi (dovrebbe usare la cache)...");
            Thread.sleep(5000);

            RealtimeSnapshot cached = client.fetchRealtime();
            System.out.println("La seconda risposta proviene dalla cache? " + (cached == snapshot));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

