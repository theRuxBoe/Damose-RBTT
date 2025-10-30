package frontend;

import java.util.*;

import javax.swing.JPanel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import backendDONTPUSH.Bus;

public class WaypointRenderer {
	
	private JXMapViewer map;
	private HashSet<Waypoint> waypoints;
	
	
	public WaypointRenderer(List<Bus> buses, JXMapViewer map) {
		if (this.map == null) {
			this.map = map;
		}
		waypointConverter(buses);
		paintWaypoints();
		
//		WaypointRenderer(buses);	// i would like to call the other constructor to not rewrite the code
	}
	

	
	public void setAndPaintWaypoints(List<Bus> buses) {
		waypointConverter(buses);
		paintWaypoints();
	}
	
	private void paintWaypoints() {
		WaypointPainter<Waypoint> painter = new WaypointPainter<>();
	    painter.setWaypoints(waypoints);
	    
//	    JXMapViewer map = (JXMapViewer) this.map.getComponent(0);
	    map.setOverlayPainter(painter);
	    
	}
	
	private void waypointConverter(List<Bus> buses) {
		this.waypoints = new HashSet<Waypoint>();
		for (Bus b : buses) {
			waypoints.add(new BusWaypoint(b));
		}
	}
	
	public HashSet<Waypoint> getWaypoints() {
		return waypoints;
	}
	
}
