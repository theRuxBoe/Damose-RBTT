package main.java.frontend.waypoints;

import org.jxmapviewer.viewer.GeoPosition;

/**
 * The Class VehicleWaypoint represents the 
 * way-point for a generic vehicle.
 */
public class VehicleWaypoint extends GenericWaypoint {

	/**
	 * Instantiates a new vehicle way-point.
	 *
	 * @param pos the position
	 */
	public VehicleWaypoint(GeoPosition pos) {
		super(pos);
	}

	/**
	 * Instantiates a new vehicle way-point.
	 *
	 * @param lat the latitude
	 * @param lon the longitude
	 */
	public VehicleWaypoint(double lat, double lon) {
		this(new GeoPosition(lat, lon));
	}
}
