package backend.predict;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

import backend.model.*;
import backend.realtime.GTFSRealTimeClient;
import backend.realtime.RealtimeSnapshot;

public class PredictionEngine {
	
	private final Map<String, List<OrarioFermata>> stopIndex;
	private final Map<String, Corsa> tripById;
	private final GTFSRealTimeClient tripClient;
	private final List<OrarioFermata> allStopTimes;
	

	
	public PredictionEngine(List<OrarioFermata> stopTimes, List<Corsa> corse, GTFSRealTimeClient tClient) {
		
		this.allStopTimes = List.copyOf(stopTimes);
		this.tripClient = tClient;
		this.tripById = new HashMap<String, Corsa>();
		for (Corsa c : corse) {
			
			tripById.put(c.getTripId(), c);
		}
		
		this.stopIndex = buildStopIndex(stopTimes);
	}

	private static Map<String, List<OrarioFermata>> buildStopIndex(List<OrarioFermata> stopTimes) {

		Map<String, List<OrarioFermata>> idx = new HashMap<String, List<OrarioFermata>>();
		
		for (OrarioFermata o : stopTimes) {
			
			if (idx.containsKey(o.getStopId())) {
				
				idx.get(o.getStopId()).add(o);
			}
			
			else {
				
				List<OrarioFermata> list = new ArrayList<OrarioFermata>();
				list.add(o);
				idx.put(o.getStopId(), list);
			}
		}
		
		for (List<OrarioFermata> list : idx.values()) {
            list.sort(Comparator.comparingInt(OrarioFermata::getStopSequence));
        }
		
		return idx;
	}
	
	public List<PredizioneArrivo> predictNextArrivals(String stopId, int limit) {
		 
		 return predictNextArrivals(stopId, limit, null);
	 }
	 
	public List<PredizioneArrivo> predictNextArrivals(String stopId, int limit, RealtimeSnapshot snap) {
		    if (limit <= 0) return List.of();

		    List<PredizioneArrivo> predizioni = new ArrayList<>();
		    List<OrarioFermata> orariInteressati = stopIndex.getOrDefault(stopId, List.of());
		    if (orariInteressati.isEmpty()) return predizioni;

		    LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));
		    LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));

		    // usa lo snap passato; se è null, prova a fetcharlo (compatibilità)
		    RealtimeSnapshot usedSnap = snap;
		    if (usedSnap == null && tripClient != null) {
		        try {
		            usedSnap = tripClient.fetchRealtime();
		        } catch (IOException e) {
		            usedSnap = null;
		        }
		    }

		    for (OrarioFermata o : orariInteressati) {
		        GTFSTime gt = GTFSTime.parseGTFSTime(o.getArrivalTime());
		        LocalTime time = gt.getTime();
		        int dayOffset = gt.getDayOffset();
		        LocalDateTime scheduled = LocalDateTime.of(today.plusDays(dayOffset), time);

		        if (scheduled.isBefore(now)) continue;

		        String tripId = o.getTripId();
		        Corsa corsa = tripById.get(tripId);
		        if (corsa == null) continue;

		        OptionalInt delaySecondsOpt = OptionalInt.empty();
		        LocalDateTime predicted = scheduled;
		        boolean isRealTime = false;
		        
		        if (usedSnap != null) {
		            // DEBUG: verifica cosa c'è nello snapshot per questo trip/stop
		            //System.out.println("DEBUG: checking trip=" + tripId + " stop=" + stopId + " sched=" + scheduled);
		            
		        	OptionalInt d1 = usedSnap.getDelaySecondsByStopId(tripId, stopId);
		            OptionalInt d2 = usedSnap.getDelaySecondsByStopSequence(tripId, o.getStopSequence());
		            OptionalLong p1 = usedSnap.getPredictedEpochSecondsByStopId(tripId, stopId);
		            OptionalLong p2 = usedSnap.getPredictedEpochSecondsByStopSequence(tripId, o.getStopSequence());
		            
		            //System.out.println("DEBUG: delayByStopId=" + (d1.isPresent()?d1.getAsInt():"-") +
		            //                  " delayBySeq=" + (d2.isPresent()?d2.getAsInt():"-") +
		            //                  " predEpochByStopId=" + (p1.isPresent()?p1.getAsLong():"-") +
		            //                   " predEpochBySeq=" + (p2.isPresent()?p2.getAsLong():"-"));

		            // primo tentativo: delay esplicito
		            delaySecondsOpt = d1.isPresent() ? d1 : d2;

		            // se non c'è delay esplicito, cerco predicted epoch e lo trasformo in delay
		            if (delaySecondsOpt.isEmpty()) {
		                OptionalLong predictedEpochOpt = p1.isPresent() ? p1 : p2;
		                if (predictedEpochOpt.isPresent()) {
		                    long predictedEpoch = predictedEpochOpt.getAsLong(); // epoch seconds (UTC) dal feed
		                    long scheduledEpoch = scheduled.atZone(ZoneId.of("Europe/Rome")).toEpochSecond(); // epoch seconds locale->UTC
		                    int computedDelay = (int) (predictedEpoch - scheduledEpoch);
		                    
		                    delaySecondsOpt = OptionalInt.of(computedDelay);

		                    predicted = scheduled.plusSeconds(delaySecondsOpt.orElse(0));
		                    isRealTime = true;
		                }
		            } else {
		                // se abbiamo delay esplicito presente, applichiamolo e segnaliamo realtime
		                predicted = scheduled.plusSeconds(delaySecondsOpt.orElse(0));
		                isRealTime = true;
		            }
		        }

		        int delaySeconds = delaySecondsOpt.orElse(0);
		        LocalTime arrivalTime = predicted.toLocalTime();
		        predizioni.add(new PredizioneArrivo(stopId, corsa.getRouteId(), tripId, corsa.getDirectionName(), arrivalTime, isRealTime, delaySeconds));
		    }

		    predizioni.sort(Comparator.comparing(PredizioneArrivo::getArrivalTime));
		    return predizioni.subList(0, Math.min(limit, predizioni.size()));
		}
	 
	 public Optional<PredizioneArrivo> predictNextForLineAtStop(String stopId, String routeId, String directionName) {
		 
		 return predictNextForLineAtStop(stopId, routeId, directionName, null);
	 }
	 
	 public Optional<PredizioneArrivo> predictNextForLineAtStop(String stopId, String routeId, String directionName, RealtimeSnapshot snap) {
		 
		 List<PredizioneArrivo> allPredictions = predictNextArrivals(stopId, 50, snap);
		 
		 for (PredizioneArrivo p : allPredictions) {
			 
			 if ((p.getStopId().equals(stopId))&&(p.getRouteId().equals(routeId))&&(p.getDirectionName().equalsIgnoreCase(directionName))) {
				 
				 return Optional.of(p);
			 }
		 }
		 
		 return Optional.empty();
	 }

}
