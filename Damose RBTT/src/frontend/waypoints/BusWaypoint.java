package frontend.waypoints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.imageio.ImageIO;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import frontend.focus.entities.BusPanel;

public class BusWaypoint implements Waypoint {
	
	
	private GeoPosition position;
	
	
	public BusWaypoint(Bus bus) {
		this.position = bus.getPosition();
	}
	
	public BusWaypoint(GeoPosition pos) {
		this.position = pos;
	}

	
	
	@Override
	public GeoPosition getPosition() {
		return position;
	}

	
	
	
	
}
