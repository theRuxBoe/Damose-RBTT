package frontend.focus;

import java.awt.Dimension;
//import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import backendDONTPUSH.Bus;
import frontend.BusWaypoint;

public class BusPanel extends JPanel {
	
	private GeoPosition position;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
	
	
//	when the waypoint is pressed a new buspanel is created to show the vehicle informations
//	this one represents a concrete instance of the line
	public BusPanel(BusWaypoint waypoint) {
		super(new GridLayout(5, 0, 0, 10));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(500,200));
		this.position = waypoint.getPosition();
		this.id = waypoint.getId();
		this.line = waypoint.getLine();
		this.direction = waypoint.getDirection();
		this.seats_available = waypoint.getSeats_available();
		addInfo();
	}
	
	public BusPanel(Bus b) {
		super(new GridLayout(5, 0, 0, 10));
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(500,200));
		this.position = b.getPosition();
		this.id = b.getId();
		this.line = b.getLine();
		this.direction = b.getDirection();
		this.seats_available = b.getSeats_available();
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
	
	
}
