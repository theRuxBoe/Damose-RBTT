package frontend.focus;

import backendDONTPUSH.*;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import frontend.BusWaypoint;
import frontend.MainFrame;
import frontend.Map;


public class BusStopPanel extends JPanel implements Favouritable {	//implements Focusable

	
//	TODO find the right layout!!!
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
//		addScrollPanel();
		if (MainFrame.isLogged() == true) {
			addFavouriteButton();
		}
		showOnMap();
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
//	private JPanel createStopPanel() {
//		JPanel panel = new JPanel();
//		
//		panel.setLayout(new GridLayout(1, 3));
//		panel.setPreferredSize(new Dimension(500,200));
//		panel.add(new JLabel("Stop id : " + id), 0);
//		panel.add(new JLabel("Name : " + name), 0);
		
		
		
//		MainFrame.getMap().add(new StopWaypoint(position));		we could show on the map the stop
		
		private void addScrollPanel() {
		JPanel supportPanel = new JPanel();
		supportPanel.setLayout(new BoxLayout(supportPanel, BoxLayout.Y_AXIS));
		for (Bus bus : arrivingBuses) {
			supportPanel.add(new BusPanel(bus));
//			scrollpanel.add(new JLabel("Arriving time :" + bus.getTime(position))); //unimplemented method
		}
		
		JScrollPane scrollpanel = new JScrollPane(supportPanel);
		scrollpanel.setLayout(new ScrollPaneLayout());
		scrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
//		panel.add(scrollpanel, 1);
		this.add(scrollpanel);
	}
	
//	a bus stop panel will display the buses with their arrival times
//	public void getArrivingBuses() {}

//@Override
//	public JPanel createPanel() {
//		JPanel a = this.createStopPanel();
//		return a;
//	}


	public void setFavourite() {
		 //TODO add a Favourite class for logic in backend
	}
	
	
	
//	
}
