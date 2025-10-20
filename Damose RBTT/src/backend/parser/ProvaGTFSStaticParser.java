package backend.parser;

import java.util.List;

import backend.model.Corsa;

public class ProvaGTFSStaticParser {
	
	public static void main(String[] args) {
		
		try {
			
			GTFSStaticParser parser = new GTFSStaticParser();
			String url = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip";
			parser.parseAll(url);
			
            System.out.println("Totale fermate: " + parser.getFermate().size());
            System.out.println("Prima fermata: " + parser.getFermate().get(0));
            
            List<Corsa> corse = parser.getCorse();
            
            for (int i = 0; i < corse.size(); i++) {
            	
            	if (corse.get(i).getRouteId().equals("777")) {
            		System.out.println(corse.get(i));
            	}
            }
		}
		
		catch (Exception e) {
            e.printStackTrace();
        }
	}

}
