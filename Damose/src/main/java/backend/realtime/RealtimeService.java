package main.java.backend.realtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.List;

/**
 * The Class RealtimeService -> it merges snapshots, each of which has TripUpdateInfo, VehiclePositionInfo and ServiceAlertInfo respectively.
 */
public class RealtimeService {
	
	/** The trip client (a GTFSRealTimeClient with the URL linked to the trip update protobuf). */
	private final GTFSRealTimeClient tripClient;    
    
    /** The vehicle client (a GTFSRealTimeClient with the URL linked to the vehicle position protobuf). */
    private final GTFSRealTimeClient vehicleClient; 
    
    /** The alert client (a GTFSRealTimeClient with the URL linked to the alert protobuf). */
    private final GTFSRealTimeClient alertClient;   

    /**
     * Instantiates a new realtime service.
     *
     * @param tripClient the trip client
     * @param vehicleClient the vehicle client
     * @param alertClient the alert client
     */
    public RealtimeService(GTFSRealTimeClient tripClient,
                           GTFSRealTimeClient vehicleClient,
                           GTFSRealTimeClient alertClient) {
        this.tripClient = tripClient;
        this.vehicleClient = vehicleClient;
        this.alertClient = alertClient;
    }
    
    /**
     * Method that fetches and merges the realtime snapshots.
     *
     * @return the realtime snapshot
     * @throws IOException Signals that an I/O exception has occurred.
     */
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
    
    return realSnap;
    }

    /**
     * Unified connectivity control method
     *
     * @return true, if successful
     */
    public boolean checkAllConnectivity() {
        return tripClient.checkConnectivity() || vehicleClient.checkConnectivity() || alertClient.checkConnectivity();
    }
}
