package backend.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

import backend.model.*;

/**
 * The Class RealtimeSnapshot -> A class that acts as a snapshot containing trip update data, vehicle positions, 
 * and alerts available in real time as fetched from the client.
 * N.B.: To obtain a complete instance of all the data, you need to merge it into the RealtimeSnapshot class.
 */
public class RealtimeSnapshot {
	
	/** The delays indexed by trip id and stop sequence. */
	private final Map<String, Map<Integer, Integer>> delayByTripAndSequence;
    
    /** The delays indexed by trip id and stop id. */
    private final Map<String, Map<String, Integer>> delayByTripAndStopId;
    
    /** The trip update info indexed by trip id. */
    private final Map<String, TripUpdateInfo> tripUpdateInfoMap;
    
    /** The vehicle positions indexed by trip id. */
    private final Map<String, VehiclePositionInfo> vehiclePositionByTripId;
    
    /** The vehicle positions indexed by vehicle id. */
    private final Map<String, VehiclePositionInfo> vehiclePositionByVehicleId;
    
    /** The alerts list. */
    private final List<ServiceAlertInfo> alerts;
    
    /** The alerts indexed by route id. */
    private Map<String, List<ServiceAlertInfo>> alertsByRouteId;
    
    /** The alerts indexed by trip id. */
    private Map<String, List<ServiceAlertInfo>> alertsByTripId;
    
    /** The alerts indexed by stop id. */
    private Map<String, List<ServiceAlertInfo>> alertsByStopId;
    
    /**
     * Instantiates a new realtime snapshot.
     *
     * @param delayByTripAndSequence the delay by trip id and stop sequence
     * @param delayByTripAndStopId the delay by trip id and stop id
     * @param tripUpdateInfoMap the trip update info map
     * @param vehiclePositionByTripId the vehicle position by trip id
     * @param vehiclePositionByVehicleId the vehicle position by vehicle id
     * @param alerts the alerts
     */
    public RealtimeSnapshot(Map<String, Map<Integer, Integer>> delayByTripAndSequence, Map<String, Map<String, Integer>> delayByTripAndStopId, Map<String, TripUpdateInfo> tripUpdateInfoMap, Map<String, VehiclePositionInfo> vehiclePositionByTripId, Map<String, VehiclePositionInfo> vehiclePositionByVehicleId,
    		List<ServiceAlertInfo> alerts) {
    	
    	this.delayByTripAndSequence = Map.copyOf(delayByTripAndSequence);
    	this.delayByTripAndStopId = Map.copyOf(delayByTripAndStopId);
    	this.tripUpdateInfoMap = Map.copyOf(tripUpdateInfoMap);
    	this.vehiclePositionByTripId = Map.copyOf(vehiclePositionByTripId);
    	this.vehiclePositionByVehicleId = Map.copyOf(vehiclePositionByVehicleId);
    	this.alerts = List.copyOf(alerts);

    }
    
    /**
     * Builds the alert indexed maps.
     */
    private void buildAlertIndexes() {
    	
    	this.alertsByRouteId = new HashMap<String, List<ServiceAlertInfo>>();
    	this.alertsByTripId = new HashMap<String, List<ServiceAlertInfo>>();
    	this.alertsByStopId = new HashMap<String, List<ServiceAlertInfo>>();
    	
		 for (ServiceAlertInfo a : this.alerts) {
			 
			 for (String rId : a.getRouteIds()) {
				 
				 if (!(alertsByRouteId.containsKey(rId))) {
					 
					 alertsByRouteId.put(rId, new ArrayList<ServiceAlertInfo>());
					 
				 }
				 
				 alertsByRouteId.get(rId).add(a);
			 }
			 
             for (String tId : a.getTripIds()) {
				 
				 if (!(alertsByTripId.containsKey(tId))) {
					 
					 alertsByTripId.put(tId, new ArrayList<ServiceAlertInfo>());
					 
				 }
				 
				 alertsByTripId.get(tId).add(a);
			 }
             
             for (String sId : a.getStopIds()) {
				 
				 if (!(alertsByStopId.containsKey(sId))) {
					 
					 alertsByStopId.put(sId, new ArrayList<ServiceAlertInfo>());
					 
				 }
				 
				 alertsByStopId.get(sId).add(a);
			 }
			 
		 }
    }
	
    /**
     * Gets the delay in seconds by stop sequence.
     *
     * @param tripId the trip id
     * @param stopSequence the stop sequence
     * @return the delay seconds by stop sequence
     */
    // Ritardo in secondi per una determinata fermata identificata per stop sequence di una corsa 
	public OptionalInt getDelaySecondsByStopSequence(String tripId, int stopSequence) {
		
		Map<Integer, Integer> delaysByStopSequence = delayByTripAndSequence.get(tripId);
		
		if (delaysByStopSequence == null) {
			
			return OptionalInt.empty();
		}
		if (delaysByStopSequence.containsKey(stopSequence)) {
			
			return OptionalInt.of(delaysByStopSequence.get(stopSequence));
		}
		
		return OptionalInt.empty();
	}
	
	/**
	 * Gets the delay in seconds by stop id.
	 *
	 * @param tripId the trip id
	 * @param stopId the stop id
	 * @return the delay seconds by stop id
	 */
	// Ritardo in secondi per una determinata fermata identificata per stop id di una corsa 
	
