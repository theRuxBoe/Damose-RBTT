package backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import backend.model.*;
import backend.realtime.ServiceAlertInfo;
import backend.realtime.VehiclePositionInfo;

/**
 * The Interface TransitService -> implemented in TransitServiceImpl, the main API for the fronted
 */
public interface TransitService {
	
	/**
	 * Find the routes that pass through that stop by the stop id
	 *
	 * @param stopId the stop id
	 * @return the list
	 */
	List<RisultatoLinea> trovaLineePerIdFermata(String stopId);
	
	/**
	 * Find the routes that pass through that stop by the stop name
	 *
	 * @param nomeFermata the nome fermata
	 * @return the list
	 */
	List<RisultatoLinea> trovaLineePerNomeFermata(String nomeFermata);
	
	/**
	 * Gets the stop by id.
	 *
	 * @param stopId the stop id
	 * @return the stop by id
	 */
	Fermata getFermata(String stopId);
	
	/**
	 * Gets the route by id.
	 *
	 * @param stopId the route id
	 * @return the route by id
	 */
	Linea getLinea(String routeId);
	
	/**
	 * General route search.
	 *
	 * @param query the query
	 * @return the list
	 */
	List<Linea> cercaLinee(String query);
	
	/**
	 * General stop search.
	 *
	 * @param query the query
	 * @return the list
	 */
	List<Fermata> cercaFermate(String query);
	
	/**
	 * Find the path of stops served by a route.
	 *
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the list
	 */
	List<Fermata> trovaFermatePerLinea(String routeId, String directionName);
	
	/**
	 * Gets a map with quality of service statistics for all routes indexed by route id
	 *
	 * @return the map
	 */
	Map<String, RouteMetricsDB.InfoLinea> ottieniStatisticheServizio();
	
	/**
	 * Gets the next real-time or static arrivals of trips for a given stop and in an arbitrary quantity
	 *
	 * @param stopId the stop id
	 * @param limit the limit
	 * @return the list
	 */
	List<PredizioneArrivo> prediciArriviPerFermata(String stopId, int limit);
	
	/**
	 * Gets the next arrival at a given stop on a given route.
	 *
	 * @param stopId the stop id
	 * @param routeId the route id
	 * @param directionName the direction name
	 * @return the optional
	 */
	Optional<PredizioneArrivo> ottieniProssimoArrivoLineaAllaFermata(String stopId, String routeId, String directionName);
	
	/**
	 * Gets the vehicle position for a given trip id.
	 *
	 * @param tripId the trip id
	 * @return the vehicle position for trip id
	 */
	Optional<VehiclePositionInfo> getVehiclePositionForTripId(String tripId);
	
	/**
	 * Gets the alerts for a given stop id.
	 *
	 * @param stopId the stop id
	 * @return the alerts for stop id
	 */
	List<ServiceAlertInfo> getAlertsForStopId(String stopId);
	
	/**
	 * Gets the alerts for a given route id.
	 *
	 * @param routeId the route id
	 * @return the alerts for route id
	 */
	List<ServiceAlertInfo> getAlertsForRouteId(String routeId);
	
	/**
	 * Gets all the alerts.
	 *
	 * @return all the alerts
	 */
	List<ServiceAlertInfo> getAllAlerts();

}
