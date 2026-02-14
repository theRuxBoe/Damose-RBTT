package main.java.backend.service;

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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import main.java.backend.model.*;
import main.java.backend.parser.*;
import main.java.backend.predict.PredictionEngine;
import main.java.backend.realtime.GTFSRealTimeClient;
import main.java.backend.realtime.RealtimeService;
import main.java.backend.realtime.RealtimeSnapshot;
import main.java.backend.realtime.ServiceAlertInfo;
import main.java.backend.realtime.VehiclePositionInfo;

/**
 * The Class TransitServiceImpl -> the class which acts as the main API for the frontend developer.
 */
public class TransitServiceImpl implements TransitService {
	
	/** The routes list. */
	private List<Linea> linee;
	
	/** The trips list. */
	private List<Corsa> corse;
	
	/** The routes indexed by route id. */
	private Map<String, Linea> lineeByRouteId;
	
	/** The stops indexed by stop id. */
	private Map<String, Fermata> fermateByStopId;
	
	/** The times. */
	private List<OrarioFermata> orari;
	
	/** The stops. */
	private List<Fermata> fermate;
	
	/** The calendar service map. */
	private Map<String, ServiceCalendar> serviziCalendario;
	
	/** A set of RisultatoLinea, wrapper objects which contain a route and its direction name */
	private Set<RisultatoLinea> risultatiLinea;
	
	/** The trip client. */
	private final GTFSRealTimeClient tripClient;
	
	/** The vehicle client. */
	private final GTFSRealTimeClient vehicleClient;
	
	/** The alert client. */
	private final GTFSRealTimeClient alertClient;
	
	/** The prediction engine. */
	private final PredictionEngine predictionEngine;
	
	/** The realtime service. */
	private final RealtimeService realtimeService;

	/** The route metrics DB. */
	private final RouteMetricsDB routeMetricsDB;
	
	/** The scheduler. */
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(); //timer automatico per eseguire periodicamente il controllo qualità del servizio
	
	/**
	 * Instantiates a new transit service impl. 
	 * Once the transit service impl is instantiated, it will automatically do a routes service quality analysis every 30 minutes as long as the software is running.
	 * The timer can be manually stopped through a public method.
	 *
	 * @param tClient the trip client
	 * @param vClient the vehicle client
	 * @param aClient the alert client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public TransitServiceImpl(GTFSRealTimeClient tClient, GTFSRealTimeClient vClient, GTFSRealTimeClient aClient) throws IOException {
		
		GTFSStaticRepository.initIfNeeded("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
		
		this.linee = GTFSStaticRepository.getLinee();
	    this.corse = GTFSStaticRepository.getCorse();
	    this.orari = GTFSStaticRepository.getOrari();
	    this.fermate = GTFSStaticRepository.getFermate();
	    this.serviziCalendario = GTFSStaticRepository.getCalendarMap();
	    this.risultatiLinea = GTFSStaticRepository.getRisultatiLinea();
	    
	    this.tripClient = tClient;
	    this.vehicleClient = vClient;
	    this.alertClient = aClient;
	    this.predictionEngine = new PredictionEngine(orari, corse, serviziCalendario, tClient);
	    this.realtimeService = new RealtimeService(tClient, vClient, aClient);
	    
	    this.routeMetricsDB = new RouteMetricsDB();
	    
	    startAutomaticMonitoring();
	    Map<String, Linea> routeMap = new HashMap<String, Linea>();
	    Map<String, Fermata> stopMap = new HashMap<String, Fermata>();
	    
	    for (Linea l : linee) {
	    	
	    	routeMap.put(l.getRouteId(), l);
	    }
	    
	    for (Fermata f : fermate) {
	    	
	    	stopMap.put(f.getStopId(), f);
	    }
	    
	    this.fermateByStopId = stopMap;
	    this.lineeByRouteId = routeMap;
	    
	}
	
	/**
	 * Gets the rating of a specific route.
	 *
	 * @param routeId the route id
	 * @return the rating of a route
	 */
	public RatingRoute getValutazioneLinea(String routeId) {
		
		return routeMetricsDB.getRating(routeId);
	}
	
	/**
	 * Gets the route.
	 *
	 * @param routeId the route id
	 * @return the route
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws NoSuchElementException the no such element exception
	 */
	@Override
	public Linea getLinea(String routeId) throws IllegalArgumentException, NoSuchElementException {
		
		if (routeId == null || routeId.isBlank()) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		
		Linea l = lineeByRouteId.get(routeId);
		
		if (l == null) {
			
			throw new NoSuchElementException("RouteId non esistente");
		}
		
		return l;
	}
	
	/**
	 * Gets the stop.
	 *
	 * @param stopId the stop id
	 * @return the stop
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws NoSuchElementException the no such element exception
	 */
	@Override
	public Fermata getFermata(String stopId) throws IllegalArgumentException, NoSuchElementException {
		
		if (stopId == null || stopId.isBlank()) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		
		Fermata f = fermateByStopId.get(stopId);
		
		if (f == null) {
			
			throw new NoSuchElementException("RouteId non esistente");
		}
		
		return f;
	}
	
