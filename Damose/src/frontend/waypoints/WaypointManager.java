package frontend.waypoints;

import java.awt.Font;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;

import backend.model.DatoGTF;
import backend.model.Fermata;
import frontend.main.MapPanel;


public class WaypointManager {
	
	
	{
		
	
	}
	
	private static MapPanel map;
//	private static BusStopRenderer stopRenderer = new BusStopRenderer();
//	private static BusRenderer busRenderer = new BusRenderer();
	private static BufferedImage stopImg;
	private static BufferedImage busImg;

	
	public static void addMap(MapPanel p) {
		map = p;
	}
	
	public static JXMapViewer getMap() {
		return map.getMapViewer();
	}
	
	public static void paintBusStop(GeoPosition g, WaypointPainter<BusWaypoint> painter) {
		if (stopImg == null) {
			try {  stopImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/stop.png")); 
					}
			catch (IOException e) {}
		}
		
		Set<BusWaypoint> waypoints = new HashSet<>();
		waypoints.add(new BusWaypoint(g));
		painter.setWaypoints(waypoints);
		painter.setRenderer(
				(gra, map, w) -> {
					
					
			Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
			int x = (int) point.getX() - stopImg.getWidth() / 2;
	        int y = (int) point.getY() - stopImg.getHeight();
			gra.setFont(new Font("Serif", Font.PLAIN, 30));
			gra.drawImage(stopImg, x, y, null);
		}
		);
	    map.getMapViewer().setOverlayPainter(painter);
	}
	
	
	
	public static void paintBus(BusWaypoint waypoint, WaypointPainter<BusWaypoint> painter) {
		if (busImg == null) {
			try {  busImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/bus.png")); 
					}
			catch (IOException e) {}
		}
		Set<BusWaypoint> waypoints = new HashSet<>();
		waypoints.add(waypoint);
		painter.setWaypoints(waypoints);
		painter.setRenderer((gra, map, w) -> {
			Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
			int x = (int)point.getX() - busImg.getWidth() / 2;
	        int y = (int)point.getY() - busImg.getHeight();
			gra.setFont(new Font("Serif", Font.PLAIN, 30));
			gra.drawImage(busImg.getScaledInstance(64,64 , BufferedImage.SCALE_DEFAULT), x, y, null);
		}
		);
	    map.getMapViewer().setOverlayPainter(painter);
	    map.getMapViewer().repaint();
	}
	
	
	

}
