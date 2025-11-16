package frontend.focus.entities;

import backendDONTPUSH.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import frontend.MainFrame;
import frontend.Map;
import frontend.ScrollablePanel;
import frontend.focus.SearchFocusPanel;
import frontend.waypoints.BusWaypoint;


public class BusStopPanel extends ScrollablePanel {	//implements Focusable

	

	private List<Bus> arrivingBuses;
	private int id;
	private GeoPosition position;
	private String name;
	
	
	public BusStopPanel(BusStop bs) {
		super();
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(500,200));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.id = bs.getId();
		this.position = bs.getPosition();
		this.arrivingBuses = bs.getArrivingBuses();
//		this.name = bs.getName();		
		addInfo();
		addListener();
//		setContent(BusStop.getArrivingBuses());
	
//		addScrollPanel();
		if (MainFrame.isLogged() == true) {
			addFavouriteButton();
		}
		showOnMap();
	}
	
	private void addListener() {
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
				
				BusStopPanel.this.setBackground(new Color(0));		//sets the colour to the original
			}			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				SearchFocusPanel.getFocus().setStop(BusStopPanel.this);
				BusStopPanel.this.setBackground(new Color(808080));		//sets the colour to grey
				
			}
		});
	}
	
	
	private JPanel addInfo() {
		add(new JLabel("Stop id : " + id), SpringLayout.VERTICAL_CENTER);
		add(new JLabel("Name : " + name), SpringLayout.BASELINE);
		
		return this;
	}
	
	private void addFavouriteButton() {
		JButton b = new JButton("☆");
		b.setSize(new Dimension(10,10));
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getText() == "☆") {
					b.setText("★");
//					this will put the stop into the favourites area
				}
				else {
					b.setText("☆");
//					this will kick the stop from the favourites
				}
				
			}
		});
		this.add(b, SpringLayout.EAST);
		
	}
	
	private void showOnMap() {
		JXMapViewer m = Map.getMapViewer();
		m.setCenterPosition(position);
		WaypointPainter<Waypoint> painter = new WaypointPainter<>();
		HashSet<BusWaypoint> h = new HashSet();
		h.add(new BusWaypoint(this.position));
		
		painter.setWaypoints(h);
	    
	    m.setOverlayPainter(painter);
	}

		
//		private void addScrollPanel() {
//		JPanel supportPanel = new JPanel();
//		supportPanel.setLayout(new BoxLayout(supportPanel, BoxLayout.Y_AXIS));
//		for (Bus bus : arrivingBuses) {
//			supportPanel.add(new BusPanel(bus));
////			scrollpanel.add(new JLabel("Arriving time :" + bus.getTime(position))); //unimplemented method
//		}
//		
//		JScrollPane scrollpanel = new JScrollPane(supportPanel);
//		scrollpanel.setLayout(new ScrollPaneLayout());
//		scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		
////		panel.add(scrollpanel, 1);
//		this.add(scrollpanel);
//	}
	
}
