package frontend.utilities;

import java.util.*;

import javax.swing.JPanel;


import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import backendNOTPUSH.Bus;
import backendNOTPUSH.Entity;
import frontend.MapPanel;
import frontend.waypoints.BusWaypoint;


public class WaypointRenderer {
	
	private static JXMapViewer map;
	private static WaypointPainter<Waypoint> painter = new WaypointPainter<>();
	private static WaypointFactory wpf = new WaypointFactory();
	

	
	public static void addMap(MapPanel m) {
		map = m.getMapViewer();
	}
	
//	da capire
//	public static void setAndPaintWaypoints(List<Entity> buses) {
//		waypointConverter(buses);
//		paintWaypoints();
//	}
	
//	public static void setAndPaintWaypoints(GeoPosition gp) {
//		
//		paintWaypoints(new BusWaypoint(gp));
//	}
	
	public static void paintWaypoints(BusWaypoint gp) {
		Set<BusWaypoint> waypoints = new HashSet<>();
		waypoints.add(gp);
	    painter.setWaypoints(waypoints);
	    
	    map.setOverlayPainter(painter);
	    
	}
	
	
	public static void paintBusWaypoints(List<Entity> bb) {
		Set<Waypoint> wp = waypointConverter(bb);
		painter.setWaypoints(wp);
		map.setOverlayPainter(painter);
		
	}
	
//	public static void 
	
	
	private static <T extends Entity> Set<Waypoint>  waypointConverter(List<T> entities) {
		Set<Waypoint> waypoints = new HashSet<Waypoint>();
		
		for (T e : entities) {
			waypoints.add(wpf.createWaypoint(e));
		}
		return waypoints;
	}
	

}
