package main.java.backend.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Class RisultatoLinea -> a wrapper with a Linea object and an additional field which indentifies the route's direction name.
 */
public class RisultatoLinea extends Risultato {
	
	/** The direction name. */
	private String routeId, directionName;
	
	/**
	 * Instantiates a new risultato linea.
	 *
	 * @param routeId the route id
	 * @param directionName the direction name
	 */
	public RisultatoLinea(String routeId, String directionName) {
		
		this.routeId = routeId;
		this.directionName = directionName;
		
	}
	
	/**
	 * To string.
	 *
	 * @return the string with the route id and its direction name
	 */
	@Override 
	public String toString() {
		
		return "Linea " + routeId + " direzione " + directionName;
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
	 * Gets the direction name.
	 *
	 * @return the direction name
	 */
	public String getDirectionName() {
		return directionName;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(directionName, routeId);
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
		RisultatoLinea other = (RisultatoLinea) obj;
		return Objects.equals(directionName, other.directionName) && Objects.equals(routeId, other.routeId);
	}

}
