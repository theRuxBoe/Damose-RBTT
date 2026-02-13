package main.java.backend.realtime;

import java.util.List;
import java.util.Objects;

public class ServiceAlertInfo {
	
	private final String alertId;
	private final String header;
	private final String description;
	private final Long startTime;
	private final Long endTime;
	
	private final List<String> routeIds;
	private final List<String> tripIds;
	private final List<String> stopIds;
	
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

	public String getAlertId() {
		return alertId;
	}

	public String getHeader() {
		return header;
	}

	public String getDescription() {
		return description;
	}

	public Long getStartTime() {
		return startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public List<String> getRouteIds() {
		return routeIds;
	}

	public List<String> getTripIds() {
		return tripIds;
	}

	public List<String> getStopIds() {
		return stopIds;
	}
	
	public boolean isGlobal() {
        return routeIds.isEmpty() && tripIds.isEmpty() && stopIds.isEmpty();
    }
	
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
