package backend.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RisultatoLinea extends Risultato {
	
	private String routeId, directionName;
	private List<String> arrivals;
	
	public RisultatoLinea(String routeId, String directionName, List<String> arrivals) {
		
		this.routeId = routeId;
		this.directionName = directionName;
		this.arrivals = arrivals;
	}
	
	@Override 
	public String toString() {
		
		return "Linea " + routeId + " direzione " + directionName + " -> " + arrivals;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getDirectionName() {
		return directionName;
	}

	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}

	public List<String> getArrivals() {
		return arrivals;
	}

	public void setArrivals(List<String> arrivals) {
		this.arrivals = arrivals;
	}

	@Override
	public int hashCode() {
		return Objects.hash(arrivals, directionName, routeId);
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
		return Objects.equals(arrivals, other.arrivals) && Objects.equals(directionName, other.directionName)
				&& Objects.equals(routeId, other.routeId);
	}
	
	public void sortArrivals() {
	    Collections.sort(arrivals);
	}

}
