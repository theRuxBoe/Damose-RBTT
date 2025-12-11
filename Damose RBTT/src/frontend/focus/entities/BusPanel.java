package frontend.focus.entities;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
//import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import frontend.waypoints.BusWaypoint;

public class BusPanel extends JPanel {
	
	private GeoPosition position;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
	
	
	
	public BusPanel(Bus b) {
		super(new GridLayout(5, 1, 11, 0));
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
	

	
}
