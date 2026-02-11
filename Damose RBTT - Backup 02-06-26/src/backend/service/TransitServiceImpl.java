package backend.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import backend.model.*;
import backend.parser.*;
import backend.predict.PredictionEngine;
import backend.realtime.GTFSRealTimeClient;
import backend.realtime.RealtimeService;
import backend.realtime.RealtimeSnapshot;
import backend.realtime.ServiceAlertInfo;
import backend.realtime.VehiclePositionInfo;

public class TransitServiceImpl implements TransitService {
	
	private List<Linea> linee;
	private List<Corsa> corse;
	private List<OrarioFermata> orari;
	private List<Fermata> fermate;
	private Map<String, ServiceCalendar> serviziCalendario;
	private final GTFSRealTimeClient tripClient;
	private final GTFSRealTimeClient vehicleClient;
	private final GTFSRealTimeClient alertClient;
	private final PredictionEngine predictionEngine;
	private final RealtimeService realtimeService;
	
	public TransitServiceImpl(GTFSRealTimeClient tClient, GTFSRealTimeClient vClient, GTFSRealTimeClient aClient) throws IOException {
		
		GTFSStaticRepository.initIfNeeded("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
		
		this.linee = GTFSStaticRepository.getLinee();
	    this.corse = GTFSStaticRepository.getCorse();
	    this.orari = GTFSStaticRepository.getOrari();
	    this.fermate = GTFSStaticRepository.getFermate();
	    this.serviziCalendario = GTFSStaticRepository.getCalendarMap();
	    
	    this.tripClient = tClient;
	    this.vehicleClient = vClient;
	    this.alertClient = aClient;
	    this.predictionEngine = new PredictionEngine(orari, corse, serviziCalendario, tClient);
	    this.realtimeService = new RealtimeService(tClient, vClient, aClient);
	}
	
	@Override
	public List<RisultatoLinea> trovaLineePerIdFermata(String stopId) throws IllegalArgumentException {
	    
		if (stopId.isBlank() || stopId == null) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		
	    Map<String, Corsa> corsePerTripId = new HashMap<>();
	    for (Corsa c : corse) {
	        corsePerTripId.put(c.getTripId(), c);
	    }
	    
	    Set<RisultatoLinea> setRisultatoLinee = new HashSet<RisultatoLinea>();
	    
	    for (OrarioFermata o : orari) {
	    	
	    	if (o.getStopId().equals(stopId)) {
	    		
	    		Corsa c = corsePerTripId.get(o.getTripId());
	    		
	    		if (c != null) {
	    			
	    			if (isTripActiveToday(c.getTripId(), c.getServiceId())) {
	    				
	    				RisultatoLinea rl = new RisultatoLinea(c.getRouteId(), c.getDirectionName().trim());
	    				
	    				if (rl != null) {
	    					
	    					setRisultatoLinee.add(rl);
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    List<RisultatoLinea> risultato = new ArrayList<RisultatoLinea>(setRisultatoLinee);
	    risultato.sort(Comparator.comparing(RisultatoLinea::getRouteId));
	    return risultato;
		
	}
	
	@Override
	public List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata) throws IllegalArgumentException {
		
		if (nomeFermata.isBlank() || nomeFermata == null) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		
		List<Fermata> fermateConStessoNome = cercaFermate(nomeFermata);
		if (fermateConStessoNome.isEmpty()) return List.of();
		
		Fermata fermataRiferimento = fermateConStessoNome.get(0);
		Set<String> stopsIdGruppo = new HashSet<String>();
		double DistanzaMaxKm = 0.2;
		
		for (Fermata f : fermateConStessoNome) {
			
			if (calcolaDistanzaKm(fermataRiferimento.getLat(), fermataRiferimento.getLon(), f.getLat(), f.getLon()) <= DistanzaMaxKm) {
				stopsIdGruppo.add(f.getStopId());
			}
		}
	    
	    Map<String, Corsa> corsePerTripId = new HashMap<>();
	    for (Corsa c : corse) {
	        corsePerTripId.put(c.getTripId(), c);
	    }
	    
	    Set<RisultatoLinea> setRisultatoLinee = new HashSet<RisultatoLinea>();
	    
	    for (OrarioFermata o : orari) {
	    	
	    	if (stopsIdGruppo.contains(o.getStopId())) {
	    		
	    		Corsa c = corsePerTripId.get(o.getTripId());
	    		
	    		if (c != null) {
	    			
	    			if (isTripActiveToday(c.getTripId(), c.getServiceId())) {
	    				
	    				RisultatoLinea rl = new RisultatoLinea(c.getRouteId(), c.getDirectionName().trim());
	    				
	    				if (rl !=null ) {
	    					
	    					setRisultatoLinee.add(rl);
	    				}
	    			}
	    		}
	    	}
	    }
	    
	    List<RisultatoLinea> risultato = new ArrayList<RisultatoLinea>(setRisultatoLinee);
	    risultato.sort(Comparator.comparing(RisultatoLinea::getRouteId));
	    return risultato;
		
	}
	
	@Override
	public Optional<Fermata> getFermataById(String stopId) {
		
		for (Fermata f : fermate) {
			
			if (f.getStopId().equals(stopId)) {
				
				return Optional.of(f);
			}
		}
		
		return Optional.empty();
	}
	
	@Override
	public List<Linea> cercaLinee(String query) throws IllegalArgumentException {
		
		if (query == null || query.isEmpty()) throw new IllegalArgumentException("Input invalido.");
		
		String q = query.toLowerCase();
		
		List<Linea> risultato = new ArrayList<Linea>();
		
		for (Linea l : linee) {
			
			if (l.getRouteId().toLowerCase().contains(q) || l.getName().toLowerCase().contains(q) || l.getDescription().toLowerCase().contains(q)) {
				
				risultato.add(l);
			}
		}
		
		return risultato;
		
	}
	
	@Override
	public List<Fermata> cercaFermate(String query) throws IllegalArgumentException {
		
		if (query == null || query.isEmpty()) throw new IllegalArgumentException("Input invalido.");
		
		String q = query.toLowerCase();
		
		List<Fermata> risultato = new ArrayList<Fermata>();
		
		for (Fermata f : fermate) {
			
			if (f.getStopId().toLowerCase().equals(q) || f.getName().toLowerCase().contains(q)) {
				
				risultato.add(f);
			}
		}
		
		return risultato;
	}
	
	private boolean isTripActiveToday(String tripId, String serviceId) {
		
		LocalDate today = LocalDate.now();
		ServiceCalendar cal = serviziCalendario.get(serviceId);
		if (cal == null || !cal.isActiveOn(today)) return false;
		
		return true;
	}
	
	private String trovaMigliorTripId(String routeId, String directionName) {
	    String migliorTrip = null;
	    int maxFermate = -1;

	    // Conta quante fermate ha ogni corsa attiva oggi per quella linea/direzione
	    Map<String, Integer> fermatePerCorsa = new HashMap<>();
	    for (OrarioFermata o : orari) {
	    	fermatePerCorsa.put(o.getTripId(), fermatePerCorsa.getOrDefault(o.getTripId(), 0) + 1);
	    }
	    
	    //trova la corsa con il numero più alto di fermate
	    for (Corsa c : corse) {
	        if (c.getRouteId().equals(routeId) && c.getDirectionName().equalsIgnoreCase(directionName)) {
	            if (isTripActiveToday(c.getTripId(), c.getServiceId())) {
	                int numFermate = fermatePerCorsa.getOrDefault(c.getTripId(), 0);
	                if (numFermate > maxFermate) {
	                    maxFermate = numFermate;
	                    migliorTrip = c.getTripId();
	                }
	            }
	        }
	    }
	    
	    return migliorTrip;
	}
	
	private static double calcolaDistanzaKm(double lat1, double lon1, double lat2, double lon2) {
	    final int R = 6371; // raggio medio della Terra in km

	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLon / 2) * Math.sin(dLon / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return R * c;
	}
	
	public List<Fermata> trovaFermatePerLinea(String routeId, String directionName) throws IllegalArgumentException {

		if (routeId == null || routeId.isBlank() || directionName.isBlank() || directionName == null) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		
		String tripIdRiferimento = trovaMigliorTripId(routeId, directionName.trim());
	    if (tripIdRiferimento == null) return new ArrayList<>();
	    
	    Map<String, Fermata> fermateByStopId = new HashMap<String, Fermata>();
	    for (Fermata f : fermate) {
	        fermateByStopId.put(f.getStopId(), f);
	    }

	    //Recuperiamo tutti gli orari di quella corsa specifico
	    List<OrarioFermata> sequenzaOrari = new ArrayList<>();
	    for (OrarioFermata o : orari) {
	        if (o.getTripId().equals(tripIdRiferimento)) {
	            sequenzaOrari.add(o);
	        }
	    }

	    //Ordiniamo la sequenza in base allo stopSequence
	    sequenzaOrari.sort(new Comparator<OrarioFermata>() {
	        @Override
	        public int compare(OrarioFermata o1, OrarioFermata o2) {
	            return Integer.compare(o1.getStopSequence(), o2.getStopSequence());
	        }
	    });
	    
	    List<Fermata> percorso = new ArrayList<>();
	    for (OrarioFermata o : sequenzaOrari) {
	        Fermata f = fermateByStopId.get(o.getStopId());
	        if (f != null) {
	            percorso.add(f);
	        }
	    }

	    return percorso;

	}
	
	public List<WrapperGenerico> ricercaGenerica(String in) throws IllegalArgumentException {
		
		if (in.isBlank() || in == null) {
			throw new IllegalArgumentException("Input di ricerca invalido.");
		}
		
		List<WrapperGenerico> risultato = new ArrayList<WrapperGenerico>();
		
		for (Linea l : cercaLinee(in)) {
			
			risultato.add(new WrapperGenerico("Linea", l));
		}
		
		for (Fermata f : cercaFermate(in)) {
			
			risultato.add(new WrapperGenerico("Fermata", f));
		}
		
		if (risultato.size() == 0) {
			System.out.println("La ricerca non ha prodotto alcun risultato.");
		}
		
		return risultato;
	}
	
	@Override
	public List<PredizioneArrivo> prediciArriviPerFermata(String stopId, int limit) throws IllegalArgumentException {
		
		if (stopId.isBlank() || stopId == null) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		RealtimeSnapshot snap = null;

		try {
			snap = realtimeService.fetchCombinedSnapshot();
		} catch (IOException e) {
			System.out.println("AVVISO: dati in tempo reale non disponibili, pertanto saranno usati quelli statici");
		}
		
		List<PredizioneArrivo> risultati = predictionEngine.predictNextArrivals(stopId, limit, snap);
		
		return risultati;
	}
	
	@Override
	public Optional<PredizioneArrivo> ottieniProssimoArrivoLineaAllaFermata(String stopId, String routeId, String directionName) {
		
		RealtimeSnapshot snap = null;
		
		try {
			snap = realtimeService.fetchCombinedSnapshot();
		} catch (IOException e) {
			System.out.println("AVVISO: dati in tempo reale non disponibili, pertanto saranno usati quelli statici");
		}
		
		return predictionEngine.predictNextForLineAtStop(stopId, routeId, directionName.trim(), snap);
	}
	
    public boolean isOnline() {
    	
    	return predictionEngine.isOnline();
    }
	
	@Override
	public Optional<VehiclePositionInfo> getVehiclePositionForTripId(String tripId){
		
		try {
			RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
			return Optional.ofNullable(snap.getVehiclePositionByTripId(tripId));
		} catch (IOException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public List<ServiceAlertInfo> getAlertsForStopId(String stopId) {
		
		try {
			RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
			return snap.getAlertsByStopId(stopId);
		}
		catch (IOException e) {
			return List.of();
		}
	}
	
	@Override
	public List<ServiceAlertInfo> getAlertsForRouteId(String routeId) {
		
		try {
			RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
			return snap.getAlertsByRouteId(routeId);
		}
		catch (IOException e) {
			return List.of();
		}
	}

	public List<ServiceAlertInfo> getAllAlerts() {
		
		try {
			RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
			return snap.getAllAlerts();
		}
		catch (IOException e) {
			return List.of();
		}
	}
	
	public static TransitServiceImpl createDefault() throws IOException {
		
	        GTFSStaticRepository.init("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
	        
	        GTFSRealTimeClient tripClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb");
	        GTFSRealTimeClient vehicleClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb");
	        GTFSRealTimeClient alertClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_service_alerts_feed.pb");
	        return new TransitServiceImpl(tripClient, vehicleClient, alertClient);
	    }
	
	public RealtimeService getRealtimeService() {
		
		return this.realtimeService;
	}
    
    public static class WrapperGenerico {
    	
    	private String type;
    	private DatoGTF item;
    	
    	WrapperGenerico(String t, DatoGTF i) {
    		
    		this.type = t;
    		this.item = i;
    	}

		public String getType() {
			return type;
		}

		public DatoGTF getItem() {
			return item;
		}
		
		@Override
		public String toString() {
			
			return type+" - "+item.toString();
		}
    }

}
