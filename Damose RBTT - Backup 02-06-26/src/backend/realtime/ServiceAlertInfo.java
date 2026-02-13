package backend.realtime;

import java.util.List;
import java.util.Objects;

/**
 * The Class ServiceAlertInfo -> the class that represents a real time service alert.
 */
public class ServiceAlertInfo {
	
	/** The alert id. */
	private final String alertId;
	
	/** The header. */
	private final String header;
	
	/** The description. */
	private final String description;
	
	/** The start time. */
	private final Long startTime;
	
	/** The end time. */
	private final Long endTime;
	
	/** The route ids of the interested routes. */
	private final List<String> routeIds;
	
	/** The trip ids of the interested trips. */
	private final List<String> tripIds;
	
	/** The stop ids of the interested stops. */
	private final List<String> stopIds;
	
	/**
	 * Instantiates a new service alert info.
	 *
	 * @param aId the alert id
	 * @param hd the header
	 * @param ds the description
	 * @param sTime the start time
	 * @param eTime the end time
	 * @param rIds the route ids
	 * @param tIds the trip ids
	 * @param sIds the stop ids
	 */
	public ServiceAlertInfo(String aId, String hd, String ds, Long sTime, Long eTime, List<String> rIds, List<String> tIds, List<String> sIds) {
		
		this.alertId = aId;
		this.header = hd;
		this.description = ds;
		this.startTime = sTime;
		this.endTime = eTime;
		this.routeIds = rIds == null ? List.of() : List.copyOf(rIds);
		this.tripIds = tIds == null ? List.of() : List.copyOf(tIds);
		this.stopIds = sIds == null ? List.of() : List.copyOf(sIds);
		
	}

	/**
	 * Gets the alert id.
	 *
	 * @return the alert id
	 */
	public String getAlertId() {
		return alertId;
	}

	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * Gets the route ids.
	 *
	 * @return the route ids
	 */
	public List<String> getRouteIds() {
		return routeIds;
	}

	/**
	 * Gets the trip ids.
	 *
	 * @return the trip ids
	 */
	public List<String> getTripIds() {
		return tripIds;
	}

	/**
	 * Gets the stop ids.
	 *
	 * @return the stop ids
	 */
	public List<String> getStopIds() {
		return stopIds;
	}
	
	/**
	 * Checks if the alert is global (it is not related to stops, trips or routes).
	 *
	 * @return true, if is global
	 */
	public boolean isGlobal() {
        return routeIds.isEmpty() && tripIds.isEmpty() && stopIds.isEmpty();
    }
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
    public String toString() {
        return "ServiceAlertInfo{" +
                "id='" + alertId + '\'' +
                ", header='" + header + '\'' +
                ", routes=" + routeIds +
                ", trips=" + tripIds +
                ", stops=" + stopIds +
                ", start=" + startTime +
                ", end=" + endTime +
                '}';
    }
	
}
