package backend.favorite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import backend.model.Linea;
import backend.model.RisultatoLinea;
import backend.parser.GTFSStaticRepository;

/**
 * The Class FavoriteRouteDB -> represents a database (a txt file) containing all the users' saved routes, which are represented by a RisultatoLinea object.
 */
public class FavoriteRouteDB {

	/** The favorites file DB routes. */
	private final File favoritesFileDBRoutes;
	
	/** The favorite routes indexed by user id. */
	private final Map<String, List<FavoriteRoute>> favoriteRoutesByUserId;

	/**
	 * Instantiates a new favorite route DB.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public FavoriteRouteDB() throws IOException {
		
        GTFSStaticRepository.initIfNeeded("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
        
		favoritesFileDBRoutes = new File("FavoritesFileDBRoutes.txt");
		
		if (!favoritesFileDBRoutes.exists()) {
			
			favoritesFileDBRoutes.createNewFile();
		}
		
		favoriteRoutesByUserId = new HashMap<String, List<FavoriteRoute>>();
		loadFavoritesFromFile();
	}
	
	/**
	 * Loads saved favorite routes from the database file to fill the favoriteRoutesByUserId map when a new instance of FavoriteRouteDB is created.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadFavoritesFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(favoritesFileDBRoutes))) {
			
			String riga;
			while ((riga = reader.readLine()) != null) {
				
				String idUtenteRiga = riga;
				String lineaRiga = reader.readLine();
				String direzioneLinea = reader.readLine();
				String commRiga = reader.readLine();
				
				reader.readLine(); // Salta riga vuota
				
				if (lineaRiga == null || commRiga == null || direzioneLinea == null) {
					
					break;
				}
				
				try {
					
					String idUtente = idUtenteRiga.substring(idUtenteRiga.indexOf(":")+1).trim();
					String routeId = lineaRiga.substring(lineaRiga.indexOf(":")+1).trim();
					String directionName = direzioneLinea.substring(direzioneLinea.indexOf(":")+1).trim();
					String commento = commRiga.substring(commRiga.indexOf(":")+1).trim();
					
					Optional<RisultatoLinea> optLinea = getLineaById(routeId, directionName);
					if (optLinea.isPresent()) {
						
						favoriteRoutesByUserId.computeIfAbsent(idUtente, k -> new ArrayList<>())
                        .add(new FavoriteRoute(idUtente, optLinea.get(), commento));
					}
				}
				
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Gets the route by its id and direction name.
	 *
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the route by id
	 */
	private Optional<RisultatoLinea> getLineaById(String routeId, String directionName) {
		
		for (RisultatoLinea rl : GTFSStaticRepository.getRisultatiLinea()) {
			
			if (rl.getRouteId().equals(routeId)&&(rl.getDirectionName().equals(directionName))) {
				
				return Optional.of(rl);
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Adds the favorite route in the database and in the map.
	 *
	 * @param userId the user id
	 * @param linea the linea
	 * @param commento the commento
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public synchronized boolean addFavoriteRoute(String userId, RisultatoLinea linea, String commento) throws IOException {
		
		if (userId == null || linea == null) {
			
			throw new IllegalArgumentException("Testo nullo non consentito.");
			
		}
		
		if (commento == null) commento = "";
		commento = commento.replace("\r", " ").replace("\n", " ").trim();
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		if (list == null) {
			
			list = new ArrayList<FavoriteRoute>();
			favoriteRoutesByUserId.put(userId, list);
		}
		
		for (FavoriteRoute f : list) {
			
			if (f.getLineaSalvata().getRouteId().equals(linea.getRouteId())&&(f.getLineaSalvata().getDirectionName().equals(linea.getDirectionName()))) {
				
				throw new FavoriteAlreadyExistingException("Hai già aggiunto questa linea con questa direzione ai tuoi Preferiti");
			}
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDBRoutes, true))) {
			
			writer.write("ID Utente: "+userId);
			writer.newLine();
			writer.write("Linea: "+linea.getRouteId());
			writer.newLine();
			writer.write("Direzione: "+linea.getDirectionName());
			writer.newLine();
			writer.write("Commento: "+commento);
			writer.newLine();
			writer.newLine();
			
		}
		
		list.add(new FavoriteRoute(userId, linea, commento));
		System.out.println("Linea con questa direzione aggiunta ai Preferiti.");
		
		return true;
	}
	
	/**
	 * Checks if a saved favorite route is present in the map (and so in the database).
	 *
	 * @param userId the user id
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return true, if is favorite route present
	 */
	public boolean isFavoriteRoutePresent(String userId, String routeId, String directionName) {
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		if (list == null || list.isEmpty()) return false;
		
		for (FavoriteRoute f : list) {
			
			if (f.getLineaSalvata().getRouteId().equals(routeId)&(f.getLineaSalvata().getDirectionName().equals(directionName))) {
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Finds all the user's saved favorite routes by user id.
	 *
	 * @param userId the user id
	 * @return the optional
	 */

	public synchronized Optional<List<FavoriteRoute>> findFavoriteRoutesByUserId(String userId) {
		
		if (favoriteRoutesByUserId.containsKey(userId)) {
			
			return Optional.of(List.copyOf(favoriteRoutesByUserId.get(userId)));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Finds a certain saved favorite route by user id, route id and direction name.
	 *
	 * @param userId the user id
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the optional
	 */
	//trova un particolare FavoriteRoute di un determinato utente
	public synchronized Optional<FavoriteRoute> findFavoriteRouteByUserIdRouteIdDirName(String userId, String routeId, String directionName) {
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		if (list != null) {
			
			for (FavoriteRoute f : list) {
				
				if (f.getLineaSalvata().getRouteId().equals(routeId)&&f.getLineaSalvata().getDirectionName().equals(directionName)) {
					
					return Optional.of(f);
				}
			}
		}
		
		return Optional.empty();
		
	}
	
	/**
	 * Rewrites file once a favorite is deleted from the database.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void rewriteFile() throws IOException {

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDBRoutes, false))) {

	        for (Map.Entry<String, List<FavoriteRoute>> entry : favoriteRoutesByUserId.entrySet()) {

	            for (FavoriteRoute f : entry.getValue()) {

	                writer.write("ID Utente: " + f.getUserId());
	                writer.newLine();
	                writer.write("Linea: " + f.getLineaSalvata().getRouteId());
	                writer.newLine();
	    			writer.write("Direzione: "+f.getLineaSalvata().getDirectionName());
	    			writer.newLine();
	                writer.write("Commento: " + f.getCommento());
	                writer.newLine();
	                writer.newLine();
	            }
	        }
	    }
	}
	
	/**
	 * Deletes a favorite route.
	 *
	 * @param userId the user id
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the optional
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public synchronized Optional<FavoriteRoute> deleteFavoriteRoute(String userId, String routeId, String directionName) throws IOException {
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		if (list == null) return Optional.empty();
		
		Optional<FavoriteRoute> toDelete = findFavoriteRouteByUserIdRouteIdDirName(userId, routeId, directionName);
		
		if (toDelete.isEmpty()) {
			
			return Optional.empty();
		}
			
		FavoriteRoute deleted = toDelete.get();
		list.remove(deleted);
		rewriteFile();
		
	    if (list.isEmpty()) {
	        favoriteRoutesByUserId.remove(userId);
	    }
		
		return toDelete;
		
	}
}
