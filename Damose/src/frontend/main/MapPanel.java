package frontend.main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import frontend.waypoints.WaypointManager;

import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;

/**
 * The Class MapPanel contains the JXMap viewer to display and offers the viewer
 * to be painted on.
 */
public class MapPanel extends JPanel {

	/** The map viewer. */
	private JXMapViewer map;

	/**
	 * Instantiates a new map panel by creating the viewer and adding it to the
	 * panel. It also links the map viewer to Waypoint Manager for future use.
	 */
	public MapPanel() {
		super(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(500, 500));
		setMapPanel();

		WaypointManager.addMap(this);

	}

	/**
	 * Adds the zooming to the map.
	 */
	private void addZooming() {
		MouseInputListener mice = new PanMouseInputListener(map);
		map.addMouseListener(mice);
		map.addMouseMotionListener(mice);
		map.addMouseListener(new CenterMapListener(map));
		map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));
		map.addKeyListener(new PanKeyListener(map));
	}

	/**
	 * Gets the map viewer.
	 *
	 * @return the map viewer
	 */
	public JXMapViewer getMapViewer() {
		return map;
	}

	/**
	 * Creates the map viewer and adds it to the map panel.
	 */
	private void setMapPanel() {
		JXMapViewer map = new JXMapViewer();
		TileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		map.setTileFactory(tileFactory);

		tileFactory.setThreadPoolSize(8);

		GeoPosition romeCenter = new GeoPosition(41.890210, 12.492231);
		map.setZoom(5);
		map.setAddressLocation(romeCenter);

		
		this.map = map;
		addZooming();
		this.add(map);

	}
}
