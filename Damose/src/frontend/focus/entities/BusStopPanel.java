package frontend.focus.entities;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
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

import backendNOTPUSH.Bus;
import backendNOTPUSH.BusStop;
import frontend.MainFrame;
import frontend.MapPanel;
import frontend.ScrollablePanel;
import frontend.focus.SearchFocusPanel;
import frontend.focus.SearchPanel;
import frontend.main.FocusController;
import frontend.main.RightPanel;
import frontend.utilities.WaypointRenderer;
import frontend.waypoints.BusWaypoint;


public class BusStopPanel extends ScrollablePanel {


	
	private int id;
	private GeoPosition position;
	private String name;
	private BusStop stop;
	
//	private RightPanel rp;
	private Color defaultColor = this.getBackground();
	
	
	public BusStopPanel(BusStop bs) {
		super();
		
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(350,50));
		stop = bs;
		this.name = bs.getName();
		this.id = bs.getId();
		this.position = bs.getPosition();
		addLabelsData();
		addListener();
	
		if (MainFrame.isLogged() == true) {
			addFavouriteButton();
		}
	}
	
	
	private void addListener() {
		addMouseListener(new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {
						BusStopPanel.this.setBackground(defaultColor);
			}			
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {

				if (BusStopPanel.this.getBackground() == defaultColor) {
					BusStopPanel.this.setBackground(Color.LIGHT_GRAY);		
					BusStopFocus bsfocus = new BusStopFocus(BusStopPanel.this);
					
					FocusController.openFocus(bsfocus);
				}
					
				else {
					BusStopPanel.this.setBackground(defaultColor);
				}
				showOnMap();
				
			}
		});
	}

	
	

	
	private void addLabelsData() {
		JLabel data = new JLabel("  " + name + " - " + id);
		data.setFont(new Font("Serif", Font.PLAIN, 30));
		add(data);
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
		this.add(b, BorderLayout.EAST);
		
	}
	
	private void showOnMap() {
//		JXMapViewer m = MapPanel.getMapViewer();
		MapPanel.getMapViewer().setCenterPosition(position);
		WaypointRenderer.paintWaypoints(new BusWaypoint(stop.getPosition()));
//		WaypointPainter<Waypoint> painter = new WaypointPainter<>();
//		HashSet<BusWaypoint> h = new HashSet();
//		h.add(new BusWaypoint(this.position));
//		
//		painter.setWaypoints(h);
	    
//	    m.setOverlayPainter(painter);
	}

	public BusStop getStop() {
		return stop;
	}
}
