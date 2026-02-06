package backend.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PredizioneArrivo extends DatoGTF {
	
	private String stopId, routeId, tripId, directionName;
	private LocalTime arrivalTime;
	private boolean realTime;
	private int delaySeconds;
	
	//costruttore per arrivi in tempo reale
	public PredizioneArrivo(String sId, String rId, String tId, String dN, LocalTime at, boolean rT, int del) {
		
		this.stopId = sId;
		this.routeId = rId;
		this.arrivalTime = at;
		this.tripId = tId;
		this.realTime = rT;
		this.delaySeconds = del;
		this.directionName = dN;
	}
	
	//costruttore per arrivi con dati statici
	public PredizioneArrivo(String sId, String rId, String tId, String dN, LocalTime at) {
		
		this(sId, rId, tId, dN, at, false, 0);
	}

	public String getStopId() {
		return stopId;
	}

	public String getRouteId() {
		return routeId;
	}

	public String getTripId() {
		return tripId;
	}

	public String getDirectionName() {
		
		return directionName;
	}
	
	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public boolean isRealTime() {
		return realTime;
	}

	public int getDelaySeconds() {
		return delaySeconds;
	}
	
	public int getDelayMinutes() {
		
		return Math.round(delaySeconds/60f);
	}

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
        
	@Override
	public int hashCode() {
		return Objects.hash(arrivalTime, delaySeconds, realTime, routeId, stopId, tripId);
	}

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
