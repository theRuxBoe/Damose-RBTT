package main.java.backend.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RisultatoLinea extends Risultato {
	
	private String routeId, directionName;
	
	public RisultatoLinea(String routeId, String directionName) {
		
		this.routeId = routeId;
		this.directionName = directionName;
		
	}
	
	@Override 
	public String toString() {
		
		return "Linea " + routeId + " direzione " + directionName;
	}

	public String getRouteId() {
		return routeId;
	}

	public String getDirectionName() {
		return directionName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(directionName, routeId);
	}

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
