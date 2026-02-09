package frontend.waypoints;

import java.util.Optional;


import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import backend.model.Corsa;
import backend.realtime.VehiclePositionInfo;
import frontend.main.MainFrame;

public class BusWaypoint implements Waypoint {

	private GeoPosition position;
	
	public BusWaypoint(GeoPosition pos) {
		this.position = pos;
	}

	@Override
	public GeoPosition getPosition() {
		return position;
	}

	
	
	
	
}
