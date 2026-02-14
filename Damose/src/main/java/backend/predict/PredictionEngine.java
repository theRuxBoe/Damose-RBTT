package main.java.backend.predict;

import java.io.IOException;
import java.time.Instant;
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

import main.java.backend.model.*;
import main.java.backend.realtime.GTFSRealTimeClient;
import main.java.backend.realtime.RealtimeSnapshot;
import main.java.backend.realtime.TripUpdateInfo;
import main.java.backend.service.RouteMetricsDB;

/**
 * The Class PredictionEngine -> its task is to calculate the arrival time and any delay of the trips given a fixed stop.
 */
public class PredictionEngine {
	
	/** The stops indexed by their stop ids. */
	private final Map<String, List<OrarioFermata>> stopIndex;
	
	/** The map containing trips indexed by their id. */
	private final Map<String, Corsa> tripById;
	
	/** The trip client. */
	private final GTFSRealTimeClient tripClient;
	
	/** The list with all the static stop times. */
	private final List<OrarioFermata> allStopTimes;
	
	/** The calendar map. */
	private final Map<String, ServiceCalendar> calendarMap;
	
	/** The boolean which can be consulted to check the online status. */
	private boolean lastOnlineStatus = false;

	
	/**
	 * Instantiates a new prediction engine.
	 *
	 * @param stopTimes the stop times
	 * @param corse the corse
	 * @param calendarMap the calendar map
	 * @param tClient the trip client
	 */
	public PredictionEngine(List<OrarioFermata> stopTimes, List<Corsa> corse, Map<String, ServiceCalendar> calendarMap, GTFSRealTimeClient tClient) {
		
		this.allStopTimes = List.copyOf(stopTimes);
		this.tripClient = tClient;
		this.tripById = new HashMap<String, Corsa>();
		this.calendarMap = Map.copyOf(calendarMap);
		
		for (Corsa c : corse) {
			
			tripById.put(c.getTripId(), c);
		}
		
		this.stopIndex = buildStopIndex(stopTimes);
	}

	/**
	 * Builds the stopIndex map.
	 *
	 * @param stopTimes the stop times
	 * @return the map
	 */
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
	
    /**
    * Helper method to find the best applicable delay.
    * Logic:
    * 1. Find the exact delay for stopSequence
    * 2. If the first one is not available, find the exact delay for stopId.
    * 3. (Propagation) Find the last known delay in a stop prior to the current stop sequence.
     *
     * @param tripUpdate the trip update
     * @param currentStopSequence the current stop sequence
     * @param currentStopId the current stop id
     * @return the delay
     */
	private int findBestDelay(TripUpdateInfo tripUpdate, int currentStopSequence, String currentStopId) {
	    Map<Integer, Integer> delaysBySeq = tripUpdate.getDelayByStopSequence();

	    // Match esatto per sequenza (massima affidabilità)
	    if (delaysBySeq.containsKey(currentStopSequence)) {
	        return delaysBySeq.get(currentStopSequence);
	    }

	    // Match esatto per ID fermata
	    Map<String, Integer> delaysById = tripUpdate.getDelayByStopId();
	    if (delaysById.containsKey(currentStopId)) {
	        return delaysById.get(currentStopId);
	    }

	    // Propagazione: cerca la stop sequence più alta che sia però inferiore alla corrente
	    int bestPrevSeq = -1;
	    
	    // Iteriamo sulle stop sequence disponibili nel feed RT
	    for (Integer seq : delaysBySeq.keySet()) {
	        if (seq < currentStopSequence) {
	            // Troviamo la stop sequence inferiore a quella attuale, ma massima tra le altre
	            if (seq > bestPrevSeq) {
	                bestPrevSeq = seq;
	            }
	        }
	    }

	    // Se abbiamo trovato una fermata precedente valida, usiamo quel ritardo
	    if (bestPrevSeq != -1) {
	        return delaysBySeq.get(bestPrevSeq);
	    }

	    // Nessun dato utile trovato (né attuale, né precedente) -> assumiamo puntuale
	    return 0;
	}
	 
