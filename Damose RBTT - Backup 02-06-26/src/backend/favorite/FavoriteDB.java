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

public class FavoriteDB {

	private final File favoritesFileDB;
	private final Map<String, List<Favorite>> favoritesByUserId;
	private final GTFSStaticParser parser;

	public FavoriteDB() throws IOException {
		
		parser = new GTFSStaticParser();
		parser.parseAll("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
		
		favoritesFileDB = new File("FavoritesFileDB.txt");
		
		if (!favoritesFileDB.exists()) {
			
			favoritesFileDB.createNewFile();
		}
		
		favoritesByUserId = new HashMap<String, List<Favorite>>();
		loadFavoritesFromFile();
	}
	
	private void loadFavoritesFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(favoritesFileDB))) {
			
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
						
						favoritesByUserId.computeIfAbsent(idUtente, k -> new ArrayList<>())
                        .add(new Favorite(idUtente, optFermata.get(), commento));
					}
				}
				
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	private Optional<Fermata> getFermataById(String stopId) {
		
		for (Fermata f : parser.getFermate()) {
			
			if (f.getStopId().equals(stopId)) {
				
				return Optional.of(f);
			}
		}
		
		return Optional.empty();
	}
	
	public synchronized boolean addFavorite(String userId, Fermata fermata, String commento) throws IOException, FavoriteAlreadyExistingException {
		
		if (userId == null || fermata == null) {
			
			throw new IllegalArgumentException("Testo nullo non consentito.");
			
		}
		
		if (commento == null) commento = "";
		commento = commento.replace("\r", " ").replace("\n", " ").trim();
		
		List<Favorite> list = favoritesByUserId.get(userId);
		
		if (list == null) {
			
			list = new ArrayList<Favorite>();
			favoritesByUserId.put(userId, list);
		}
		
		for (Favorite f : list) {
			
			if (f.getFermataSalvata().getStopId().equals(fermata.getStopId())) {
				
				throw new FavoriteAlreadyExistingException("Hai già aggiunto questa fermata ai tuoi Preferiti");
			}
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(favoritesFileDB, true))) {
			
			writer.write("ID Utente: "+userId);
			writer.newLine();
			writer.write("Fermata: "+fermata.getStopId());
			writer.newLine();
			writer.write("Commento: "+commento);
			writer.newLine();
			writer.newLine();
			
		}
		
		list.add(new Favorite(userId, fermata, commento));
		System.out.println("Fermata aggiunta ai Preferiti.");
		
		return true;
	}
	
	//trova tutti i Favorites di un determinato utente
	public synchronized Optional<List<Favorite>> findFavoritesByUserId(String userId) {
		
		if (favoritesByUserId.containsKey(userId)) {
			
			return Optional.of(List.copyOf(favoritesByUserId.get(userId)));
		}
		
		return Optional.empty();
	}
	
	//trova un particolare Favorite di un determinato utente
	public synchronized Optional<Favorite> findFavoriteByUserIdAndStopId(String userId, String stopId) {
		
		List<Favorite> list = favoritesByUserId.get(userId);
		
		if (list != null) {
			
			for (Favorite f : list) {
				
				if (f.getFermataSalvata().getStopId().equals(stopId)) {
					
					return Optional.of(f);
				}
			}
		}
		
		return Optional.empty();
		
	}
}
