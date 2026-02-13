package main.java.frontend.waypoints;

import org.jxmapviewer.viewer.GeoPosition;

public class VehicleWaypoint extends GenericWaypoint {

	public VehicleWaypoint(GeoPosition pos) {
		super(pos);
	}

	public VehicleWaypoint(double lat, double lon) {
		this(new GeoPosition(lat, lon));
	}
}
