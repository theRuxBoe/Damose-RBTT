package main.java.backend.model;

import java.util.Objects;

/**
 * The Class Corsa -> identifies a certain trip
 */
public class Corsa extends DatoGTF {
	
	/** The route id (the route associated to that trip), trip id, service id and direction name. */
	private String routeId, tripId, serviceId, directionName;
	
	/** The direction id. */
	private int directionId;
	
	/**
	 * Instantiates a new corsa.
	 *
	 * @param rId the r id
	 * @param tId the t id
	 * @param svId the sv id
	 * @param dirn the dirn
	 * @param dirId the dir id
	 */
	public Corsa(String rId, String tId, String svId, String dirn, int dirId) {
		
		this.routeId = rId;
		this.tripId = tId;
		this.serviceId = svId;
		this.directionName = dirn;
		this.directionId = dirId;
		
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		
		return "Corsa di linea "+routeId+" direzione "+directionName;
	}

	/**
	 * Gets the route id.
	 *
	 * @return the route id
	 */
	public String getRouteId() {
		return routeId;
	}

	/**
	 * Gets the trip id.
	 *
	 * @return the trip id
	 */
	public String getTripId() {
		return tripId;
	}

	/**
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Gets the direction name.
	 *
	 * @return the direction name
	 */
	public String getDirectionName() {
		return directionName;
	}

	/**
	 * Gets the direction id.
	 *
	 * @return the direction id
	 */
	public int getDirectionId() {
		return directionId;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(directionId, directionName, routeId, serviceId, tripId);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Corsa other = (Corsa) obj;
		return directionId == other.directionId && Objects.equals(directionName, other.directionName)
				&& Objects.equals(routeId, other.routeId) && Objects.equals(serviceId, other.serviceId)
				&& Objects.equals(tripId, other.tripId);
	}

}