	/**
	 * Method to manually stop the automatic routes service quality analysis.
	 */
	public void stopService() {
	    System.out.println("Arresto del servizio di monitoraggio...");
	    scheduler.shutdown(); // Per spegnere il timer
	}
	
	/**
	 * Starts the automatic routes service quality analysis. It will be repeated every 30 minutes, until the software is closed.
	 */
	private void startAutomaticMonitoring() {
		
		scheduler.scheduleAtFixedRate( () -> {
	        try {
	            System.out.println("[AUTO-MONITOR] Avvio analisi qualità servizio...");
	            
	            // Ottiene lo snapshot più recente in quel momento
	            RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
	            
	            predictionEngine.analyzeService(snap, this.routeMetricsDB);
	            
	            System.out.println("[AUTO-MONITOR] Analisi completata e salvata.");

	        } catch (Exception e) {
	
	            System.err.println("[AUTO-MONITOR] Errore durante l'aggiornamento: " + e.getMessage());
	        }
	    }, 0, 30, TimeUnit.MINUTES);
	}
	
	/**
	 * Gets a map with quality of service statistics for all routes indexed by route id.
	 *
	 * @return the map
	 */
	@Override
	public Map<String, RouteMetricsDB.InfoLinea> ottieniStatisticheServizio() {
		
		return routeMetricsDB.getAllRouteMetrics();
	}
	
	/**
	 * Find the routes that pass through that stop by the stop id.
	 *
	 * @param stopId the stop id
	 * @return the list of the routes
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	@Override
	public List<RisultatoLinea> trovaLineePerIdFermata(String stopId) throws IllegalArgumentException {
	    
		if (stopId == null || stopId.isBlank()) {
			
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
	    				
	    				RisultatoLinea rl = new RisultatoLinea(getLinea(c.getRouteId()), c.getDirectionName().trim());
	    				
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
	
	/**
	 * Find the routes that pass through that stop by the stop name
	 *
	 * @param nomeFermata the stop name
	 * @return the list of RisultatoLinea object
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	@Override
	public List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata) throws IllegalArgumentException {
		
		if (nomeFermata == null || nomeFermata.isBlank()) {
			
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
	    				
	    				RisultatoLinea rl = new RisultatoLinea(getLinea(c.getRouteId()), c.getDirectionName().trim());
	    				
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
	
	/**
	 * General route search.
	 *
	 * @param query the query
	 * @return the routes' list
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	@Override
	public List<RisultatoLinea> cercaLinee(String query) throws IllegalArgumentException {
		
		if (query == null || query.isEmpty()) throw new IllegalArgumentException("Input invalido.");
		
		String q = query.toLowerCase();
		
		List<RisultatoLinea> risultato = new ArrayList<RisultatoLinea>();
		
		for (RisultatoLinea rl : risultatiLinea) {
			
			if (rl.getRouteId().toLowerCase().contains(q) || rl.getLinea().getName().toLowerCase().contains(q)) {
				
				risultato.add(rl);
			}
		}
		
	    risultato.sort(Comparator.comparing(RisultatoLinea::getRouteId));
		
		return risultato;
		
	}
	
	/**
	 * General stop search.
	 *
	 * @param query the query
	 * @return the list
	 * @throws IllegalArgumentException the illegal argument exception
	 */
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
	
	/**
	 * Checks if the trip is active today.
	 *
	 * @param tripId the trip id
	 * @param serviceId the service id
	 * @return true, if is trip active today
	 */
	private boolean isTripActiveToday(String tripId, String serviceId) {
		
		LocalDate today = LocalDate.now();
		ServiceCalendar cal = serviziCalendario.get(serviceId);
		if (cal == null || !cal.isActiveOn(today)) return false;
		
		return true;
	}
	
