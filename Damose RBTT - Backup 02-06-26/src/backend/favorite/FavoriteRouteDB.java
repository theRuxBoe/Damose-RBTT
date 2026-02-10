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
import backend.parser.GTFSStaticParser;

public class FavoriteRouteDB {

	private final File favoritesFileDBRoutes;
	private final Map<String, List<FavoriteRoute>> favoriteRoutesByUserId;
	private final GTFSStaticParser parser;

	public FavoriteRouteDB() throws IOException {
		
		parser = new GTFSStaticParser();
		parser.parseAll("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
		
		favoritesFileDBRoutes = new File("FavoritesFileDBRoutes.txt");
		
		if (!favoritesFileDBRoutes.exists()) {
			
			favoritesFileDBRoutes.createNewFile();
		}
		
		favoriteRoutesByUserId = new HashMap<String, List<FavoriteRoute>>();
		loadFavoritesFromFile();
	}
	
	private void loadFavoritesFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(favoritesFileDBRoutes))) {
			
			String riga;
			while ((riga = reader.readLine()) != null) {
				
				String idUtenteRiga = riga;
				String lineaRiga = reader.readLine();
				String commRiga = reader.readLine();
				
				reader.readLine(); //salta riga vuota
				
				if (lineaRiga == null || commRiga == null) {
					
					break;
				}
				
				try {
					
					String idUtente = idUtenteRiga.substring(idUtenteRiga.indexOf(":")+1).trim();
					String routeId = lineaRiga.substring(lineaRiga.indexOf(":")+1).trim();
					String commento = commRiga.substring(commRiga.indexOf(":")+1).trim();
					
					Optional<Linea> optLinea = getLineaById(routeId);
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
	
	private Optional<Linea> getLineaById(String routeId) {
		
		for (Linea l : parser.getLinee()) {
			
			if (l.getRouteId().equals(routeId)) {
				
				return Optional.of(l);
			}
		}
		
		return Optional.empty();
	}
	
	public synchronized boolean addFavoriteRoute(String userId, Linea linea, String commento) throws IOException {
		
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
			
			if (f.getLineaSalvata().getRouteId().equals(linea.getRouteId())) {
				
				throw new FavoriteAlreadyExistingException("Hai già aggiunto questa linea ai tuoi Preferiti");
			}
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDBRoutes, true))) {
			
			writer.write("ID Utente: "+userId);
			writer.newLine();
			writer.write("Linea: "+linea.getRouteId());
			writer.newLine();
			writer.write("Commento: "+commento);
			writer.newLine();
			writer.newLine();
			
		}
		
		list.add(new FavoriteRoute(userId, linea, commento));
		System.out.println("Linea aggiunta ai Preferiti.");
		
		return true;
	}
	
	public boolean isFavoriteRoutePresent(String userId, String routeId) {
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		for (FavoriteRoute f : list) {
			
			if (f.getLineaSalvata().getRouteId().equals(routeId)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	//trova tutti i Favorites di un determinato utente
	public synchronized Optional<List<FavoriteRoute>> findFavoriteRouteByUserId(String userId) {
		
		if (favoriteRoutesByUserId.containsKey(userId)) {
			
			return Optional.of(List.copyOf(favoriteRoutesByUserId.get(userId)));
		}
		
		return Optional.empty();
	}
	
	//trova un particolare FavoriteRoute di un determinato utente
	public synchronized Optional<FavoriteRoute> findFavoriteRouteByUserIdAndRouteId(String userId, String routeId) {
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		if (list != null) {
			
			for (FavoriteRoute f : list) {
				
				if (f.getLineaSalvata().getRouteId().equals(routeId)) {
					
					return Optional.of(f);
				}
			}
		}
		
		return Optional.empty();
		
	}
	
	private void rewriteFile() throws IOException {

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDBRoutes, false))) {

	        for (Map.Entry<String, List<FavoriteRoute>> entry : favoriteRoutesByUserId.entrySet()) {

	            for (FavoriteRoute f : entry.getValue()) {

	                writer.write("ID Utente: " + f.getUserId());
	                writer.newLine();
	                writer.write("Linea: " + f.getLineaSalvata().getRouteId());
	                writer.newLine();
	                writer.write("Commento: " + f.getCommento());
	                writer.newLine();
	                writer.newLine();
	            }
	        }
	    }
	}
	
	public synchronized Optional<FavoriteRoute> deleteFavoriteRoute(String userId, String routeId) throws IOException {
		
		List<FavoriteRoute> list = favoriteRoutesByUserId.get(userId);
		
		if (list == null) return Optional.empty();
		
		Optional<FavoriteRoute> toDelete = findFavoriteRouteByUserIdAndRouteId(userId, routeId);
		
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


