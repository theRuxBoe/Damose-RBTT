package main.java.backend.realtime;

// TODO: Auto-generated Javadoc
/**
 * The Class VehiclePositionInfo -> represents an object with the real time infos about a vehicle which is currently associated with a trip.
 */
public class VehiclePositionInfo {
	
	/** The vehicle id. */
	private final String vehicleId;
    
    /** The vehicle label. */
    private final String vehicleLabel;
    
    /** The trip id associated with the vehicle. */
    private final String tripId; 
    
    /** The latitude. */
    private final double lat; 
    
    /** The longitude. */
    private final double lon;
    
    /** The vehicle bearing (movement direction) . */
    private final Double bearing;
    
    /** The speed. */
    private final Double speed; 
    
    /** The timestamp. */
    private final Long timestamp;
    
    /** The current stop sequence. */
    private final Integer currentStopSequence; 
    
    /** The current stop id. */
    private final String currentStopId;
    
    /** The occupancy level (how much crowded the vehicle is). */
    private final OccupancyLevel occupancyLevel;
    
    /**
     * Instantiates a new vehicle position info.
     *
     * @param vId the vehicle id
     * @param vLabel the vehicle label
     * @param tId the trip id
     * @param lat the latitude
     * @param lon the longitude
     * @param bear the bearing
     * @param sp the speed
     * @param tstamp the timestamp
     * @param curStopSeq the current stop sequence
     * @param curSId the current stop id
     * @param occLvl the occupancy level
     */
    public VehiclePositionInfo(String vId, String vLabel, String tId, double lat, double lon, Double bear, Double sp, Long tstamp, Integer curStopSeq, String curSId, OccupancyLevel occLvl) {
    	
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
    	this.occupancyLevel = occLvl;
    	
    }

	/**
	 * Gets the vehicle id.
	 *
	 * @return the vehicle id
	 */
	public String getVehicleId() {
		return vehicleId;
	}

	/**
	 * Gets the vehicle label.
	 *
	 * @return the vehicle label
	 */
	public String getVehicleLabel() {
		return vehicleLabel;
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
	 * Gets the latitude.
	 *
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * Gets the bearing.
	 *
	 * @return the bearing
	 */
	public Double getBearing() {
		return bearing;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public Double getSpeed() {
		return speed;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Gets the current stop sequence.
	 *
	 * @return the current stop sequence
	 */
	public Integer getCurrentStopSequence() {
		return currentStopSequence;
	}

	/**
	 * Gets the current stop id.
	 *
	 * @return the current stop id
	 */
	public String getCurrentStopId() {
		return currentStopId;
	}
	
	/**
	 * Gets the occupancy level.
	 *
	 * @return the occupancy level
	 */
	public OccupancyLevel getOccupancyLevel() {
		
		return this.occupancyLevel;
	}
    
}
