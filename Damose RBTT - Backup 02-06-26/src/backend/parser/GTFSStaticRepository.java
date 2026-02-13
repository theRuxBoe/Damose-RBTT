package backend.parser;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import backend.model.*;

/**
 * The Class GTFSStaticRepository -> It's a caching class that instantiates a GTFSStaticParser with all the related lists of data objects. 
 * This class will provide, upon call, data on routes, stop times, stops, service calendars, and trips to all other classes that need it. 
 * This way, they will all have the same lists with the same objects, thus avoiding the need to instantiate a GTFSStaticParser for each class.
 */
public class GTFSStaticRepository {
	
	/** The boolean initialized, which indicates if the GTFSStaticParser has ever been called before. */
	private static boolean initialized;
	
	/** The stops list. */
	private static List<Fermata> fermate;
    
    /** The routes list. */
    private static List<Linea> linee;
    
    /** The trips list. */
    private static List<Corsa> corse;
    
    /** The stop times list. */
    private static List<OrarioFermata> orari;
    
    /** The service calendar map indexed by service id. */
    private static Map<String, ServiceCalendar> serviziCalendario;

    /**
     * Instantiates a new GTFS static repository.
     */
    private GTFSStaticRepository() {}
    
    /**
     * Inits the GTFSStaticRepository with a url that will be the one used in the inner GTFSStaticParser.
     *
     * @param gtfsUrl the gtfs url
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static synchronized void init(String gtfsUrl) throws IOException {
        if (initialized) return;

        GTFSStaticParser parser = new GTFSStaticParser();
        parser.parseAll(gtfsUrl);

        fermate = parser.getFermate();
        linee = parser.getLinee();
        corse = parser.getCorse();
        orari = parser.getOrari();
        serviziCalendario = parser.getCalendarMap();

        initialized = true;
    }
    
    /**
     * Ensures the initialization of the repository.
     */
    private static void ensureInit() {
        if (!initialized) {
            throw new IllegalStateException("GTFSStaticRepository non inizializzato.");
        }
    }
    
    /**
     * Gets the stops list.
     *
     * @return the stops list
     */
    public static List<Fermata> getFermate() {
    	
    	ensureInit();
    	return fermate;
    }
    
    /**
     * Gets the routes list.
     *
     * @return the routes list
     */
    public static List<Linea> getLinee() {
    	
    	ensureInit();
    	return linee;
    }
    
    /**
     * Gets the stop times list.
     *
     * @return the stop times list
     */
    public static List<OrarioFermata> getOrari() {
    	
    	ensureInit();
    	return orari;
    }
    
    /**
     * Gets the trips list.
     *
     * @return the trips list
     */
    public static List<Corsa> getCorse() {
    	
    	ensureInit();
    	return corse;
    }
    
    /**
     * Gets the service calendar map.
     *
     * @return the service calendar map
     */
    public static Map<String, ServiceCalendar> getCalendarMap() {
    	
    	ensureInit();
    	return serviziCalendario;
    }
    
    /**
     * Inits the GTFSStaticRepository if needed (defensive method).
     *
     * @param url the url
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void initIfNeeded(String url) throws IOException {
        if (!initialized) init(url);
    }

}
