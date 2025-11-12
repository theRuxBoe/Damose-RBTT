package backend.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
	
	public static List<String[]> readCSV(Path path) throws IOException {
		
		List<String[]> righe = new ArrayList<String[]>();
		try (BufferedReader br = Files.newBufferedReader(path)) {
			String riga;
			boolean primaRiga = true;
			
			while((riga = br.readLine()) != null) {
				
				if (primaRiga == true) {
					primaRiga = false;
					continue;
				}
				
				if (riga.isBlank()) continue;
				
				String[] colonne = riga.split(",", -1);
				righe.add(colonne);
			}
		}
		
		return righe;
	}
	
	public static List<String[]> readCSVFromUrl(String fileUrl) throws IOException {
		List<String[]> righe = new ArrayList<>();
		URL url = new URL(fileUrl);
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
			String riga;
			boolean primaRiga = true;
			
			while ((riga = br.readLine()) != null) {
				if (primaRiga) { primaRiga = false; continue; }
				if (riga.isBlank()) continue;
				String[] colonne = riga.split(",", -1);
				righe.add(colonne);
			}
		}
		return righe;
	}
}
