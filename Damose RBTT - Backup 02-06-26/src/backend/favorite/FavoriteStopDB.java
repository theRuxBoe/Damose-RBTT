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

import backend.model.Fermata;
import backend.parser.GTFSStaticParser;
import backend.parser.GTFSStaticRepository;

public class FavoriteStopDB {

	private final File favoritesFileDBStops;
	private final Map<String, List<FavoriteStop>> favoriteStopsByUserId;

	public FavoriteStopDB() throws IOException {
		
        GTFSStaticRepository.initIfNeeded("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
		favoritesFileDBStops = new File("FavoritesFileDBStops.txt");
		
		if (!favoritesFileDBStops.exists()) {
			
			favoritesFileDBStops.createNewFile();
		}
		
		favoriteStopsByUserId = new HashMap<String, List<FavoriteStop>>();
		loadFavoritesFromFile();
	}
	
	private void loadFavoritesFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(favoritesFileDBStops))) {
			
			String riga;
			while ((riga = reader.readLine()) != null) {
				
				String idUtenteRiga = riga;
				String fermataRiga = reader.readLine();
				String commRiga = reader.readLine();
				
				reader.readLine(); //salta riga vuota
				
				if (fermataRiga == null || commRiga == null) {
					
					break;
				}
				
				try {
					
					String idUtente = idUtenteRiga.substring(idUtenteRiga.indexOf(":")+1).trim();
					String fermataId = fermataRiga.substring(fermataRiga.indexOf(":")+1).trim();
					String commento = commRiga.substring(commRiga.indexOf(":")+1).trim();
					
					Optional<Fermata> optFermata = getFermataById(fermataId);
					if (optFermata.isPresent()) {
						
						favoriteStopsByUserId.computeIfAbsent(idUtente, k -> new ArrayList<>())
                        .add(new FavoriteStop(idUtente, optFermata.get(), commento));
					}
				}
				
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	private Optional<Fermata> getFermataById(String stopId) {
		
		for (Fermata f : GTFSStaticRepository.getFermate()) {
			
			if (f.getStopId().equals(stopId)) {
				
				return Optional.of(f);
			}
		}
		
		return Optional.empty();
	}
	
	public synchronized boolean addFavoriteStop(String userId, Fermata fermata, String commento) throws IOException {
		
		if (userId == null || fermata == null) {
			
			throw new IllegalArgumentException("Testo nullo non consentito.");
			
		}
		
		if (commento == null) commento = "";
		commento = commento.replace("\r", " ").replace("\n", " ").trim();
		
		List<FavoriteStop> list = favoriteStopsByUserId.get(userId);
		
		if (list == null) {
			
			list = new ArrayList<FavoriteStop>();
			favoriteStopsByUserId.put(userId, list);
		}
		
		for (FavoriteStop f : list) {
			
			if (f.getFermataSalvata().getStopId().equals(fermata.getStopId())) {
				
				throw new FavoriteAlreadyExistingException("Hai già aggiunto questa fermata ai tuoi Preferiti");
			}
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDBStops, true))) {
			
			writer.write("ID Utente: "+userId);
			writer.newLine();
			writer.write("Fermata: "+fermata.getStopId());
			writer.newLine();
			writer.write("Commento: "+commento);
			writer.newLine();
			writer.newLine();
			
		}
		
		list.add(new FavoriteStop(userId, fermata, commento));
		System.out.println("Fermata aggiunta ai Preferiti.");
		
		return true;
	}
	
	public boolean isFavoriteStopPresent(String userId, String stopId) {
		
		List<FavoriteStop> list = favoriteStopsByUserId.get(userId);
		
		for (FavoriteStop f : list) {
			
			if (f.getFermataSalvata().getStopId().equals(stopId)) {
				
				return true;
			}
			
		}
		
		return false;
	}
	
	//trova tutti i FavoriteStops di un determinato utente
	public synchronized Optional<List<FavoriteStop>> findFavoritesByUserId(String userId) {
		
		if (favoriteStopsByUserId.containsKey(userId)) {
			
			return Optional.of(List.copyOf(favoriteStopsByUserId.get(userId)));
		}
		
		return Optional.empty();
	}
	
	//trova un particolare FavoriteStop di un determinato utente
	public synchronized Optional<FavoriteStop> findFavoriteStopByUserIdAndStopId(String userId, String stopId) {
		
		List<FavoriteStop> list = favoriteStopsByUserId.get(userId);
		
		if (list != null) {
			
			for (FavoriteStop f : list) {
				
				if (f.getFermataSalvata().getStopId().equals(stopId)) {
					
					return Optional.of(f);
				}
			}
		}
		
		return Optional.empty();
		
	}
	
	private void rewriteFile() throws IOException {

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDBStops, false))) {

	        for (Map.Entry<String, List<FavoriteStop>> entry : favoriteStopsByUserId.entrySet()) {

	            for (FavoriteStop f : entry.getValue()) {

	                writer.write("ID Utente: " + f.getUserId());
	                writer.newLine();
	                writer.write("Fermata: " + f.getFermataSalvata().getStopId());
	                writer.newLine();
	                writer.write("Commento: " + f.getCommento());
	                writer.newLine();
	                writer.newLine();
	            }
	        }
	    }
	}
	
	public synchronized Optional<FavoriteStop> deleteFavoriteStop(String userId, String stopId) throws IOException {
		
		List<FavoriteStop> list = favoriteStopsByUserId.get(userId);
		
		if (list == null) return Optional.empty();
		
		Optional<FavoriteStop> toDelete = findFavoriteStopByUserIdAndStopId(userId, stopId);
		
		if (toDelete.isEmpty()) {
			
			return Optional.empty();
		}
			
		FavoriteStop deleted = toDelete.get();
		list.remove(deleted);
		rewriteFile();
		
	    if (list.isEmpty()) {
	        favoriteStopsByUserId.remove(userId);
	    }
		
		return toDelete;
		
	}
}