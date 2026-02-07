package frontend.waypoints;

import java.util.Optional;


import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import backend.model.Corsa;
import backend.realtime.VehiclePositionInfo;
import frontend.MainFrame;

public class BusWaypoint implements Waypoint {
//	deprecated (deve morire)
	
	private GeoPosition position;
	
	
	public BusWaypoint(Corsa bus) {
		Optional<VehiclePositionInfo> x = MainFrame.getTTS().getVehiclePositionForTripId(bus.getTripId());
		double lon = x.get().getLon();
		double lat = x.get().getLat();
		
		this.position = new GeoPosition(lat, lon);
	}
	
	public BusWaypoint(GeoPosition pos) {
		this.position = pos;
	}

//	aggiungeremo un'immagine diversa per gli autobus
	
	@Override
	public GeoPosition getPosition() {
		return position;
	}

	
	
	
	
}
