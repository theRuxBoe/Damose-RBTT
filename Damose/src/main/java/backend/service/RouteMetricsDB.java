package main.java.backend.service;

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

import main.java.backend.favorite.FavoriteStop;
import main.java.backend.model.Fermata;
import main.java.backend.model.Linea;
import main.java.backend.parser.GTFSStaticRepository;

public class RouteMetricsDB {
	
	private final File RouteMetricsDB;
	private final Map<String, InfoLinea> RouteMetricsByRouteId;
	private boolean dirty;
	
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
	
	private void initialize() throws IOException {
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(RouteMetricsDB))) {
			
			for (Linea l : GTFSStaticRepository.getLinee()) {
				
				writer.write(l.getRouteId()+" - "+l.getDescription().trim()+": 0");
				writer.newLine();
				
			}
		}
	}
	
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
	
	private Optional<Linea> getLineaById(String routeId) {
		
		for (Linea l : GTFSStaticRepository.getLinee()) {
			
			if (l.getRouteId().equals(routeId)) {
				
				return Optional.of(l);
			}
		}
		
		return Optional.empty();
	}
	
	public void updateRouteScore(String routeId, int amount) throws IOException {
			
			InfoLinea il = RouteMetricsByRouteId.get(routeId);
			
			if (il != null) {
				
				il.setScore(il.getScore()+amount);
				
				dirty = true;
			}
	}
	
	public void saveRouteMetricsDBIfDirty() throws IOException {
		
		if (dirty == true) {
			
			rewriteFile();
			dirty = false;
		}
		
	}
	
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
	
	public Map<String, InfoLinea> getAllRouteMetrics() {
		
		return Map.copyOf(this.RouteMetricsByRouteId);
	}
	
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
	
	public static class InfoLinea {
		
		private Linea linea;
		private Integer score;
		
		public InfoLinea(Linea l, Integer score) {
			
			this.linea = l;
			this.score = score;
			
		}
		
		public Linea getLinea() {
			
			return this.linea;
		}
		
		public Integer getScore() {
			
			return this.score;
		}
		
		private void setScore(Integer s) {
			
			this.score = s;
		}
	}

}
