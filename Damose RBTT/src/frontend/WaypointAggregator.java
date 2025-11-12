package frontend;

import java.util.*;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

public class WaypointAggregator {
//	the idea is to get Bus information from back-end for every single bus and to turn them into a list
//	of geoPosition elements to be displayed by the WaypointRenderer class
//	maybe it should be better with a static class to convert all buses??
	
	private List<BusWaypoint> waypoints;
	
	
//	TODO when connected to back-end we will need to know the type of object bus
//	mainly a placeholder
	
	public WaypointAggregator(List<Bus> buses) {
		for (Bus b : buses) {
			waypoints.add(new BusWaypoint(b));
			
		}
	}

//	private static BusWaypoint waypointConverter(Bus bus) {
//		
//	}
	
	
//	@Override
//	public GeoPosition getPosition() {
//		return position;
//	}
//	
	
	
}
