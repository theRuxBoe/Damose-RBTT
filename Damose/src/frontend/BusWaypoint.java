package frontend;

import java.awt.event.ActionEvent;
import backendDONTPUSH.*;
import frontend.focus.BusPanel;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

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
	
	
	@Override
	public GeoPosition getPosition() {
		return position;
	}
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
	
	
	
	
}
