package frontend;

import javax.swing.JPanel;

public class BusStopPanel extends JPanel {

//	we need to wait until we know how the information is passed from the back-end
	private List<Bus> arrivingBuses;
	private int id;
	
	
	public BusStopPanel(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
//	a bus stop panel will display the buses with their arrival times
	public void getArrivingBuses() {}
	
//	
}
