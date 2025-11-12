package backend.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RisultatoFermata extends Risultato {
	
	private String stopId, name;
	private List<String> arrivals;
	
	public RisultatoFermata(String stopId, String name, List<String> arrivals) {
		
		this.stopId = stopId;
		this.name = name;
		this.arrivals = arrivals;
	}
	
	@Override 
	public String toString() {
		
		return "Fermata " + name + " (" + stopId + ")" + " -> " + arrivals;
	}
	
	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getArrivals() {
		return arrivals;
	}

	public void setArrivals(List<String> arrivals) {
		this.arrivals = arrivals;
	}

	@Override
	public int hashCode() {
		return Objects.hash(arrivals, name, stopId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RisultatoFermata other = (RisultatoFermata) obj;
		return Objects.equals(arrivals, other.arrivals) && Objects.equals(name, other.name)
				&& Objects.equals(stopId, other.stopId);
	}

	public void sortArrivals() {
	    Collections.sort(arrivals);
	}

}
