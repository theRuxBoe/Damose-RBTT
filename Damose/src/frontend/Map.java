package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;

//import javax.swing.*;

public class Map  extends JPanel{
	
	private JXMapViewer map;
//	private int MAXIMUM_ZOOM = 10;
	private JPanel panel;
	
	public Map() {
		}
	
	
	
	public void paintWaypoint() {}
	
	private void setZooming() {
		MouseInputListener mice = new PanMouseInputListener(map);
		map.addMouseListener(mice);
		map.addMouseMotionListener(mice);
		map.addMouseListener(new CenterMapListener(map));
		map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));
		map.addKeyListener(new PanKeyListener(map));
	}
	
	
	

	
	public JPanel getMapPanel() {
		return this.panel;
	}
	
	
	private void setMapPanel() {
		JPanel p = new JPanel();
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
		p.setLayout(new BorderLayout());
		p.add(map);
		
		this.panel = p;
		
		
	}
	
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
	
}

