package frontend;

import java.awt.event.ActionEvent;
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
//	private 
	
	
	public BusWaypoint(Bus bus) {
		this.position = new GeoPosition(bus.lat, bus.lon);
		this.id = bus.id;
//		this.listener = add
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				it needs to call the focus panel and to show the clicked bus on it
				
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
