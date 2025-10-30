package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

//import java.awt.peer.ComponentPeer;

//import java.awt.Frame;
//import java.awt.event.KeyAdapter;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputListener;
import javax.tools.JavaCompiler;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import backendDONTPUSH.Bus;

import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;


public class Map  extends JPanel{
	
	private static JXMapViewer map;
	private JPanel panel;
	private WaypointRenderer renderer;

	public Map() {
		super(new BorderLayout());
		setMapPanel();
		List<Bus> list = new ArrayList<Bus>();
		list.add(new Bus());
		refreshPaint(list);
		
	}
	
	
	
	public void refreshPaint(List<Bus> buses) {
 		if (renderer == null) {
		WaypointRenderer rend = new WaypointRenderer(buses, getMapViewer());
		this.renderer = rend;
 		}
 		renderer.setAndPaintWaypoints(buses);
 		
 	}

	private void setZooming() {
		MouseInputListener mice = new PanMouseInputListener(map);
		map.addMouseListener(mice);
		map.addMouseMotionListener(mice);
		map.addMouseListener(new CenterMapListener(map));
		map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));
		map.addKeyListener(new PanKeyListener(map));
	}
	
	
	public static JXMapViewer getMapViewer() {
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
		map.setSize(new Dimension(500,500));
		
//		Adding the mouse listener for panning and zooming
		
		
		setZooming();
		this.add(map);
		
		
		
	}
	
//	public static void main(String[] args) {
//		Map m = new Map();
//		m.setMapPanel();
//		JFrame f = new JFrame();
//		
//		f.setLayout(new BorderLayout());
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setSize(500,500);
//		f.add(m.getMapPanel());
//		f.setVisible(true);
//		
//	
//	}
}

