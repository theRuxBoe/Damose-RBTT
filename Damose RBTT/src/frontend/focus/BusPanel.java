package frontend.focus;

<<<<<<< Updated upstream
import javax.swing.*;

import org.jxmapviewer.viewer.GeoPosition;

public class BusPanel extends JPanel implements Focusable {
=======
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
//import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import backendDONTPUSH.Bus;
import frontend.BusWaypoint;

public class BusPanel extends JPanel {
>>>>>>> Stashed changes
	
	private GeoPosition position;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
<<<<<<< Updated upstream
	private List<BusStop> line_stops;	// TODO we will need to check if List is the optimal data structure
	
	
//	when the waypoint is pressed a new buspanel is created to show the vehicle informations
//	this one represents a concrete instance of the line
	public BusPanel(BusWaypoint waypoint) {
		this.position = waypoint.getPosition();
		this.id = waypoint.getId();
		this.line = waypoint.getLine();
		this.direction = waypoint.getDirection();
		this.seats_available = waypoint.getSeats_available();
	}
	
	public BusPanel(Bus b) {
=======
	
	

//	public BusPanel(BusWaypoint waypoint) {
//		super(new GridLayout(5, 0, 0, 10));
//		setBorder(new BevelBorder(BevelBorder.LOWERED));
//		setPreferredSize(new Dimension(500,200));
//		
//		this.position = waypoint.getPosition();
//		this.id = waypoint.getId();
//		this.line = waypoint.getLine();
//		this.direction = waypoint.getDirection();
//		this.seats_available = waypoint.getSeats_available();
//		addInfo();
//	}
	
	public BusPanel(Bus b) {
		super(new GridLayout(5, 1, 11, 0));
//		super(new FlowLayout(FlowLayout.TRAILING, 100, 3));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(500,200));
>>>>>>> Stashed changes
		this.position = b.getPosition();
		this.id = b.getId();
		this.line = b.getLine();
		this.direction = b.getDirection();
		this.seats_available = b.getSeats_available();
<<<<<<< Updated upstream
	}
	

	
	private JPanel createBusPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.add(new JLabel("Position :" + this.position));
		panel.add(new JLabel("Vehicle ID :" + this.id));
		panel.add(new JLabel("Line :" + this.line));
		panel.add(new JLabel("Direction :" + this.direction));
		if (seats_available == -1) {
			panel.add(new JLabel("Seats unavailable"));
		}
		else {
			panel.add(new JLabel("Seats_available :" + seats_available));
		}
		return panel;
		
		
	}

	@Override
	public JPanel createPanel() {
		this.createBusPanel();
		
	}
=======
		addInfo();
	}
	

	private JPanel addInfo() {
		add(new JLabel("Line : " + this.line), 0);
		add(new JLabel("Direction : " + this.direction), 1);
		add(new JLabel("Vehicle ID : " + this.id), 2);
		if (seats_available == -1) {
			add(new JLabel("Seats : unavailable"), 3);
		}
		else {
			add(new JLabel("Seats available : " + seats_available), 3);
		}
		return this;
	}
	
//	private JPanel createBusPanel() {
//		JPanel panel = new JPanel();
//		panel.setLayout(new GridLayout(5, 0, 0, 10));
//		
//		panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
//		
//		panel.setPreferredSize(new Dimension(500,200));
//		panel.add(new JLabel("Line : " + this.line), 0);
//		
//		panel.add(new JLabel("Direction : " + this.direction), 1);
//		
//		panel.add(new JLabel("Position : " + this.position), 2);
//		
//		panel.add(new JLabel("Vehicle ID : " + this.id), 3);
//		if (seats_available == -1) {
//			panel.add(new JLabel("Seats : unavailable"), 4);
//		}
//		else {
//			panel.add(new JLabel("Seats available : " + seats_available), 4);
//		}
//		return panel;
//		
//		
//	}

//	@Override
//	public JPanel createPanel() {
//		JPanel pan = this.createBusPanel();
//		return pan;
//	}
>>>>>>> Stashed changes
	
	
}
