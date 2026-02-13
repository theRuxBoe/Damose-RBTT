package frontend.waypoints;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;


import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

import backend.model.RouteType;
import frontend.main.MapPanel;


/**
 * The class WaypointManager is used to store methods to paint the entities on the map.
 * It should facilitate work because the user has just one method to call.
 * 
 */
public class WaypointManager {
	
	/** The MapPanel to paint way-points. */
	private static MapPanel map;
	
	/** The image for a bus stop. */
	private static BufferedImage stopImg;
	
	/** The image for a bus. */
	private static BufferedImage busImg;
	
	/** The image for a tram. */
	private static BufferedImage tramImg;
	
	/** The image for a train. */
	private static BufferedImage trainImg;
	
	/** The image for the metro train. */
	private static BufferedImage metroImg;
	
	
	
	

	
	/**
	 * Sets the {@link MapPanel} to work on.
	 *
	 * @param p the MapPanel with the map
	 */
	public static void addMap(MapPanel p) {
		map = p;
	}
	
	/**
	 * Gets the map.
	 *
	 * @return the {@link JXMapViewer} from the MapPanel
	 */
	public static JXMapViewer getMap() {
		return map.getMapViewer();
	}
	
	/**
	 * Paints the bus stop from a {@link GeoPosition} and a painter TODO verificare se questo painter ha senso
	 *
	 * @param g the GeoPosition
	 * @param painter the WaypointPainter
	 */
	public static void paintStop(GeoPosition g, WaypointPainter<GenericWaypoint> painter) {
		if (stopImg == null) {
			try {  stopImg = ImageIO.read(WaypointManager.class.getResource("/res/waypoints/stop.png")); 
					}
			catch (IOException e) {}
		}
		
		Set<GenericWaypoint> waypoints = new HashSet<>();
		waypoints.add(new GenericWaypoint(g));
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
	
	
	
	/**
	 * Paints the  vehicle from a waypoint, a {@link WaypointPainter} and the vehicle type.
	 * Based on the type it paints a different image.
	 *
	 * @param position the Geoposition of the vehicle
	 * @param painter the waypoint painter
	 * @param type the vehicle type
	 */
	public static void paintVehicle(GeoPosition position, WaypointPainter<GenericWaypoint> painter, RouteType type) {
		
		Set<GenericWaypoint> waypoints = new HashSet<>();
		waypoints.add(new GenericWaypoint(position));
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
		}
	}
	
	
	/**
	 * Selects the image for a specific vehicle type.
	 *
	 * @param type the vehicle type
	 * @return the buffered image to be painted on the map
	 */
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
