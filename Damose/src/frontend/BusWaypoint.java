package frontend;

import java.awt.event.ActionEvent;
import backendDONTPUSH.*;
import frontend.focus.BusPanel;

import java.awt.event.ActionListener;

import javax.swing.*;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

public class BusWaypoint extends JButton implements Waypoint {
	
	
	private GeoPosition position;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
	
	
	public BusWaypoint(Bus bus) {
		this.position = bus.getPosition();
		this.id = bus.getId();
		this.direction = bus.getDirection();
		this.seats_available = bus.getSeats_available();
		this.line = bus.getLine();
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				it needs to call the focus panel and to show the clicked bus on it
				 	// TODO we don't know if that works
				b.createPanel();
				
			}
		
		});
		
	}
	@Override
	public GeoPosition getPosition() {
		return position;
	}
	public int getId() {
		return id;
	}
	public int getLine() {
		return line;
	}
	public String getDirection() {
		return direction;
	}
	public int getSeats_available() {
		return seats_available;
	}
	
	
	
	
}
