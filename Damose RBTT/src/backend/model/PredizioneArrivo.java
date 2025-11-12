package backend.model;

import java.time.LocalTime;
import java.util.Objects;

public class PredizioneArrivo extends DatoGTF {
	
	private String stopId, routeId, tripId;
	private LocalTime arrivalTime;
	private boolean realTime;
	private int delay;
	
	//costruttore per arrivi in tempo reale
	public PredizioneArrivo(String sId, String rId, String tId, LocalTime at, boolean rT, int del) {
		
		this.stopId = sId;
		this.routeId = rId;
		this.arrivalTime = at;
		this.tripId = tId;
		this.realTime = rT;
		this.delay = del;
	}
	
	//costruttore per arrivi con dati statici
	public PredizioneArrivo(String sId, String rId, String tId, LocalTime at) {
		
		this(sId, rId, tId, at, false, 0);
	}

	public String getStopId() {
		return stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
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

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public boolean isRealTime() {
		return realTime;
	}

	public void setRealTime(boolean realTime) {
		this.realTime = realTime;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
    @Override
    public String toString() {
        String tipo = realTime ? "tempo reale" : "statico";
        String infoRitardo = (delay != 0) ? (" (" + delay + " min di ritardo)") : "";
        return "Linea " + routeId + " -> fermata " + stopId + " alle " + arrivalTime + " [" + tipo + "]" + infoRitardo;
        
    }

	@Override
	public int hashCode() {
		return Objects.hash(arrivalTime, delay, realTime, routeId, stopId, tripId);
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
		return Objects.equals(arrivalTime, other.arrivalTime) && delay == other.delay && realTime == other.realTime
				&& Objects.equals(routeId, other.routeId) && Objects.equals(stopId, other.stopId)
				&& Objects.equals(tripId, other.tripId);
	}
    
}
