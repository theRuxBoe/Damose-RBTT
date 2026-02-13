package main.java.backend.realtime;

import main.java.backend.service.TransitServiceImpl;
import main.java.backend.service.TransitService;
import main.java.backend.model.PredizioneArrivo;
import main.java.backend.realtime.GTFSRealTimeClient;
import main.java.backend.realtime.RealtimeService;
import main.java.backend.realtime.RealtimeSnapshot;
import main.java.backend.realtime.ServiceAlertInfo;
import main.java.backend.realtime.VehiclePositionInfo;

import java.util.List;
import java.util.Optional;

public class ManualStopTest {
    public static void main(String[] args) throws Exception {
        // --- creazione (modifica gli URL se necessario) ---
        GTFSRealTimeClient tripClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb");
        GTFSRealTimeClient vehicleClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb");
        GTFSRealTimeClient alertClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_service_alerts_feed.pb");

        TransitServiceImpl service = TransitServiceImpl.createDefault(); // se fai createDefault, modifica quella factory per passare i client; altrimenti usa il costruttore custom:
        // TransitServiceImpl service = new TransitServiceImpl(parser, tripClient, vehicleClient, alertClient);

        // fermate da testare
        String[] stops = {"70218","70172","74362"};
        int perStopLimit = 5;

        System.out.println("=== CHECK CONNECTIVITY (any feed probably online) ===");
        System.out.println("Any feed probably online: " + service.getRealtimeService().checkAllConnectivity()); // se hai getter, altrimenti usa i client direttamente

        System.out.println("\n=== FETCH COMBINED SNAPSHOT (once) ===");
        try {
            RealtimeSnapshot snap = new RealtimeService(tripClient, vehicleClient, alertClient).fetchCombinedSnapshot();
            System.out.println("Snapshot fetched. Alerts: " + snap.getAllAlerts().size());
        } catch (Exception e) {
            System.out.println("Non ho potuto fetchare lo snapshot combinato: " + e.getMessage());
            System.out.println("Continuiamo comunque col fallback statico.");
        }

        for (String stopId : stops) {
            System.out.println("\n--- STOP " + stopId + " ---");
            // 1) Predizioni
            List<PredizioneArrivo> preds = service.prediciArriviPerFermata(stopId, perStopLimit);
            System.out.println("Predizioni (" + preds.size() + "):");
            for (PredizioneArrivo p : preds) {
                System.out.println("  " + p.toString() + " tripId=" + p.getTripId() + " dir=" + p.getDirectionName());
            }

            // 2) Se abbiamo un tripId, prova a prendere la posizione del veicolo (prendi il primo pred con tripId non-null)
            Optional<PredizioneArrivo> any = preds.stream().filter(x -> x.getTripId()!=null && !x.getTripId().isBlank()).findFirst();
            if (any.isPresent()) {
                String tripId = any.get().getTripId();
                System.out.println("Provo vehicle position per tripId=" + tripId);
                Optional<VehiclePositionInfo> vpos = service.getVehiclePositionForTripId(tripId); // usa l'API che hai
                if (vpos.isPresent()) {
                    VehiclePositionInfo v = vpos.get();
                    System.out.println("  Vehicle: id=" + v.getVehicleId() + " label=" + v.getVehicleLabel() + " lat=" + v.getLat() + " lon=" + v.getLon() + " ts=" + v.getTimestamp());
                } else {
                    System.out.println("  Nessuna posizione disponibile per quel trip (null).");
                }
            } else {
                System.out.println("  Nessun tripId nelle predizioni per interrogare vehicle position.");
            }

            // 3) Alerts per stop
            List<ServiceAlertInfo> alerts = service.getAlertsForStopId(stopId);
            System.out.println("Alerts per stop: " + alerts.size());
            for (ServiceAlertInfo a : alerts) {
                System.out.println("  alertId=" + a.getAlertId() + " header=" + a.getHeader() + " routeIds=" + a.getRouteIds());
            }
        }

        System.out.println("\n=== RIPETERE CHIAMATA (verifica cache TTL di 30s) ===");
        // richiama la stessa chiamata predici per la prima fermata e vedi che il realtime non effettua più fetch HTTP se entro 30s
        List<PredizioneArrivo> again = service.prediciArriviPerFermata(stops[0], perStopLimit);
        System.out.println("Second call returned " + again.size() + " preds (should be fast / cached).");
    }
}
