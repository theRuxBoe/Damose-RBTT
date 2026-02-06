package backend.model;

import java.util.Objects;

public class Corsa extends DatoGTF {
	
	private String routeId, tripId, serviceId, directionName;
	private int directionId;
	
	public Corsa(String rId, String tId, String svId, String dirn, int dirId) {
		
		this.routeId = rId;
		this.tripId = tId;
		this.serviceId = svId;
		this.directionName = dirn;
		this.directionId = dirId;
		
	}
	
	@Override
	public String toString() {
		
		return "Corsa di linea "+routeId+" direzione "+directionName;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getDirectionName() {
		return directionName;
	}

	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}

	public int getDirectionId() {
		return directionId;
	}

	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(directionId, directionName, routeId, serviceId, tripId);
	}

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
