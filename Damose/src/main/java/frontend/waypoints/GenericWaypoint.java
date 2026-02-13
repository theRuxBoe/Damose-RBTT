package main.java.frontend.waypoints;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;


/**
 * This class represents a generic way-point.
 * It is used as a concrete implementation of {@link Waypoint}.
 */
public class GenericWaypoint implements Waypoint {

	/** Position. */
	private GeoPosition position;
	
	/**
	 * Instantiates a new generic way-point from a {@link GeoPosition}.
	 *
	 * @param pos the position
	 */
	public GenericWaypoint(GeoPosition pos) {
		this.position = pos;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	@Override
	public GeoPosition getPosition() {
		return position;
	}

	
}
