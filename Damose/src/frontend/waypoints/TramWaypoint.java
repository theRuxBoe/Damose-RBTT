package frontend.waypoints;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

public class TramWaypoint  implements Waypoint{
	//	 ancora più deprecato
	private GeoPosition position;
	
	public TramWaypoint(Tram t) { 	//Tram t
//		this.position = t.getPosition();
		
	}
	
	public GeoPosition getPosition() {
		return position;
	}
}
