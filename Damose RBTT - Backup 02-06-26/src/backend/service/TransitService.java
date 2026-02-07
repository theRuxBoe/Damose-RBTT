package backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import backend.model.*;
import backend.realtime.ServiceAlertInfo;
import backend.realtime.VehiclePositionInfo;

public interface TransitService {
	
	List<RisultatoLinea> trovaLineePerIdFermata(String stopId);
	List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata);
	Optional<Fermata> getFermataById(String stopId);
	List<Linea> cercaLinee(String query);
	List<Fermata> cercaFermate(String query);
	List<RisultatoFermata> trovaFermatePerLinea(String routeId, String directionName);
    //ServiceQualityMetrics getQualityMetrics(String routeId);
	List<PredizioneArrivo> prediciArriviPerFermata(String stopId, int limit);
	Optional<PredizioneArrivo> ottieniProssimoArrivoLineaAllaFermata(String stopId, String routeId, String directionName);
	Optional<VehiclePositionInfo> getVehiclePositionForTripId(String tripId);
	List<ServiceAlertInfo> getAlertsForStopId(String stopId);
	List<ServiceAlertInfo> getAlertsForRouteId(String routeId);
	List<ServiceAlertInfo> getAllAlerts();

}
