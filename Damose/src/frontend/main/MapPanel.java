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

//import frontend.utilities.WaypointRenderer;

import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;


public class MapPanel  extends JPanel{
	
	private JXMapViewer map;

	public MapPanel() {
		super(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(500,500));
		setMapPanel();
		
		WaypointManager.addMap(this);
		
	}

	private void setZooming() {
		MouseInputListener mice = new PanMouseInputListener(map);
		map.addMouseListener(mice);
		map.addMouseMotionListener(mice);
		map.addMouseListener(new CenterMapListener(map));
		map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));
		map.addKeyListener(new PanKeyListener(map));
	}
	
	
	public JXMapViewer getMapViewer() {
		return map;
	}

	
	public JPanel getMapPanel() {
		return this;
	}
	
	
	private void setMapPanel() {
		JXMapViewer map = new JXMapViewer();
		this.map = map;
		TileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
		
		DefaultTileFactory tileFactory = new DefaultTileFactory(info);
		
		map.setTileFactory(tileFactory);
		
		tileFactory.setThreadPoolSize(8);
		
//		Setting Rome center as starting point
		GeoPosition romeCenter = new GeoPosition(41.890210, 12.492231);
		map.setZoom(5);
		map.setAddressLocation(romeCenter);
//		map.setSize(new Dimension(500,500));
		
//		Adding the mouse listener for panning and zooming
		
		
		setZooming();
		this.add(map);
		
		
		
	}
}