	/**
	 * Helper method which finds the trip that serves the greatest number of stops today,
	 * in order to use it as a reference trip for the method trovaFermatePerLinea
	 *
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the string
	 */
	private String trovaMigliorTripId(String routeId, String directionName) {
	    String migliorTrip = null;
	    int maxFermate = -1;

	    // Conta quante fermate ha ogni corsa attiva oggi per quella linea/direzione
	    Map<String, Integer> fermatePerCorsa = new HashMap<>();
	    for (OrarioFermata o : orari) {
	    	fermatePerCorsa.put(o.getTripId(), fermatePerCorsa.getOrDefault(o.getTripId(), 0) + 1);
	    }
	    
	    // Trova la corsa con il numero più alto di fermate
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
	
	/**
	 * calculate the distance in kilometers between two stops.
	 *
	 * @param lat1 the lat 1
	 * @param lon1 the lon 1
	 * @param lat2 the lat 2
	 * @param lon2 the lon 2
	 * @return the double
	 */
	private static double calcolaDistanzaKm(double lat1, double lon1, double lat2, double lon2) {
	    final int R = 6371; // Raggio medio della Terra in km

	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLon / 2) * Math.sin(dLon / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return R * c;
	}
	
	/**
	 * Find the path of stops served by a route.
	 *
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the stop list
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public List<Fermata> trovaFermatePerLinea(String routeId, String directionName) throws IllegalArgumentException {

		if (routeId == null || routeId.isBlank()  || directionName == null) {
			
			throw new IllegalArgumentException("Ciao Input invalido.");
		}
		
		String tripIdRiferimento = trovaMigliorTripId(routeId, directionName.trim());
	    if (tripIdRiferimento == null) return new ArrayList<>();
	    
	    Map<String, Fermata> fermateByStopId = new HashMap<String, Fermata>();
	    for (Fermata f : fermate) {
	        fermateByStopId.put(f.getStopId(), f);
	    }

	    // Recuperiamo tutti gli orari di quella corsa specifica
	    List<OrarioFermata> sequenzaOrari = new ArrayList<>();
	    for (OrarioFermata o : orari) {
	        if (o.getTripId().equals(tripIdRiferimento)) {
	            sequenzaOrari.add(o);
	        }
	    }

	    // Ordiniamo la sequenza in base allo stop sequence
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
	
	/**
	 * General search that returns a list of WrapperGenerico objects, which can contain a route or a stop.
	 *
	 * @param in the input string
	 * @return the results list
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public List<WrapperGenerico> ricercaGenerica(String in) throws IllegalArgumentException {
		
		if (in.isBlank() || in == null) {
			throw new IllegalArgumentException("Input di ricerca invalido.");
		}
		
		List<WrapperGenerico> risultato = new ArrayList<WrapperGenerico>();
		
		for (RisultatoLinea l : cercaLinee(in)) {
			
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
	
	/**
	 * Gets the next real-time or static arrivals of trips for a given stop and in an arbitrary quantity
	 *
	 * @param stopId the stop id
	 * @param limit the limit
	 * @return the predicted arrivals list
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	@Override
	public List<PredizioneArrivo> prediciArriviPerFermata(String stopId, int limit) throws IllegalArgumentException {
		
		if (stopId == null || stopId.isBlank()) {
			
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
	
	/**
	 * Gets the next arrival at a given stop on a given route.
	 *
	 * @param stopId the stop id
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the optional
	 */
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
	
    /**
     * Checks if the system is online.
     *
     * @return true, if is online
     */
    public boolean isOnline() {
    	
    	return realtimeService.checkAllConnectivity();
    }
	
	/**
	 * Gets the vehicle position for trip id.
	 *
	 * @param tripId the trip id
	 * @return the vehicle position for trip id
	 */
	@Override
	public Optional<VehiclePositionInfo> getVehiclePositionForTripId(String tripId){
		
		try {
			RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
			return Optional.ofNullable(snap.getVehiclePositionByTripId(tripId));
		} catch (IOException e) {
			return Optional.empty();
		}
	}
	
	/**
	 * Gets the alerts for stop id.
	 *
	 * @param stopId the stop id
	 * @return the alerts for stop id
	 */
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
	
	/**
	 * Gets the alerts for route id.
	 *
	 * @param routeId the route id
	 * @return the alerts for route id
	 */
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

	/**
	 * Gets all the alerts.
	 *
	 * @return all the alerts
	 */
	public List<ServiceAlertInfo> getAllAlerts() {
		
		try {
			RealtimeSnapshot snap = realtimeService.fetchCombinedSnapshot();
			return snap.getAllAlerts();
		}
		catch (IOException e) {
			return List.of();
		}
	}
	
	/**
	 * Default method to instantiate the transit service impl with the proper URLs to the Roma Mobilita site for the clients.
	 *
	 * @return the transit service impl
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TransitServiceImpl createDefault() throws IOException {
		
	        GTFSStaticRepository.init("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
	        
	        GTFSRealTimeClient tripClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_trip_updates_feed.pb");
	        GTFSRealTimeClient vehicleClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb");
	        GTFSRealTimeClient alertClient = new GTFSRealTimeClient("https://romamobilita.it/sites/default/files/rome_rtgtfs_service_alerts_feed.pb");
	        return new TransitServiceImpl(tripClient, vehicleClient, alertClient);
	    }
	
	/**
	 * Gets the realtime service.
	 *
	 * @return the realtime service
	 */
	public RealtimeService getRealtimeService() {
		
		return this.realtimeService;
	}
    
    /**
     * The inner Class WrapperGenerico, an object which can contain or a route or a stop.
     */
    public static class WrapperGenerico {
    	
    	/** The type indicates if the item is a route or a stop. */
	    private String type;
    	
	    /** The item (can be a route or a stop). */
	    private DatoGTF item;
    	
    	/**
	     * Instantiates a new wrapper generico.
	     *
	     * @param t the type
	     * @param i the item
	     */
	    WrapperGenerico(String t, DatoGTF i) {
    		
    		this.type = t;
    		this.item = i;
    	}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * Gets the item.
		 *
		 * @return the item
		 */
		public DatoGTF getItem() {
			return item;
		}
		
		/**
		 * To string.
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			
			return type+" - "+item.toString();
		}
    }

}
