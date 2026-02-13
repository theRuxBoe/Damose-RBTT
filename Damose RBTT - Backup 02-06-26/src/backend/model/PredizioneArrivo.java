package backend.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * The Class PredizioneArrivo -> identifies an object with data (real-time or static) relating to the predicted arrival of a trip at a specific stop
 */
public class PredizioneArrivo extends DatoGTF {
	
	/** The stop id, the route id, the trip id and the direction name. */
	private String stopId, routeId, tripId, directionName;
	
	/** The predicted arrival time. */
	private LocalTime arrivalTime;
	
	/** A boolean which indicates if that arrival is in real time or not. */
	private boolean realTime;
	
	/** The delay in seconds. */
	private int delaySeconds;
	
	/** The full arrival time with also the date of the day. */
	private LocalDateTime orarioCompleto;
	
	/**
	 * Instantiates a new object predizione arrivo.
	 *
	 * @param sId the stop id
	 * @param rId the route id
	 * @param tId the trip id
	 * @param dN the direction name
	 * @param at the arrival time
	 * @param rT the real time boolean
	 * @param del the delay in seconds
	 * @param oc the full arrival time
	 */

	public PredizioneArrivo(String sId, String rId, String tId, String dN, LocalTime at, boolean rT, int del, LocalDateTime oc) {
		
		this.stopId = sId;
		this.routeId = rId;
		this.arrivalTime = at;
		this.tripId = tId;
		this.realTime = rT;
		this.delaySeconds = del;
		this.directionName = dN;
		this.orarioCompleto = oc;
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
	 * Gets the direction name.
	 *
	 * @return the direction name
	 */
	public String getDirectionName() {
		
		return directionName;
	}
	
	/**
	 * Gets the arrival time.
	 *
	 * @return the arrival time
	 */
	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * Checks if is real time.
	 *
	 * @return true, if is real time
	 */
	public boolean isRealTime() {
		return realTime;
	}

	/**
	 * Gets the delay seconds.
	 *
	 * @return the delay seconds
	 */
	public int getDelaySeconds() {
		return delaySeconds;
	}
	
	/**
	 * Gets the delay minutes.
	 *
	 * @return the delay minutes
	 */
	public int getDelayMinutes() {
		
		return Math.round(delaySeconds/60f);
	}
	
	/**
	 * Gets the orario completo.
	 *
	 * @return the orario completo
	 */
	public LocalDateTime getOrarioCompleto() {
		
		return this.orarioCompleto;
	}

	/**
	 * To string.
	 *
	 * @return the string with all the trip's predicted arrival time infos
	 */
	@Override
	public String toString() {
	    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
	    String timeStr = arrivalTime.format(fmt);

	    String delayStr;
	    if (!realTime) {
	        delayStr = "schedulato";
	    } else if (delaySeconds == 0) {
	        delayStr = "in orario";
	    } else if (Math.abs(delaySeconds) < 60) {
	        String sign = delaySeconds > 0 ? "+" : "−";
	        delayStr = sign + Math.abs(delaySeconds) + "s";
	    } else {
	        int minutes = Math.round(delaySeconds / 60f);
	        String sign = minutes > 0 ? "+" : "−";
	        delayStr = sign + Math.abs(minutes) + "m";
	    }

	    return String.format(
	        "Linea %s - %s (%s)",
	        routeId,
	        timeStr,
	        delayStr
	    );
	}
        
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(arrivalTime, delaySeconds, realTime, routeId, stopId, tripId);
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
		PredizioneArrivo other = (PredizioneArrivo) obj;
		return Objects.equals(arrivalTime, other.arrivalTime) && delaySeconds == other.delaySeconds && realTime == other.realTime
				&& Objects.equals(routeId, other.routeId) && Objects.equals(stopId, other.stopId)
				&& Objects.equals(tripId, other.tripId);
	}
    
}
