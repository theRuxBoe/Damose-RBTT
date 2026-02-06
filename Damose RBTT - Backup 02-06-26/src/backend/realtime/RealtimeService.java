package backend.realtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.List;

public class RealtimeService {
	
	private final GTFSRealTimeClient tripClient;    
    private final GTFSRealTimeClient vehicleClient; 
    private final GTFSRealTimeClient alertClient;   

    public RealtimeService(GTFSRealTimeClient tripClient,
                           GTFSRealTimeClient vehicleClient,
                           GTFSRealTimeClient alertClient) {
        this.tripClient = tripClient;
        this.vehicleClient = vehicleClient;
        this.alertClient = alertClient;
    }
    
    public RealtimeSnapshot fetchCombinedSnapshot() throws IOException {

        RealtimeSnapshot tripSnap = null;
        RealtimeSnapshot vehSnap = null;
        RealtimeSnapshot alertSnap = null;

        IOException lastEx = null;

        try {
            tripSnap = tripClient.fetchRealtime();
        } catch (IOException e) {
            lastEx = e;
        }

        try {
            vehSnap = vehicleClient.fetchRealtime();
        } catch (IOException e) {
            lastEx = e;
        }

        try {
            alertSnap = alertClient.fetchRealtime();
        } catch (IOException e) {
            lastEx = e;
        }

        // se tutti e tre hanno fallito, rilancia l'ultimo errore
        if (tripSnap == null && vehSnap == null && alertSnap == null && lastEx != null) {
            throw lastEx;
        }
        
        Map<String, Map<Integer,Integer>> delayByTripAndSequence = new HashMap<String, Map<Integer,Integer>>();
        Map<String, Map<String,Integer>> delayByTripAndStopId = new HashMap<String, Map<String,Integer>>();
        Map<String, TripUpdateInfo> tripUpdateInfoMap = new HashMap<String, TripUpdateInfo>();
        Map<String, VehiclePositionInfo> vehiclePositionByTripId = new HashMap<String, VehiclePositionInfo>();
        Map<String, VehiclePositionInfo> vehiclePositionByVehicleId = new HashMap<String, VehiclePositionInfo>();
        List<ServiceAlertInfo> alerts = new ArrayList<>();
        
        if (tripSnap != null) {
            delayByTripAndSequence.putAll(tripSnap.getDelayByTripAndSequence()); 
            delayByTripAndStopId.putAll(tripSnap.getDelayByTripAndStopId());
            tripUpdateInfoMap.putAll(tripSnap.getAllTripUpdates());
        }

        if (vehSnap != null) {
            vehiclePositionByTripId.putAll(vehSnap.getVehiclePositionMapByTripId());
            vehiclePositionByVehicleId.putAll(vehSnap.getVehiclePositionMapByVehicleId());
        }

        if (alertSnap != null) {
            alerts.addAll(alertSnap.getAllAlerts());

    }
        
    RealtimeSnapshot realSnap = new RealtimeSnapshot(
                delayByTripAndSequence,
                delayByTripAndStopId,
                tripUpdateInfoMap,
                vehiclePositionByTripId,
                vehiclePositionByVehicleId,
                alerts);
    
    //System.out.println("DEBUG: trip updates count = " + realSnap.getAllTripUpdates().size());
    realSnap.getAllTripUpdates().keySet().stream().limit(10).forEach(t -> System.out.println("DEBUG: sample tripUpdateId=" + t));
    
    return realSnap;
    }

    public boolean checkAllConnectivity() {
        return tripClient.checkConnectivity() || vehicleClient.checkConnectivity() || alertClient.checkConnectivity();
    }
}
