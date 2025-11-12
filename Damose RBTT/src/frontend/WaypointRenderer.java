package frontend;

import java.util.*;

import javax.swing.JPanel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

public class WaypointRenderer {
	
	private Map map;
	
	public WaypointRenderer(List<GeoPosition> positions, Map map) {
		setMap(map);
		paintWaypoints(positions);
	}
	
//	when the map is already set
	public WaypointRenderer(List<GeoPosition> positions) {
		paintWaypoints(positions);
	}
	
	private void setMap(Map map) {
		if (this.map == null) {
			this.map = map;
		}
	}
	
//	TODO placeholder, we need the object that will be returned by back-end
	
	private void paintWaypoints(List<GeoPosition> positions) {
		Set<BusWaypoint> waypoints = new HashSet<>();
	    for (GeoPosition pos : positions) {
//	        waypoints.add(new BusWaypoint(pos.getLatitude(), pos.getLongitude()));
	    }

	    WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
	    painter.setWaypoints(waypoints);
	    
	    JXMapViewer map = (JXMapViewer) this.map.getComponent(0);
	    map.setOverlayPainter(painter);
	}
	
	
}
