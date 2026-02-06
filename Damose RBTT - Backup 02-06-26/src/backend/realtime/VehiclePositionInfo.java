package backend.realtime;

public class VehiclePositionInfo {
	
	private final String vehicleId;
    private final String vehicleLabel;
    private final String tripId; //ID della corsa associata al veicolo
    private final double lat; //latitudine
    private final double lon; //longitudine
    private final Double bearing; //direzione di movimento
    private final Double speed; 
    private final Long timestamp;
    private final Integer currentStopSequence; //stopSequence della fermata su cui transita attualmente il bus
    private final String currentStopId; //stopId della fermata su cui transita attualmente il bus
    
    public VehiclePositionInfo(String vId, String vLabel, String tId, double lat, double lon, Double bear, Double sp, Long tstamp, Integer curStopSeq, String curSId) {
    	
    	this.vehicleId = vId;
    	this.vehicleLabel = vLabel;
    	this.tripId = tId;
    	this.lat = lat;
    	this.lon = lon;
    	this.bearing = bear;
    	this.speed = sp;
    	this.timestamp = tstamp;
    	this.currentStopSequence = curStopSeq;
    	this.currentStopId = curSId;
    	
    }

	public String getVehicleId() {
		return vehicleId;
	}

	public String getVehicleLabel() {
		return vehicleLabel;
	}

	public String getTripId() {
		return tripId;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public Double getBearing() {
		return bearing;
	}

	public Double getSpeed() {
		return speed;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Integer getCurrentStopSequence() {
		return currentStopSequence;
	}

	public String getCurrentStopId() {
		return currentStopId;
	}
    
}
