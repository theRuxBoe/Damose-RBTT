package backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import backend.model.*;

public interface TransitService {
	
	List<RisultatoLinea> trovaLineePerIdFermata(String stopId);
	List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata);
	Optional<Fermata> getFermataById(String stopId);
	List<Linea> cercaLinee(String query);
	List<Fermata> cercaFermate(String query);
	List<RisultatoFermata> trovaFermatePerLinea(String routeId, String directionName);
	//List<PredizioneArrivo> prediciArrivoPerFermata(String stopId, LocalDateTime when);
	//void addFavorite(String userId, Favorite fav);
    //List<Favorite> getFavorites(String userId);
    //ServiceQualityMetrics getQualityMetrics(String routeId);
	

}
