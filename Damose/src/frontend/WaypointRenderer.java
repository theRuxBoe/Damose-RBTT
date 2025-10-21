package frontend;

import java.util.*;

import javax.swing.JPanel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import backendDONTPUSH.Bus;

public class WaypointRenderer {
	
	private JXMapViewer map;
	private HashSet<BusWaypoint> waypoints;
	
	
	public WaypointRenderer(List<Bus> buses, JXMapViewer map) {
		this.map = map;
		paintWaypoints(buses);
	}
	
//	TODO placeholder, we need the object that will be returned by back-end
	private void paintWaypoints(List<Bus> buses) {
		WaypointAggregator wa = new WaypointAggregator(buses);
		WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
	    painter.setWaypoints(wa.getWaypoints());
	    
//	    JXMapViewer map = (JXMapViewer) this.map.getComponent(0);
	    map.setOverlayPainter(painter);
	    
	}
	
}
