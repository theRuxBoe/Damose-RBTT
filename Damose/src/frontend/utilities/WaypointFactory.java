package frontend.utilities;

import org.jxmapviewer.viewer.Waypoint;

import backendNOTPUSH.Bus;
import backendNOTPUSH.Entity;
import frontend.waypoints.BusWaypoint;
import frontend.waypoints.TramWaypoint;

public class WaypointFactory {

	public WaypointFactory() {
		// TODO Auto-generated constructor stub
	}
	
//	public <T extends Entity> <W implements Waypoint> createWaypoint(T e) {
	public <T extends Entity,W extends Waypoint> W createWaypoint(T e) {	
		
		if ( e instanceof Bus ) {
			BusWaypoint bw = new BusWaypoint((Bus) e);
			return bw ;
		}
		else if ( e instanceof Tram) {
			TramWaypoint tw = new TramWaypoint((Tram) e);
			return tw;
		}
		
		
		
	}

}
