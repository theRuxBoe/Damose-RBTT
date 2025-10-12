package frontend;

import javax.swing.*;

import org.jxmapviewer.viewer.GeoPosition;

public class BusPanel extends JPanel {
	
	private GeoPosition position;
	private double id;
	private int line;
	private String direction;
	private int seats_available;
	
	public BusPanel(BusWaypoint waypoint) {
		this.position = waypoint.getPosition();
		this.id = waypoint.getId();
		this.line = waypoint.getLine();
		this.direction = waypoint.getDirection();
		this.seats_available = waypoint.getSeats_available();
		
		
	}
	
	public BusPanel(Bus b) {
		this.position = b.getPosition();
		this.id = b.getId();
		this.line = b.getLine();
		this.direction = b.getDirection();
		this.seats_available = b.getSeats_available();
		
		
	}
	
	public JPanel createBusPanel() {
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
		
		
	}
	
	
}
