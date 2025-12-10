package backend.model;

import java.util.Objects;

public class Fermata extends DatoGTF {
	
	private String stopId, name;
	private double lat, lon;
	
	public Fermata(String sId, String n, double lat, double lon) {
		
		this.stopId = sId;
		this.name = n;
		this.lat = lat;
		this.lon = lon;
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

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lat, lon, name, stopId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fermata other = (Fermata) obj;
		return Double.doubleToLongBits(lat) == Double.doubleToLongBits(other.lat)
				&& Double.doubleToLongBits(lon) == Double.doubleToLongBits(other.lon)
				&& Objects.equals(name, other.name) && Objects.equals(stopId, other.stopId);
	}
	
	@Override
	public String toString() {
		
		return this.name + " (" + this.stopId + ") ";
	}

}
