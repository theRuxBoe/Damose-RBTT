package backend.model;

import java.time.LocalTime;
import java.util.Objects;

public class OrarioFermata {
	
	private String tripId, stopId, arrivalTime, departureTime;
	
	public OrarioFermata(String tId, String sId, String at, String dt) {
		
		this.tripId = tId;
		this.stopId = sId;
		this.arrivalTime = at;
		this.departureTime = dt;
		
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	
	public LocalTime getArrivalAsTime() {
		
		return LocalTime.parse(arrivalTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(arrivalTime, departureTime, stopId, tripId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrarioFermata other = (OrarioFermata) obj;
		return Objects.equals(arrivalTime, other.arrivalTime) && Objects.equals(departureTime, other.departureTime)
				&& Objects.equals(stopId, other.stopId) && Objects.equals(tripId, other.tripId);
	}

}