	/**
	 * Given the input stop id and an integer that defines how many arrivals you want to display, 
	 * the method calculates the next arrivals (in real time or static, based on the feed availability) of the trips for the selected stop.
	 *
	 * @param stopId the stop id
	 * @param limit the limit
	 * @param snap the snap
	 * @return the list
	 */
	public List<PredizioneArrivo> predictNextArrivals(String stopId, int limit, RealtimeSnapshot snap) {
		
		    if (limit <= 0) return List.of();

		    List<PredizioneArrivo> predizioni = new ArrayList<>();
		    List<OrarioFermata> orariInteressati = stopIndex.getOrDefault(stopId, List.of());
		    if (orariInteressati.isEmpty()) return predizioni;

		    LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));
		    LocalDate today = now.toLocalDate();

		    // Usa lo snapshot passato; se è null, prova a farne un fetch
		    RealtimeSnapshot usedSnap = snap;
		    if (usedSnap == null && tripClient != null) {
		        try {
		            usedSnap = tripClient.fetchRealtime();
		            this.lastOnlineStatus = true;
		        } catch (IOException e) {
		            usedSnap = null;
		            this.lastOnlineStatus = false;
		        }
		    }

		    for (OrarioFermata o : orariInteressati) {
		    	
		    	String tripId = o.getTripId();
		    	
		        Corsa corsa = tripById.get(tripId);
		        
		        if (corsa == null) continue;
		        
		        ServiceCalendar cal = calendarMap.get(corsa.getServiceId());
	            if (cal == null || !cal.isActiveOn(today)) {
	                continue; // Se la corsa non è attiva oggi, viene saltata
	            }
		        
	            // Conversione dell'orario statico
		    	GTFSTime gtfsTime = GTFSTime.parseGTFSTime(o.getArrivalTime());
		        LocalTime time = gtfsTime.getTime();
		        int dayOffset = gtfsTime.getDayOffset();
		        LocalDateTime scheduledDT = today.atTime(time).plusDays(dayOffset);
		        
		        // Si prendono orari fino a 5 minuti prima del momento in cui si chiama il metodo (alcune corse potrebbero essere in ritardo)
		        if (scheduledDT.isBefore(now.minusMinutes(5))) continue;
		        
		        int delaySeconds = 0;
		        LocalDateTime predicted = scheduledDT;
		        boolean isRealTime = false;
		        
		        // Se il feed in tempo reale è disponibile, si calcola il ritardo, altrimenti si usa l'orario statico (predicted rimane uguale a scheduled)
		        if (usedSnap != null && usedSnap.hasTripUpdate(tripId)) {
		        	
		        	TripUpdateInfo info = usedSnap.getAllTripUpdates().get(tripId);
		        	
		        	// Se la corsa è cancellata, salto
		        	if (info.isCancelled()) continue;
		        	
		        	// Abbiamo una predizione assoluta (epoch) per questa fermata
		        	OptionalLong explicitEpoch = info.getPredictedEpochSecondsByStopSequence(o.getStopSequence());
		        	
		        	if (explicitEpoch.isPresent()) {
		        		long epoch = explicitEpoch.getAsLong();
		        		long scheduledEpoch = scheduledDT.atZone(ZoneId.of("Europe/Rome")).toEpochSecond();
		        		
		        		// Calcolo del ritardo come differenza tra lo scheduled statico e l'orario assoluto epoch
		        		delaySeconds = (int) (epoch - scheduledEpoch);
		        		predicted = LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("Europe/Rome"));
		        		isRealTime = true;
		        	}
		        	
		        	// Se non abbiamo predizione assoluta, calcoliamo ritardo propagato basandoci sulle fermate precedenti
		        	else {
		        		
		        		int bestDelay = findBestDelay(info, o.getStopSequence(), o.getStopId());
		        		
		        		boolean hasData = !info.getDelayByStopSequence().isEmpty() || !info.getDelayByStopId().isEmpty();
		        		
		        		// Se il ritardo è 0, verifichiamo se è un "vero" 0 (es. update presente con delay=0)
		                // o se è un default. Per semplicità, se troviamo info sul trip consideriamo RT true.
		        		if (bestDelay != 0 || hasData) {
		        		    delaySeconds = bestDelay;
		        		    predicted = scheduledDT.plusSeconds(delaySeconds);
		        		    isRealTime = true;
		        		}
		        	}	
		        }

		        LocalTime arrivalTime = predicted.toLocalTime();
		        predizioni.add(new PredizioneArrivo(stopId, corsa.getRouteId(), tripId, corsa.getDirectionName(), arrivalTime, isRealTime, delaySeconds, predicted));
		    }

		    predizioni.sort(Comparator.comparing(PredizioneArrivo::getOrarioCompleto));
		    return predizioni.subList(0, Math.min(limit, predizioni.size()));
		}
	 
	/**
	 * This method applies the previous method to calculate the next arrival of a specific route at a given stop.
	 *
	 * @param stopId the stop id
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the optional
	 */
 	public Optional<PredizioneArrivo> predictNextForLineAtStop(String stopId, String routeId, String directionName, RealtimeSnapshot snap) {
		 
		 List<PredizioneArrivo> allPredictions = predictNextArrivals(stopId, 50, snap);
		 
		 for (PredizioneArrivo p : allPredictions) {
			 
			 if ((p.getStopId().equals(stopId))&&(p.getRouteId().equals(routeId))&&(p.getDirectionName().equalsIgnoreCase(directionName))) {
				 
				 return Optional.of(p);
			 }
		 }
		 
		 return Optional.empty();
	 }
	 
	 /**
 	 * A method for providing approximate statistics on the quality of service for each route, based on a "gamification" approach:
     * 1. Real-time updates available for trips are analyzed.
     * 2. Based on the punctuality or delay of each trip, a number of points is added or subtracted from the route affected by the trip.
 	 *
 	 * @param snap the snap
 	 * @param database the database
 	 * @throws IOException Signals that an I/O exception has occurred.
 	 */
 	public void analyzeService(RealtimeSnapshot snap, RouteMetricsDB database) throws IOException {
		 
		    RealtimeSnapshot usedSnap = snap;
		    if (usedSnap == null && tripClient != null) {
		        try {
		            usedSnap = tripClient.fetchRealtime();
		            this.lastOnlineStatus = true;
		        } catch (IOException e) {
		            usedSnap = null;
		            this.lastOnlineStatus = false;
		        }
		    }
		    
		    Map<String, TripUpdateInfo> availableTripUpdates = usedSnap.getAllTripUpdates();
		    
		    if (usedSnap == null) return;
		    
		    if (!availableTripUpdates.isEmpty()) {
		    	
		    	for (TripUpdateInfo info : availableTripUpdates.values()) {
		    		
		    		if (info.isCancelled() == true) {
		    			database.updateRouteScore(info.getRouteId(), -3);
		    			continue;
		    		}
		    		
	    			// Per valutare la corsa ora, si prende il ritardo della prima fermata utile (la prossima).
	                int ritardoSecondi = 0;
	                boolean ritardoTrovato = false;
	                
	                // Ricerca nella mappa per StopSequence (più affidabile)
	                if (!info.getDelayByStopSequence().isEmpty()) {
	                    // Prendiamo il ritardo dell'ultima fermata aggiornata (spesso il più accurato)
	                    ritardoSecondi = info.getDelayByStopSequence().values().stream()
	                                         .findFirst().orElse(0);
	                    ritardoTrovato = true;
	                }
	                
	                // Se non c'è nella mappa StopSequence, si ricerca nella mappa per StopId -> ritardo puramente rappresentativo
	                if (!ritardoTrovato && !info.getDelayByStopId().isEmpty()) {
	                    ritardoSecondi = info.getDelayByStopId().values().stream()
	                                         .findFirst().orElse(0);
	                }

	                double minutiDiRitardo = ritardoSecondi / 60.0;
		    		
	                if (minutiDiRitardo < 5) {
	                	
	                	database.updateRouteScore(info.getRouteId(), +1);
	                }
	                
	                else if (minutiDiRitardo >= 5 && minutiDiRitardo < 20) {
	                	
	                	database.updateRouteScore(info.getRouteId(), -1);
	                }
	                
	                else if (minutiDiRitardo >= 20 && minutiDiRitardo < 40) {
	                	
	                	database.updateRouteScore(info.getRouteId(), -2);
	                }
	                
	                else if (minutiDiRitardo > 40) {
	                	
	                	database.updateRouteScore(info.getRouteId(), -3);
	                }
		    		
		    	}
		    	
		    	database.saveRouteMetricsDBIfDirty();
		    }
	 }
	 
	 /**
 	 * Checks if the system is online basing on the success of snapshot uploads.
 	 *
 	 * @return true, if is online
 	 */
 	public boolean isOnline() {
		 
		 return this.lastOnlineStatus;
	 }
	 
	 

}
