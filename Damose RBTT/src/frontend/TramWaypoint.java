package frontend;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

public class TramWaypoint  implements Waypoint{

//	we will use this class only if the back-end will send us data for trams
	
	private GeoPosition position;
	
	public TramWaypoint() { 	//Tram t
//		this.position = t.getPosition();
		
	}
	
	public GeoPosition getPosition() {
		return position;
	}
}
