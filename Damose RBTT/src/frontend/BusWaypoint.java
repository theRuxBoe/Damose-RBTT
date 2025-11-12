package frontend;

import java.awt.event.ActionEvent;
<<<<<<< Updated upstream
import java.awt.event.ActionListener;

=======
import backendDONTPUSH.*;
import frontend.focus.BusPanel;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
>>>>>>> Stashed changes
import javax.swing.*;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

<<<<<<< Updated upstream
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
=======
public class BusWaypoint implements Waypoint {
	
	
	private GeoPosition position;
//	private int id;
//	private int line;
//	private String direction;
//	private int seats_available;
	
	
	public BusWaypoint(Bus bus) {
//		super();
		this.position = bus.getPosition();
	}
	
	public BusWaypoint(GeoPosition pos) {
		this.position = pos;
	}
//		this.id = bus.getId();
//		this.direction = bus.getDirection();
//		this.seats_available = bus.getSeats_available();
//		this.line = bus.getLine();
//		addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("ciao");
//			}
//		});
//	}
	
	
>>>>>>> Stashed changes
	@Override
	public GeoPosition getPosition() {
		return position;
	}
<<<<<<< Updated upstream
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
=======
//	public int getId() {
//		return id;
//	}
//	public int getLine() {
//		return line;
//	}
//	public String getDirection() {
//		return direction;
//	}
//	public int getSeats_available() {
//		return seats_available;
//	}
>>>>>>> Stashed changes
	
	
	
	
}
