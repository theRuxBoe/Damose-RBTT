package frontend.focus;

import javax.swing.*;

import org.jxmapviewer.viewer.GeoPosition;

public class BusPanel extends JPanel implements Focusable {
	
	private GeoPosition position;
	private int id;
	private int line;
	private String direction;
	private int seats_available;
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
		this.position = b.getPosition();
		this.id = b.getId();
		this.line = b.getLine();
		this.direction = b.getDirection();
		this.seats_available = b.getSeats_available();
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
	
	
}
