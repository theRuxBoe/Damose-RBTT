package backend.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import backend.favorite.FavoriteStop;
import backend.model.Fermata;
import backend.model.Linea;
import backend.parser.GTFSStaticRepository;

/**
 * The Class RouteMetricsDB -> represents a database (text file) containing information on the quality of service of each route recorded in the static data.
 */
public class RouteMetricsDB {
	
	/** The Route metrics DB file. */
	private final File RouteMetricsDB;
	
	/** The Route metrics indexed by route id map. */
	private final Map<String, InfoLinea> RouteMetricsByRouteId;
	
	/** The boolean dirty, which indicates if the file needs to be uploaded. */
	private boolean dirty;
	
	/**
	 * Instantiates a new route metrics DB.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public RouteMetricsDB() throws IOException {
		
		GTFSStaticRepository.initIfNeeded("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
		RouteMetricsDB = new File("RouteMetricsDB.txt");
		
		if (!RouteMetricsDB.exists()) {
			
			RouteMetricsDB.createNewFile();
			initialize();
		}
		
		RouteMetricsByRouteId = new HashMap<String, InfoLinea>();
		loadMetricsFromFile();
		
		dirty = false;
	}
	
	/**
	 * Initializes the file if it has never been created before.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void initialize() throws IOException {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(RouteMetricsDB))) {
			
			for (Linea l : GTFSStaticRepository.getLinee()) {
				
				writer.write(l.getRouteId()+" - "+l.getDescription().trim()+": 0");
				writer.newLine();
				
			}
		}
	}
	
	/**
	 * Loads the saved routes service quality infos from the database file to fill the RouteMetricsByRouteId map when an instance of RouteMetricsDB is created.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadMetricsFromFile() throws IOException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(RouteMetricsDB))) {
			
			String riga;
			
			while ((riga = reader.readLine()) != null) {
				
				String rigaLineaPunteggio = riga;
				
				try {
					
					String routeId = riga.substring(0, riga.indexOf(" - ")).trim();
					Integer score = Integer.parseInt(riga.substring(riga.indexOf(": ")+1).trim());
					
					Optional<Linea> optLinea = getLineaById(routeId);
					if (optLinea.isPresent()) {
						
						RouteMetricsByRouteId.put(routeId, new InfoLinea(optLinea.get(), score));
					}
				}
				
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Gets the route by its id.
	 *
	 * @param routeId the route id
	 * @return the linea by id
	 */
	private Optional<Linea> getLineaById(String routeId) {
		
		for (Linea l : GTFSStaticRepository.getLinee()) {
			
			if (l.getRouteId().equals(routeId)) {
				
				return Optional.of(l);
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Updates a route's service quality score.
	 *
	 * @param routeId the route id
	 * @param amount the amount
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void updateRouteScore(String routeId, int amount) throws IOException {
			
			InfoLinea il = RouteMetricsByRouteId.get(routeId);
			
			if (il != null) {
				
				il.setScore(il.getScore()+amount);
				
				dirty = true;
			}
	}
	
	/**
	 * It updates the DB by rewriting it if the boolean dirty is true.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void saveRouteMetricsDBIfDirty() throws IOException {
		
		if (dirty == true) {
			
			rewriteFile();
			dirty = false;
		}
		
	}
	
	/**
	 * Rewrites the file with the updated routes service quality scores.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void rewriteFile() throws IOException {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(RouteMetricsDB, false))) {
			
			for (Map.Entry<String, InfoLinea> entry : RouteMetricsByRouteId.entrySet()) {

	            InfoLinea il = entry.getValue();

	            writer.write(entry.getKey() + " - "
	                    + il.getLinea().getDescription().trim()
	                    + ": " + il.getScore());

	            writer.newLine();
	        }
		}
	}
	
	/**
	 * Gets a copy of a map with all route metrics.
	 *
	 * @return the all route metrics
	 */
	public Map<String, InfoLinea> getAllRouteMetrics() {
		
		return Map.copyOf(this.RouteMetricsByRouteId);
	}
	
	/**
	 * Gets the route score.
	 *
	 * @param routeId the route id
	 * @return the route score
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws NoSuchElementException the no such element exception
	 */
	public Integer getRouteScore(String routeId) throws IllegalArgumentException, NoSuchElementException {
		
		if (routeId == null || routeId.isBlank()) {
			
			throw new IllegalArgumentException("Input invalido.");
		}
		
		InfoLinea il = RouteMetricsByRouteId.get(routeId);
		
		if (il == null) {
			
			throw new NoSuchElementException("RouteId non trovato: " + routeId);
		}
		
		return il.getScore();
	
	}
	
	/**
	 * Gets the rating of a route.
	 *
	 * @param routeId the route id
	 * @return the rating
	 */
	public RatingRoute getRating(String routeId) {
		
		Integer score = getRouteScore(routeId);
		return RatingRoute.fromScore(score);
	}
	
	/**
	 * The inner Class InfoLinea -> a wrapper object with a Linea object and an integer indicating the route's service quality score.
	 */
	public static class InfoLinea {
		
		/** The route. */
		private Linea linea;
		
		/** The score. */
		private Integer score;
		
		/**
		 * Instantiates a new info linea.
		 *
		 * @param l the l
		 * @param score the score
		 */
		public InfoLinea(Linea l, Integer score) {
			
			this.linea = l;
			this.score = score;
			
		}
		
		/**
		 * Gets the route.
		 *
		 * @return the route
		 */
		public Linea getLinea() {
			
			return this.linea;
		}
		
		/**
		 * Gets the score.
		 *
		 * @return the score
		 */
		public Integer getScore() {
			
			return this.score;
		}
		
		/**
		 * Sets the score.
		 *
		 * @param s the new score
		 */
		private void setScore(Integer s) {
			
			this.score = s;
		}
	}

}
