package backend.predict;

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

import backend.model.*;
import backend.realtime.GTFSRealTimeClient;
import backend.realtime.RealtimeSnapshot;
import backend.realtime.TripUpdateInfo;
import backend.service.RouteMetricsDB;

public class PredictionEngine {
	
	private final Map<String, List<OrarioFermata>> stopIndex;
	private final Map<String, Corsa> tripById;
	private final GTFSRealTimeClient tripClient;
	private final List<OrarioFermata> allStopTimes;
	private final Map<String, ServiceCalendar> calendarMap;
	private boolean lastOnlineStatus = false;

	
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
	 * Metodo helper per trovare il miglior ritardo applicabile.
	 * Logica:
	 * 1. Cerca ritardo esatto per stopSequence.
	 * 2. Cerca ritardo esatto per stopId.
	 * 3. (Propagazione) Cerca l'ultimo ritardo noto in una fermata precedente alla sequence attuale.
	 */
	private int findBestDelay(TripUpdateInfo tripUpdate, int currentStopSequence, String currentStopId) {
	    Map<Integer, Integer> delaysBySeq = tripUpdate.getDelayByStopSequence();

	    // 1. Match esatto per sequenza (massima affidabilità)
	    if (delaysBySeq.containsKey(currentStopSequence)) {
	        return delaysBySeq.get(currentStopSequence);
	    }

	    // 2. Match esatto per ID fermata
	    Map<String, Integer> delaysById = tripUpdate.getDelayByStopId();
	    if (delaysById.containsKey(currentStopId)) {
	        return delaysById.get(currentStopId);
	    }

	    // 3. Propagazione: cerca la sequenza più alta che sia però inferiore alla corrente
	    int bestPrevSeq = -1;
	    
	    // Iteriamo sulle stopSequence disponibili nel feed RT
	    for (Integer seq : delaysBySeq.keySet()) {
	        if (seq < currentStopSequence) {
	            // Troviamo il massimo tra quelli minori
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
	
	public List<PredizioneArrivo> predictNextArrivals(String stopId, int limit) {
		 
		 return predictNextArrivals(stopId, limit, null);
	 }
	 
	public List<PredizioneArrivo> predictNextArrivals(String stopId, int limit, RealtimeSnapshot snap) {
		
		    if (limit <= 0) return List.of();

		    List<PredizioneArrivo> predizioni = new ArrayList<>();
		    List<OrarioFermata> orariInteressati = stopIndex.getOrDefault(stopId, List.of());
		    if (orariInteressati.isEmpty()) return predizioni;

		    LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));
		    LocalDate today = now.toLocalDate();

		    // usa lo snap passato; se è null, prova a fetcharlo (compatibilità)
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
		    	
		    	// --- INIZIO DEBUG ---
		        // Stampe per confrontare i formati
		        //if (usedSnap != null && !usedSnap.getAllTripUpdates().isEmpty()) {
		            //System.out.println("DEBUG: TripId Statico cercato -> " + tripId);
		            
		            // tripId "campione" dal feed Realtime per vedere il formato
		            //String rtSampleId = usedSnap.getAllTripUpdates().keySet().iterator().next();
		            //System.out.println("DEBUG: Formato TripId nel Realtime -> " + rtSampleId);
		            
		            // Verifica se il match diretto fallisce
		            //System.out.println("DEBUG: Match diretto trovato? -> " + usedSnap.hasTripUpdate(tripId));
		        //}
		        // --- FINE DEBUG ---
		    	
		        Corsa corsa = tripById.get(tripId);
		        
		        if (corsa == null) continue;
		        
		        ServiceCalendar cal = calendarMap.get(corsa.getServiceId());
	            if (cal == null || !cal.isActiveOn(today)) {
	                continue; // Se la corsa non è attiva oggi, viene saltata
	            }
		        
		    	GTFSTime gtfsTime = GTFSTime.parseGTFSTime(o.getArrivalTime());
		        LocalTime time = gtfsTime.getTime();
		        int dayOffset = gtfsTime.getDayOffset();
		        LocalDateTime scheduledDT = today.atTime(time).plusDays(dayOffset);
		        
		        if (scheduledDT.isBefore(now.minusMinutes(10))) continue;
		        
		        int delaySeconds = 0;
		        LocalDateTime predicted = scheduledDT;
		        boolean isRealTime = false;
		        
		        if (usedSnap != null && usedSnap.hasTripUpdate(tripId)) {
		        	
		        	TripUpdateInfo info = usedSnap.getAllTripUpdates().get(tripId);
		        	
		        	if (info.isCancelled()) continue;
		        	
		        	//Abbiamo una predizione assoluta (epoch) per questa fermata
		        	OptionalLong explicitEpoch = info.getPredictedEpochSecondsByStopSequence(o.getStopSequence());
		        	
		        	if (explicitEpoch.isPresent()) {
		        		long epoch = explicitEpoch.getAsLong();
		        		long scheduledEpoch = scheduledDT.atZone(ZoneId.of("Europe/Rome")).toEpochSecond();
		        		
		        		//calcolo del ritardo come differenza tra scheduled e l'orario assoluto epoch
		        		delaySeconds = (int) (epoch - scheduledEpoch);
		        		predicted = LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("Europe/Rome"));
		        		isRealTime = true;
		        	}
		        	
		        	//Non abbiamo predizione assoluta, calcoliamo ritardo propagato basandoci sulle fermate precedenti
		        	else {
		        		
		        		int bestDelay = findBestDelay(info, o.getStopSequence(), o.getStopId());
		        		
		        		// Se il ritardo è 0, verifichiamo se è un "vero" 0 (es. update presente con delay=0)
		                // o se è un default. Per semplicità, se troviamo info sul trip consideriamo RT true.
		        		if (bestDelay != 0 || !info.getDelayByStopSequence().isEmpty()) {
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
		    		
	    			// Per valutare la corsa ORA, prendiamo il ritardo della prima fermata utile (la prossima).
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
	 
	 public boolean isOnline() {
		 
		 return this.lastOnlineStatus;
	 }
	 
	 

}
