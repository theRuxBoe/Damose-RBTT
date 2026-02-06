package backendNOTPUSH;

import org.jxmapviewer.viewer.GeoPosition;

public class Bus extends Entity{

	private GeoPosition position ;
	private int id, line, seats_available;
	private String direction;
	private int estimatedTime;
	
	public Bus() {
		position = new GeoPosition(0,0);
		id = 12345;
		line = 716;
		seats_available = 0;
		direction = "Ballarin";
		estimatedTime = 4;
		
	}
	
	public int getEstimatedTime() {
		return estimatedTime;
	}
	
	public GeoPosition getPosition() {
		return position;
	}
	public int getId() {
		return id;
	}
	public int getLine() {
		return line;
	}
	public int getSeats_available() {
		return seats_available;
	}
	public String getDirection() {
		return direction;
	}
	
}
