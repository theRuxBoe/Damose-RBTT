package backend.parser;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import backend.model.Corsa;
import backend.model.Fermata;
import backend.model.Linea;
import backend.model.OrarioFermata;

public class GTFSStaticParser {
	
	private List<Fermata> fermate;
	private List<Linea> linee;
	private List<Corsa> corse;
	private List<OrarioFermata> orari;
	
	private Path downloadGTFSZip(String url, Path destinazione) throws IOException {
		
	    if (!Files.exists(destinazione.getParent())) {
	        Files.createDirectories(destinazione.getParent());
	    }
	    try (var in = new URL(url).openStream()) {
	        Files.copy(in, destinazione, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
	    }
	    return destinazione;
	}
	
	private Path unzipGTFS(Path zipPath, Path outputDir) throws IOException {
	    if (!Files.exists(outputDir)) {
	        Files.createDirectories(outputDir);
	    }

	    try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(Files.newInputStream(zipPath))) {
	        java.util.zip.ZipEntry entry;
	        while ((entry = zis.getNextEntry()) != null) {
	            Path filePath = outputDir.resolve(entry.getName());
	            if (!entry.isDirectory()) {
	                try (var out = Files.newOutputStream(filePath)) {
	                    zis.transferTo(out);
	                }
	            } else {
	                Files.createDirectories(filePath);
	            }
	        }
	    }
	    return outputDir;
	}
	
	private List<Fermata> parseFermate(Path file) throws IOException {
		
		List<Fermata> risultatoFermate = new ArrayList<Fermata>();
		List<String[]> righe = CsvUtils.readCSV(file);
		for (String[] riga : righe) {
			
			String stopId = riga[0];
			String name = riga[2].replaceAll("^[\"'‘’“”]+|[\"'‘’“”]+$", "");
			Double lat = Double.parseDouble(riga[4]);
			Double lon = Double.parseDouble(riga[5]);
			
			risultatoFermate.add(new Fermata(stopId, name, lat, lon));
		}
		
		return risultatoFermate;
	}
	
	private List<Linea> parseLinee(Path file) throws IOException {
		
		List<Linea> risultatoLinee = new ArrayList<Linea>();
		List<String[]> righe = CsvUtils.readCSV(file);
		for (String[] riga : righe) {
			
			String routeId = riga[0];
			String name = riga[2].replaceAll("^[\"'‘’“”]+|[\"'‘’“”]+$", "");
			String agencyId = riga[1];
			String description = riga[3].replaceAll("^[\"'‘’“”]+|[\"'‘’“”]+$", "");
			int type = Integer.parseInt(riga[4]);
			
			risultatoLinee.add(new Linea(routeId, name, agencyId, description, type));
		}
		
		return risultatoLinee;
	}
	
	private List<Corsa> parseCorse(Path file) throws IOException {
		
		List<Corsa> risultatoCorse = new ArrayList<Corsa>();
		List<String[]> righe = CsvUtils.readCSV(file);
		for (String[] riga : righe) {
			
			String routeId = riga[0].replaceAll("^[\"'‘’“”]+|[\"'‘’“”]+$", "");
			String tripId = riga[2]; 
			String serviceId = riga[1]; 
			String directionName = riga[3].replaceAll("^[\"'‘’“”]+|[\"'‘’“”]+$", "");
			int directionId = Integer.parseInt(riga[5]);
			
			risultatoCorse.add(new Corsa(routeId, tripId, serviceId, directionName, directionId));
		}
		
		return risultatoCorse;
	}
	
	private List<OrarioFermata> parseOrari(Path file) throws IOException {
		
		List<OrarioFermata> risultatoOrari = new ArrayList<OrarioFermata>();
		List<String[]> righe = CsvUtils.readCSV(file);
		for (String[] riga : righe) {
			
			String tripId = riga[0];
			String stopId = riga[3];
			String arrivalTime = riga[1];
			String departureTime = riga[2];
			int stopSequence = Integer.parseInt(riga[4]);
			
			risultatoOrari.add(new OrarioFermata(tripId, stopId, arrivalTime, departureTime, stopSequence));
		}
		
		return risultatoOrari;
	}
	
	public void parseAll(String gtfsUrl) throws IOException {
		Path baseDir = Path.of(System.getProperty("user.dir"), "data");
		Path zipPath = baseDir.resolve("rome_gtfs.zip");
		Path outputDir = baseDir.resolve("gtfs_extracted");

	    //Controllo aggiornamento: se il file non esiste o è troppo vecchio, lo riscarico
	    boolean deveAggiornare = true;
	    if (Files.exists(zipPath)) {
	        long setteGiorniMillis = 7L * 24 * 60 * 60 * 1000; // 7 giorni in millisecondi
	        long ultimaModifica = Files.getLastModifiedTime(zipPath).toMillis();
	        long adesso = System.currentTimeMillis();

	        if (adesso - ultimaModifica < setteGiorniMillis) {
	            deveAggiornare = false;
	            System.out.println("Il file GTFS è aggiornato (meno di 7 giorni fa). Salto il download.");
	        }
	    }

	    if (deveAggiornare) {
	        System.out.println("Scarico GTFS da " + gtfsUrl);
	        downloadGTFSZip(gtfsUrl, zipPath);
	    }

	    System.out.println("Estrazione in corso...");
	    unzipGTFS(zipPath, outputDir);

	    fermate = parseFermate(outputDir.resolve("stops.txt"));
	    linee = parseLinee(outputDir.resolve("routes.txt"));
	    corse = parseCorse(outputDir.resolve("trips.txt"));
	    orari = parseOrari(outputDir.resolve("stop_times.txt"));

	    System.out.println("Parsing completato: " + fermate.size() + " fermate, "
	                       + linee.size() + " linee, " + corse.size() + " corse.");
	}

	public List<Fermata> getFermate() {
		return fermate;
	}

	public List<Linea> getLinee() {
		return linee;
	}

	public List<Corsa> getCorse() {
		return corse;
	}

	public List<OrarioFermata> getOrari() {
		return orari;
	}
	
}
