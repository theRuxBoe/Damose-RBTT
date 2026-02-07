package backend.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

import backend.model.*;

public class RealtimeSnapshot {
	
	private final Map<String, Map<Integer, Integer>> delayByTripAndSequence;
    private final Map<String, Map<String, Integer>> delayByTripAndStopId;
    private final Map<String, TripUpdateInfo> tripUpdateInfoMap;
    private final Map<String, VehiclePositionInfo> vehiclePositionByTripId;
    private final Map<String, VehiclePositionInfo> vehiclePositionByVehicleId;
    private final List<ServiceAlertInfo> alerts;
    private Map<String, List<ServiceAlertInfo>> alertsByRouteId;
    private Map<String, List<ServiceAlertInfo>> alertsByTripId;
    private Map<String, List<ServiceAlertInfo>> alertsByStopId;
    
    public RealtimeSnapshot(Map<String, Map<Integer, Integer>> delayByTripAndSequence, Map<String, Map<String, Integer>> delayByTripAndStopId, Map<String, TripUpdateInfo> tripUpdateInfoMap, Map<String, VehiclePositionInfo> vehiclePositionByTripId, Map<String, VehiclePositionInfo> vehiclePositionByVehicleId,
    		List<ServiceAlertInfo> alerts) {
    	
    	this.delayByTripAndSequence = Map.copyOf(delayByTripAndSequence);
    	this.delayByTripAndStopId = Map.copyOf(delayByTripAndStopId);
    	this.tripUpdateInfoMap = Map.copyOf(tripUpdateInfoMap);
    	this.vehiclePositionByTripId = Map.copyOf(vehiclePositionByTripId);
    	this.vehiclePositionByVehicleId = Map.copyOf(vehiclePositionByVehicleId);
    	this.alerts = List.copyOf(alerts);

    }
    
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
	
    //ritardo in secondi per una determinata fermata identificata per stopSequence di una corsa 
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
	
	//ritardo in secondi per una determinata fermata identificata per stopId di una corsa 
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
	
	//dice al PredictionEngine se esistono dati real time per una certa corsa identificata dal tripId
	public boolean hasTripUpdate(String tripId) {
		
		return tripUpdateInfoMap.containsKey(tripId);
	}
	
	//fornisce gli update per ogni singola corsa identificata dal tripId
	public Map<String, TripUpdateInfo> getAllTripUpdates() {
		
		return Map.copyOf(tripUpdateInfoMap);
	}
	
	public VehiclePositionInfo getVehiclePositionByTripId(String tripId) {
		
		return vehiclePositionByTripId.get(tripId);
	}
	
	public VehiclePositionInfo getVehiclePositionByVehicleId(String vehicleId) {
		
		return vehiclePositionByVehicleId.get(vehicleId);
	}
	
	public List<ServiceAlertInfo> getAllAlerts() {
		
		return alerts;
	}
	
	public Map<String, VehiclePositionInfo> getVehiclePositionMapByTripId() {
		return Map.copyOf(vehiclePositionByTripId);
	}

	public Map<String, VehiclePositionInfo> getVehiclePositionMapByVehicleId() {
		return Map.copyOf(vehiclePositionByVehicleId);
	}

	public Map<String, Map<Integer, Integer>> getDelayByTripAndSequence() {
		return Map.copyOf(delayByTripAndSequence);
	}

	public Map<String, Map<String, Integer>> getDelayByTripAndStopId() {
		return Map.copyOf(delayByTripAndStopId);
	}

	public List<ServiceAlertInfo> getAlertsByRouteId(String routeId) {
		
		buildAlertIndexes();
		 
		return alertsByRouteId.getOrDefault(routeId, List.of());
	}
	
	public List<ServiceAlertInfo> getAlertsByTripId(String tripId) {
		
		buildAlertIndexes();
		
		return alertsByTripId.getOrDefault(tripId, List.of());
	}
	
	public List<ServiceAlertInfo> getAlertsByStopId(String stopId) {
		 
		buildAlertIndexes();
		
		return alertsByStopId.getOrDefault(stopId, List.of());
	}
	
	public OptionalLong getPredictedEpochSecondsByStopSequence(String tripId, int stopSequence) {
	    TripUpdateInfo info = tripUpdateInfoMap.get(tripId);
	    if (info == null) return OptionalLong.empty();
	    return info.getPredictedEpochSecondsByStopSequence(stopSequence);
	}

	public OptionalLong getPredictedEpochSecondsByStopId(String tripId, String stopId) {
	    TripUpdateInfo info = tripUpdateInfoMap.get(tripId);
	    if (info == null) return OptionalLong.empty();
	    return info.getPredictedEpochSecondsByStopId(stopId);
	}
}
