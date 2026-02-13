package main.java.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import main.java.backend.model.*;
import main.java.backend.realtime.ServiceAlertInfo;
import main.java.backend.realtime.VehiclePositionInfo;

public interface TransitService {
	
	List<RisultatoLinea> trovaLineePerIdFermata(String stopId);
	List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata);
	Optional<Fermata> getFermataById(String stopId);
	List<Linea> cercaLinee(String query);
	List<Fermata> cercaFermate(String query);
	List<Fermata> trovaFermatePerLinea(String routeId, String directionName);
	Map<String, RouteMetricsDB.InfoLinea> ottieniStatisticheServizio();
	List<PredizioneArrivo> prediciArriviPerFermata(String stopId, int limit);
	Optional<PredizioneArrivo> ottieniProssimoArrivoLineaAllaFermata(String stopId, String routeId, String directionName);
	Optional<VehiclePositionInfo> getVehiclePositionForTripId(String tripId);
	List<ServiceAlertInfo> getAlertsForStopId(String stopId);
	List<ServiceAlertInfo> getAlertsForRouteId(String routeId);
	List<ServiceAlertInfo> getAllAlerts();

}
