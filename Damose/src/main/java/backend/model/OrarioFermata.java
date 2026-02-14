package main.java.backend.model;

import java.time.LocalTime;
import java.util.Objects;

/**
 * The Class OrarioFermata -> identifies a certain stop time.
 */
public class OrarioFermata extends DatoGTF {
	
	/** The trip id, the stop id, the arrival time and the departure time. */
	private String tripId, stopId, arrivalTime, departureTime;
	
	/** The stop sequence. */
	private int stopSequence;
	
	/**
	 * Instantiates a new stop time.
	 *
	 * @param tId the t id
	 * @param sId the s id
	 * @param at the at
	 * @param dt the dt
	 * @param sSq the s sq
	 */
	public OrarioFermata(String tId, String sId, String at, String dt, int sSq) {
		
		this.tripId = tId;
		this.stopId = sId;
		this.arrivalTime = at;
		this.departureTime = dt;
		this.stopSequence = sSq;
		
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
	 * Gets the stop id.
	 *
	 * @return the stop id
	 */
	public String getStopId() {
		return stopId;
	}

	/**
	 * Gets the arrival time.
	 *
	 * @return the arrival time
	 */
	public String getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * Gets the departure time.
	 *
	 * @return the departure time
	 */
	public String getDepartureTime() {
		return departureTime;
	}
	
	/**
	 * Gets the arrival as time.
	 *
	 * @return the arrival as time
	 */
	public LocalTime getArrivalAsTime() {
		
		return LocalTime.parse(arrivalTime);
	}

	/**
	 * Gets the stop sequence.
	 *
	 * @return the stop sequence
	 */
	public int getStopSequence() {
		return stopSequence;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(arrivalTime, departureTime, stopId, tripId);
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
		OrarioFermata other = (OrarioFermata) obj;
		return Objects.equals(arrivalTime, other.arrivalTime) && Objects.equals(departureTime, other.departureTime)
				&& Objects.equals(stopId, other.stopId) && Objects.equals(tripId, other.tripId);
	}

}
