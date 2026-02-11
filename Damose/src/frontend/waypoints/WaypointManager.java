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
import backend.model.RouteType;
import frontend.main.MapPanel;


public class WaypointManager {
	
	
	{
		
	
	}
	
	private static MapPanel map;
//	private static BusStopRenderer stopRenderer = new BusStopRenderer();
//	private static BusRenderer busRenderer = new BusRenderer();
	private static BufferedImage stopImg;
	private static BufferedImage busImg;
	private static BufferedImage tramImg;
	private static BufferedImage trainImg;
	private static BufferedImage metroImg;
	
	
	
	

	
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
	
	
	
	public static void paintBus(BusWaypoint waypoint, WaypointPainter<BusWaypoint> painter, RouteType type) {
		
		Set<BusWaypoint> waypoints = new HashSet<>();
		waypoints.add(waypoint);
		BufferedImage img = selectImage(type);
		if (img != null) {
			if (img == busImg && busImg == null) {
				try {  busImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/bus.png")); 
						}
				catch (IOException e) {}
			}
			else if (img == tramImg && tramImg == null) {
				try {  tramImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/tram.png")); 
				}
				catch (IOException e) {}
			}
			else if (img == metroImg && metroImg == null) {
				try {  metroImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/metro.png")); 
				}
				catch (IOException e) {}
			}
			else if(img == trainImg && trainImg == null) {
				try {  trainImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/train.png")); 
				}
				catch (IOException e) {}
			}
			
			
			
			
			
		painter.setWaypoints(waypoints);
		painter.setRenderer((gra, map, w) -> {
			Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
			int x = (int) point.getX() - img.getWidth() / 2;
	        int y = (int) point.getY() - img.getHeight();
			gra.setFont(new Font("Serif", Font.PLAIN, 30));
			gra.drawImage(img, x, y, null);
		}
		);
	    map.getMapViewer().setOverlayPainter(painter);
//	    map.getMapViewer().repaint();
		}
	}
	
	
	private static BufferedImage selectImage(RouteType type) {
			
			BufferedImage x;
	        switch (type) {
	        case TRAM:
	        	x = tramImg;
	            break;
	        case METRO:
	        	x = metroImg;
	            break;
	        case TRAIN:
	        	x = trainImg;
	            break;
	        case BUS:
	        	x = busImg;
	        	break;
	        
	        default:
	        	x = null;
	        	break;
	        }
		return x;
	}
		
		
	

}
