package frontend;

import javax.swing.JPanel;

import org.jxmapviewer.viewer.GeoPosition;

public class BusStopPanel extends JPanel {

//	we need to wait until we know how the information is passed from the back-end
	private List<Bus> arrivingBuses;
	private int id;
	private GeoPosition position;
	
	
	public BusStopPanel(GeoPosition position) {
		this.position = position;
	}
	
//	a bus stop panel will display the buses with their arrival times
	public void getArrivingBuses() {}
	
//	
}
