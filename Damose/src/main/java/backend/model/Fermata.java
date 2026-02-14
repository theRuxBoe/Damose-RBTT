package main.java.backend.model;

import java.util.Objects;

/**
 * The Class Fermata -> identifies a certain stop
 */
public class Fermata extends DatoGTF {
	
	/** The stop name and the stop id */
	private String stopId, name;
	
	/** The stop coordinates */
	private double lat, lon;
	
	/**
	 * Instantiates a new stop.
	 *
	 * @param sId the stop id
	 * @param n the name
	 * @param lat the lat
	 * @param lon the lon
	 */
	public Fermata(String sId, String n, double lat, double lon) {
		
		this.stopId = sId;
		this.name = n;
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Gets the stop id.
	 *
	 * @return the stop id
	 */
	public String getStopId() {
		return stopId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(lat, lon, name, stopId);
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
		Fermata other = (Fermata) obj;
		return Double.doubleToLongBits(lat) == Double.doubleToLongBits(other.lat)
				&& Double.doubleToLongBits(lon) == Double.doubleToLongBits(other.lon)
				&& Objects.equals(name, other.name) && Objects.equals(stopId, other.stopId);
	}
	
	/**
	 * To string.
	 *
	 * @return the stop name + the stop id in String format
	 */
	@Override
	public String toString() {
		
		return this.name + " (" + this.stopId + ") ";
	}

}
