package main.java.frontend.waypoints;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;

import main.java.backend.model.PredizioneArrivo;
import main.java.backend.model.RouteType;
import main.java.backend.realtime.VehiclePositionInfo;
import main.java.frontend.BackendController;
import main.java.frontend.MapPanel;

/**
 * The class WaypointManager is used to store methods to paint the entities on
 * the map. It should facilitate work because the user has just one method to
 * call.
 * 
 */
public class WaypointManager {

	/** The MapPanel to paint way-points. */
	private static MapPanel map;

	/** The compound painter. */
	private static CompoundPainter<JXMapViewer> compound = new CompoundPainter<JXMapViewer>();
	
	/** The bus painter. */
	private static WaypointPainter<VehicleWaypoint> busPainter = new WaypointPainter<VehicleWaypoint>();
	
	/** The tram painter. */
	private static WaypointPainter<VehicleWaypoint> tramPainter = new WaypointPainter<VehicleWaypoint>();
	
	/** The metro painter. */
	private static WaypointPainter<VehicleWaypoint> metroPainter = new WaypointPainter<VehicleWaypoint>();
	
	/** The train painter. */
	private static WaypointPainter<VehicleWaypoint> trainPainter = new WaypointPainter<VehicleWaypoint>();
	
	/** The stop painter. */
	private static WaypointPainter<StopWaypoint> stopPainter = new WaypointPainter<StopWaypoint>();
	
	
	
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
	 * Initializes the Waypoint manager by creating the various
	 * specifical painters for buses, stops and more.
	 */
	
	public static void setUp() {
		
		busPainter.setRenderer(new BusRenderer());
		compound.addPainter(busPainter);
		
		tramPainter.setRenderer(new TramRenderer());
		compound.addPainter(tramPainter);
		
		metroPainter.setRenderer(new MetroRenderer());
		compound.addPainter(metroPainter);
		
		trainPainter.setRenderer(new TrainRenderer());
		compound.addPainter(trainPainter);
		
		stopPainter.setRenderer(new StopRenderer());
		compound.addPainter(stopPainter);
		
		map.getMapViewer().setOverlayPainter(compound);
		
	}
	
	/**
	 * Paints a stop from a {@link GeoPosition}.
	 *
	 * @param g       the GeoPosition
	 */
	public static void paintStop(GeoPosition g) {
		
		Set<StopWaypoint> waypoints = new HashSet<>();
		waypoints.add(new StopWaypoint(g));
		stopPainter.setWaypoints(waypoints);
		
		
	}

	/**
	 * Paints the vehicle from a set of {@link VehicleWaypoint}. 
	 * Based on the type it paints a different image thanks to
	 * the {@link VehicleImgFactory}.
	 *
	 * @param vehicles the set containing the vehicles
	 */
	public static void paintVehicles(Set<PredizioneArrivo> vehicles) {
		
		
		
		Set<VehicleWaypoint> buses = new HashSet<>();
		Set<VehicleWaypoint> trams = new HashSet<>();
		Set<VehicleWaypoint> metros = new HashSet<>();
		Set<VehicleWaypoint> trains = new HashSet<>();
		
		busPainter.setWaypoints(buses);
		tramPainter.setWaypoints(trams);
		metroPainter.setWaypoints(metros);
		trainPainter.setWaypoints(trains);
		map.getMapViewer().repaint();
		
		
		
		for (PredizioneArrivo v : vehicles) {
			 RouteType type = RouteType.fromCode(BackendController.getTTS().getLinea(v.getRouteId()).getType());
			 if (type == null) {
				 continue;
			 }
			 Optional<VehiclePositionInfo> position = BackendController.getTTS().getVehiclePositionForTripId(v.getTripId());
			 if ( position.isPresent()) {
				 if (type.equals(RouteType.BUS)) {
					 buses.add(new VehicleWaypoint(position.get().getLat(), position.get().getLon()));
				 }
				 else if (type.equals(RouteType.TRAM)) {
					trams.add(new VehicleWaypoint(position.get().getLat(), position.get().getLon()));
				 }
				 else if (type.equals(RouteType.METRO)){
					 metros.add(new VehicleWaypoint(position.get().getLat(), position.get().getLon()));
				 }
				 else if (type.equals(RouteType.TRAIN)){
					 trains.add(new VehicleWaypoint(position.get().getLat(), position.get().getLon()));
				 }
				 
				 else {
					 continue;
				 }
			 
			 }
			
		}
		
		
		if (!buses.isEmpty()) {
			busPainter.setWaypoints(buses);
		}
		if (!trams.isEmpty()) {
			tramPainter.setWaypoints(trams);
		}
		
		if (!metros.isEmpty()) {
			metroPainter.setWaypoints(metros);
		}
		if (!trains.isEmpty()) {
			trainPainter.setWaypoints(trains);
		}
		
		
		map.getMapViewer().repaint();
		
	}
}

final class BusRenderer implements WaypointRenderer<VehicleWaypoint> {

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, VehicleWaypoint w) {
		VehicleImgFactory vif = new VehicleImgFactory();
		BufferedImage img = vif.selectImage(RouteType.BUS);
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		int x = (int) point.getX() - img.getWidth() / 2;
		int y = (int) point.getY() - img.getHeight();
		g.setFont(new Font("Serif", Font.PLAIN, 30));
		g.drawImage(img, x, y, null);
	}

}

final class TramRenderer implements WaypointRenderer<VehicleWaypoint> {

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, VehicleWaypoint w) {
		VehicleImgFactory vif = new VehicleImgFactory();
		BufferedImage img = vif.selectImage(RouteType.TRAM);
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		int x = (int) point.getX() - img.getWidth() / 2;
		int y = (int) point.getY() - img.getHeight();
		g.setFont(new Font("Serif", Font.PLAIN, 30));
		g.drawImage(img, x, y, null);
	}

}

final class MetroRenderer implements WaypointRenderer<VehicleWaypoint> {

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, VehicleWaypoint w) {
		VehicleImgFactory vif = new VehicleImgFactory();
		BufferedImage img = vif.selectImage(RouteType.METRO);
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		int x = (int) point.getX() - img.getWidth() / 2;
		int y = (int) point.getY() - img.getHeight();
		g.setFont(new Font("Serif", Font.PLAIN, 30));
		g.drawImage(img, x, y, null);
	}

}

final class TrainRenderer implements WaypointRenderer<VehicleWaypoint> {

	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, VehicleWaypoint w) {
		VehicleImgFactory vif = new VehicleImgFactory();
		BufferedImage img = vif.selectImage(RouteType.TRAIN);
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		int x = (int) point.getX() - img.getWidth() / 2;
		int y = (int) point.getY() - img.getHeight();
		g.setFont(new Font("Serif", Font.PLAIN, 30));
		g.drawImage(img, x, y, null);
	}
		
		
	}
final class StopRenderer implements WaypointRenderer<StopWaypoint> {

	private BufferedImage stopImg;
	
	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, StopWaypoint w) {

		if (stopImg == null) {
			try {
				stopImg = ImageIO.read(WaypointManager.class.getResource("/main/res/waypoints/stop.png"));
			} catch (IOException e) {
			}
		}
		Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
		
		int x = (int) point.getX() - stopImg.getWidth() / 2;
		int y = (int) point.getY() - stopImg.getHeight();
		g.setFont(new Font("Serif", Font.PLAIN, 30));
		g.drawImage(stopImg, x, y, null);
	}
}
