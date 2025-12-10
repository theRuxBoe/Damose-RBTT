package backend.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import backend.model.*;
import backend.parser.*;

public class TransitServiceImpl implements TransitService {
	
	private final GTFSStaticParser parser;
	private List<Linea> linee;
	private List<Corsa> corse;
	private List<OrarioFermata> orari;
	private List<Fermata> fermate;
	
	
	public TransitServiceImpl(GTFSStaticParser parser) {
		
		this.parser = parser;
		this.linee = parser.getLinee() != null ? parser.getLinee() : List.of();
	    this.corse = parser.getCorse() != null ? parser.getCorse() : List.of();
	    this.orari = parser.getOrari() != null ? parser.getOrari() : List.of();
	    this.fermate = parser.getFermate() != null ? parser.getFermate() : List.of();
	}
	
	@Override
	public List<RisultatoLinea> trovaLineePerIdFermata(String stopId) {
		
		Map<String, Corsa> corseByTripId = new HashMap<String, Corsa>();
		Map<NomeDirLinea, List<String>> lineeConOrari = new HashMap<NomeDirLinea, List<String>>();
		
		Optional<Fermata> fermataOpt = getFermataById(stopId);
		if (fermataOpt.isEmpty()) return List.of();
		
		List<Fermata> fermateConStessoNome = cercaFermate(fermataOpt.get().getName());
		List<String> stopsId = new ArrayList<String>();
		double DistanzaMaxKm = 0.2;
		
		for (Fermata f : fermateConStessoNome) {
			
			if (calcolaDistanzaKm(fermataOpt.get().getLat(), fermataOpt.get().getLon(), f.getLat(), f.getLon()) <= DistanzaMaxKm) {
				stopsId.add(f.getStopId());
			}

		}
		
		if (stopsId.isEmpty()) stopsId.add(stopId);
		
		for (Corsa c : corse) {
			
			corseByTripId.put(c.getTripId(), c);
		}
		
		List<RisultatoLinea> risultato = new ArrayList<RisultatoLinea>();
		
		for (OrarioFermata o : orari) {
            if (stopsId.contains(o.getStopId())) {
                Corsa c = corseByTripId.get(o.getTripId());
                if (c != null) {
                    NomeDirLinea key = new NomeDirLinea(c.getRouteId(), c.getDirectionName());
                    
                    if (!lineeConOrari.containsKey(key)) {
                    	
                    	lineeConOrari.put(key, new ArrayList<String>());
                    }
                    
                    lineeConOrari.get(key).add(o.getArrivalTime());
                }
            }
        }
		
	    for (List<String> orariList : lineeConOrari.values()) {
	        orariList.sort(String::compareTo);
	    }
		
		for (Map.Entry<NomeDirLinea, List<String>> entry : lineeConOrari.entrySet()) {
			
			risultato.add(new RisultatoLinea(entry.getKey().getRouteId(), entry.getKey().getDirectionName(), entry.getValue()));
		}
        
		return risultato;
	}
	
