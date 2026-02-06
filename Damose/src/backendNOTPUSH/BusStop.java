package backendNOTPUSH;

import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

public class BusStop extends Entity{

	
	private ArrayList<Bus> arrivingBuses;
	private int id;
	private GeoPosition position;
	private String name;
	
	public BusStop() {
		arrivingBuses = new ArrayList<>();
		arrivingBuses.add(new Bus());
		arrivingBuses.add(new Bus());
		id = 00000;
		position = new GeoPosition(0,0);
		name = "pippo";
	}
	
	public ArrayList<Bus> getArrivingBuses() {
		return arrivingBuses;
	}
	public int getId() {
		return id;
	}
	public GeoPosition getPosition() {
		return position;
	}
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return name + "(" + id + ")";
	}
	
}
