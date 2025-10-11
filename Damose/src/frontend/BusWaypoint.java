package frontend;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

public class BusWaypoint implements Waypoint{
	
	
	private double lat;
	private double lon;
	private double id;
//	private 
	
	
	public BusWaypoint(Bus bus) {
		this.lat = bus.latitudine;
		this.lon = bus.longitudine;
		this.id = bus.id;
//		this.listener = add
	}
	@Override
	public GeoPosition getPosition() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