	@Override
	public List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata) {
		
		Map<String, Corsa> corseByTripId = new HashMap<String, Corsa>();
		Map<NomeDirLinea, List<String>> lineeConOrari = new HashMap<NomeDirLinea, List<String>>();
		
		List<Fermata> fermateConStessoNome = cercaFermate(nomeFermata);
		Fermata fermataRiferimento = fermateConStessoNome.get(0);
		List<String> stopsId = new ArrayList<String>();
		double DistanzaMaxKm = 0.2;
		
		for (Fermata f : fermateConStessoNome) {
			
			if (calcolaDistanzaKm(fermataRiferimento.getLat(), fermataRiferimento.getLon(), f.getLat(), f.getLon()) <= DistanzaMaxKm) {
				stopsId.add(f.getStopId());
			}

		}
		
		List<RisultatoLinea> risultato = new ArrayList<RisultatoLinea>();
		
		if (stopsId.isEmpty()) return risultato; //non esiste alcuna fermata con il nome digitato in input
		
		for (Corsa c : corse) {
			
			corseByTripId.put(c.getTripId(), c);
		}
		

		for (OrarioFermata o : orari) {
            if (stopsId.contains(o.getStopId())) {
                Corsa c = corseByTripId.get(o.getTripId());
                if (c != null) {
                    NomeDirLinea key = new NomeDirLinea(c.getRouteId(), c.getDirectionName());
                    
                    if (!lineeConOrari.containsKey(key)) {
                    	
                    	lineeConOrari.put(key, new ArrayList<String>());
                    }
                    
                    lineeConOrari.get(key).add(o.getArrivalTime());
                }
            }
        }
		
	    for (List<String> orariList : lineeConOrari.values()) {
	        orariList.sort(String::compareTo);
	    }
		
		for (Map.Entry<NomeDirLinea, List<String>> entry : lineeConOrari.entrySet()) {
			
			risultato.add(new RisultatoLinea(entry.getKey().getRouteId(), entry.getKey().getDirectionName(), entry.getValue()));
		}
        
		return risultato;
	}
	
	@Override
	public Optional<Fermata> getFermataById(String stopId) {
		
		for (Fermata f : fermate) {
			
			if (f.getStopId().equals(stopId)) {
				
				return Optional.of(f);
			}
		}
		
		return Optional.empty();
	}
	
	@Override
	public 	List<Linea> cercaLinee(String query) {
		
		String q = query.toLowerCase();
		List<Linea> risultato = new ArrayList<Linea>();
		
		for (Linea l : linee) {
			
			if (l.getRouteId().toLowerCase().contains(q) || l.getName().toLowerCase().contains(q) || l.getDescription().toLowerCase().contains(q)) {
				
				risultato.add(l);
			}
		}
		
		return risultato;
		
	}
	
	@Override
	public List<Fermata> cercaFermate(String query) {
		
		String q = query.toLowerCase();
		List<Fermata> risultato = new ArrayList<Fermata>();
		
		for (Fermata f : fermate) {
			
			if (f.getStopId().toLowerCase().equals(q) || f.getName().toLowerCase().contains(q)) {
				
				risultato.add(f);
			}
		}
		
		return risultato;
	}
	
	
	private static double calcolaDistanzaKm(double lat1, double lon1, double lat2, double lon2) {
	    final int R = 6371; // raggio medio della Terra in km

	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);

	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLon / 2) * Math.sin(dLon / 2);

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return R * c;
	}
	
	public List<RisultatoFermata> trovaFermatePerLinea(String routeId, String directionName) {
		
		List<String> tripsId = new ArrayList<String>();
		List<RisultatoFermata> risultato = new ArrayList<RisultatoFermata>();
		Map<String, Fermata> fermateByStopId = new HashMap<String, Fermata>();
		Map<CodiceNomeFermata, List<String>> fermateConOrari = new LinkedHashMap<>();
	    List<OrarioFermata> orariFiltrati = new ArrayList<>();
		
		for (Corsa c : corse) {
			
			if ((c.getRouteId().equals(routeId)) && (c.getDirectionName().equalsIgnoreCase(directionName))) {
			    tripsId.add(c.getTripId());
			}
		}
		
		for (Fermata f : fermate) {

			fermateByStopId.put(f.getStopId(), f);
		}
		

	    for (OrarioFermata o : orari) {
	        if (tripsId.contains(o.getTripId())) {
	            orariFiltrati.add(o);
	        }
	    }
	    
	    orariFiltrati.sort(Comparator
	            .comparing(OrarioFermata::getTripId)
	            .thenComparingInt(OrarioFermata::getStopSequence));
		
		for (OrarioFermata o : orariFiltrati) {
				
				Fermata f = fermateByStopId.get(o.getStopId());
				if (f != null) {
                    CodiceNomeFermata key = new CodiceNomeFermata(f.getStopId(), f.getName());
                    
                    if (!fermateConOrari.containsKey(key)) {
                    	
                    	fermateConOrari.put(key, new ArrayList<String>());
                    }
                    
                    fermateConOrari.get(key).add(o.getArrivalTime());
                }
		}
		
		for (List<String> orariList : fermateConOrari.values()) {
		        orariList.sort(String::compareTo);
		    }
			
		for (Map.Entry<CodiceNomeFermata, List<String>> entry : fermateConOrari.entrySet()) {
		    List<String> orariUnici = entry.getValue().stream()
		        .distinct()
		        .collect(Collectors.toList());
		    
		    risultato.add(new RisultatoFermata(
		        entry.getKey().getStopId(),
		        entry.getKey().getName(),
		        orariUnici
		    ));

		}
		
	    return risultato;
	}
	
	public List<WrapperGenerico> ricercaGenerica(String in) {
		
		List<WrapperGenerico> risultato = new ArrayList<WrapperGenerico>();
		
		for (Linea l : cercaLinee(in)) {
			
			risultato.add(new WrapperGenerico("Linea", l));
		}
		
		for (Fermata f : cercaFermate(in)) {
			
			risultato.add(new WrapperGenerico("Fermata", f));
		}
		
		if (risultato.size() == 0) {
			System.out.println("La ricerca non ha prodotto alcun risultato.");
		}
		
		return risultato;
	}
	
	public static TransitServiceImpl createDefault() throws IOException {
	        GTFSStaticParser parser = new GTFSStaticParser();
	        parser.parseAll("https://romamobilita.it/sites/default/files/rome_static_gtfs.zip");
	        return new TransitServiceImpl(parser);
	    }
	
    private static class NomeDirLinea {
        private final String routeId;
        private final String directionName;

        NomeDirLinea(String routeId, String directionName) {
            this.routeId = routeId;
            this.directionName = directionName;
        }

        public String getRouteId() { return routeId; }
        public String getDirectionName() { return directionName; }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            NomeDirLinea other = (NomeDirLinea) obj;
            return routeId.equals(other.routeId) && directionName.equals(other.directionName);
        }

        @Override
        public int hashCode() {
            return routeId.hashCode() * 31 + directionName.hashCode();
        }

    }
    
    private static class CodiceNomeFermata {
        private final String stopId;
        private final String name;

        CodiceNomeFermata(String sId, String name) {
            this.stopId = sId;
            this.name = name;
        }

        public String getStopId() { return stopId; }
        public String getName() { return name; }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CodiceNomeFermata other = (CodiceNomeFermata) obj;
            return stopId.equals(other.stopId) && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return stopId.hashCode() * 31 + name.hashCode();
        }

    }
    
    private static class WrapperGenerico {
    	
    	private String type;
    	private DatoGTF item;
    	
    	WrapperGenerico(String t, DatoGTF i) {
    		
    		this.type = t;
    		this.item = i;
    	}

		public String getType() {
			return type;
		}

		public DatoGTF getItem() {
			return item;
		}
    }

}
