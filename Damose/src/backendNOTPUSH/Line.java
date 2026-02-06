package backendNOTPUSH;

import java.util.ArrayList;
import java.util.List;

public class Line extends Entity{

	
	private ArrayList<BusStop> stops;
	private int id;
	private String direction;
	
	public Line() {
		stops = new ArrayList<>();
		stops.add(new BusStop());
		stops.add(new BusStop());
		stops.add(new BusStop());
		stops.add(new BusStop());
		id = 000;
		direction = "messina";
	}
	
	
	public ArrayList<BusStop> getStops() {
		return stops;
	}
	public int getId() {
		return id;
	}
	public String getDirection() {
		return direction;
	}
	
	
}
