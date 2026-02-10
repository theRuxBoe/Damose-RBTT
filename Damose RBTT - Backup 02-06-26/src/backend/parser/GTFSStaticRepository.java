package backend.parser;
import java.io.IOException;
import java.util.List;

import backend.model.*;

public class GTFSStaticRepository {
	
	private static boolean initialized;
	private static List<Fermata> fermate;
    private static List<Linea> linee;
    private static List<Corsa> corse;
    private static List<OrarioFermata> orari;

    private GTFSStaticRepository() {}
    
    public static synchronized void init(String gtfsUrl) throws IOException {
        if (initialized) return;

        GTFSStaticParser parser = new GTFSStaticParser();
        parser.parseAll(gtfsUrl);

        fermate = parser.getFermate();
        linee = parser.getLinee();
        corse = parser.getCorse();
        orari = parser.getOrari();

        initialized = true;
    }
    
    private static void ensureInit() {
        if (!initialized) {
            throw new IllegalStateException("GTFSStaticRepository non inizializzato");
        }
    }
    
    public static List<Fermata> getFermate() {
    	
    	ensureInit();
    	return fermate;
    }
    
    public static List<Linea> getLinee() {
    	
    	ensureInit();
    	return linee;
    }
    
    public static List<OrarioFermata> getOrari() {
    	
    	ensureInit();
    	return orari;
    }
    
    public static List<Corsa> getCorse() {
    	
    	ensureInit();
    	return corse;
    }
    
    public static void initIfNeeded(String url) throws IOException {
        if (!initialized) init(url);
    }

}