	public OptionalInt getDelaySecondsByStopId(String tripId, String stopId) {
		
		Map<String, Integer> delaysByStopId = delayByTripAndStopId.get(tripId);
		
		if (delaysByStopId == null) {
			
			return OptionalInt.empty();
			
		}
			
		if (delaysByStopId.containsKey(stopId)) {
			
			return OptionalInt.of(delaysByStopId.get(stopId));
		}
		
		return OptionalInt.empty();
	}
	
	/**
	 * Tells PredictionEngine if real time data exists for a certain trip identified by the trip id.
	 *
	 * @param tripId the trip id
	 * @return true, if successful
	 */

	public boolean hasTripUpdate(String tripId) {
		
		return tripUpdateInfoMap.containsKey(tripId);
	}
	
	/**
	 * Gets the all trip updates for every single trip.
	 *
	 * @return the all trip updates
	 */
	// Fornisce gli update per ogni singola corsa identificata dal trip id
	public Map<String, TripUpdateInfo> getAllTripUpdates() {
		
		return Map.copyOf(tripUpdateInfoMap);
	}
	
	/**
	 * Gets the vehicle position by trip id.
	 *
	 * @param tripId the trip id
	 * @return the vehicle position by trip id
	 */
	public VehiclePositionInfo getVehiclePositionByTripId(String tripId) {
		
		return vehiclePositionByTripId.get(tripId);
	}
	
	/**
	 * Gets the vehicle position by vehicle id.
	 *
	 * @param vehicleId the vehicle id
	 * @return the vehicle position by vehicle id
	 */
	public VehiclePositionInfo getVehiclePositionByVehicleId(String vehicleId) {
		
		return vehiclePositionByVehicleId.get(vehicleId);
	}
	
	/**
	 * Gets all the alerts.
	 *
	 * @return all the alerts
	 */
	public List<ServiceAlertInfo> getAllAlerts() {
		
		return alerts;
	}
	
	/**
	 * Gets a copy of the vehicle position map by trip id.
	 *
	 * @return the vehicle position map by trip id
	 */
	public Map<String, VehiclePositionInfo> getVehiclePositionMapByTripId() {
		return Map.copyOf(vehiclePositionByTripId);
	}

	/**
	 * Gets a copy of the vehicle position map by vehicle id.
	 *
	 * @return the vehicle position map by vehicle id
	 */
	public Map<String, VehiclePositionInfo> getVehiclePositionMapByVehicleId() {
		return Map.copyOf(vehiclePositionByVehicleId);
	}

	/**
	 * Gets the delays map by trip id and stop sequence.
	 *
	 * @return the delay by trip and sequence
	 */
	public Map<String, Map<Integer, Integer>> getDelayByTripAndSequence() {
		return Map.copyOf(delayByTripAndSequence);
	}

	/**
	 * Gets the delays map by trip id and stop id.
	 *
	 * @return the delay by trip and stop id
	 */
	public Map<String, Map<String, Integer>> getDelayByTripAndStopId() {
		return Map.copyOf(delayByTripAndStopId);
	}

	/**
	 * Gets the alerts by route id.
	 *
	 * @param routeId the route id
	 * @return the alerts by route id
	 */
	public List<ServiceAlertInfo> getAlertsByRouteId(String routeId) {
		
		buildAlertIndexes();
		 
		return alertsByRouteId.getOrDefault(routeId, List.of());
	}
	
	/**
	 * Gets the alerts by trip id.
	 *
	 * @param tripId the trip id
	 * @return the alerts by trip id
	 */
	public List<ServiceAlertInfo> getAlertsByTripId(String tripId) {
		
		buildAlertIndexes();
		
		return alertsByTripId.getOrDefault(tripId, List.of());
	}
	
	/**
	 * Gets the alerts by stop id.
	 *
	 * @param stopId the stop id
	 * @return the alerts by stop id
	 */
	public List<ServiceAlertInfo> getAlertsByStopId(String stopId) {
		 
		buildAlertIndexes();
		
		return alertsByStopId.getOrDefault(stopId, List.of());
	}
	
	/**
	 * Gets the predicted epoch seconds (absolute expected time of arrival) by stop sequence and trip id.
	 *
	 * @param tripId the trip id
	 * @param stopSequence the stop sequence
	 * @return the predicted epoch seconds by stop sequence
	 */
	public OptionalLong getPredictedEpochSecondsByStopSequence(String tripId, int stopSequence) {
	    TripUpdateInfo info = tripUpdateInfoMap.get(tripId);
	    if (info == null) return OptionalLong.empty();
	    return info.getPredictedEpochSecondsByStopSequence(stopSequence);
	}

	/**
	 * Gets the predicted epoch seconds (absolute expected time of arrival) by stop id and trip id.
	 *
	 * @param tripId the trip id
	 * @param stopId the stop id
	 * @return the predicted epoch seconds by stop id
	 */
	public OptionalLong getPredictedEpochSecondsByStopId(String tripId, String stopId) {
	    TripUpdateInfo info = tripUpdateInfoMap.get(tripId);
	    if (info == null) return OptionalLong.empty();
	    return info.getPredictedEpochSecondsByStopId(stopId);
	}
}
