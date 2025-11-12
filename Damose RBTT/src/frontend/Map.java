package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
<<<<<<< Updated upstream
=======
import java.util.ArrayList;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

import backendDONTPUSH.Bus;

>>>>>>> Stashed changes
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;

<<<<<<< Updated upstream
//import javax.swing.*;

public class Map  extends JPanel{
	
	private JXMapViewer map;
	private JPanel panel;
	

	
	
=======

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
>>>>>>> Stashed changes

	private void setZooming() {
		MouseInputListener mice = new PanMouseInputListener(map);
		map.addMouseListener(mice);
		map.addMouseMotionListener(mice);
		map.addMouseListener(new CenterMapListener(map));
		map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));
		map.addKeyListener(new PanKeyListener(map));
	}
	
	
<<<<<<< Updated upstream
	

	
	public JPanel getMapPanel() {
		return this.panel;
=======
	public static JXMapViewer getMapViewer() {
		return map;
	}

	
	public JPanel getMapPanel() {
		return this;
>>>>>>> Stashed changes
	}
	
	
	private void setMapPanel() {
<<<<<<< Updated upstream
		JPanel p = new JPanel();
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
		p.setLayout(new BorderLayout());
		p.add(map);
		
		this.panel = p;
=======
		this.add(map);
		
>>>>>>> Stashed changes
		
		
	}
	
<<<<<<< Updated upstream
	public static void main(String[] args) {
		Map m = new Map();
		m.setMapPanel();
		JFrame f = new JFrame();
		
		f.setLayout(new BorderLayout());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500,500);
		f.add(m.getMapPanel());
		f.setVisible(true);
		
	}
	
=======
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
>>>>>>> Stashed changes
}

