package frontend;

import javax.swing.JPanel;

public class BusStopFocusPanel extends JPanel {
	
	private BusStopView currentStop;
	private BusPanel currentBus;
	
	public BusStopFocusPanel() {
//		should we place something here?
	}
	
//	TODO enter the input type for shoBus, it should be something like a JPanel
	public JPanel showBus(BusPanel b) {
		if (currentStop != null || currentBus != null) {
			this.clear();
		}
		JPanel panel = b.createBusPanel();
		return panel;
	}
	

//	TODO same as above except for bus stops
	public JPanel showStop(BusStopView s) {
		if (currentStop != null || currentBus != null) {
			this.clear();
		}
		return s;
	}
	
//	I would need a specific class for buses and bus stops, with both of them exposing methods 
//	to get their infos, 
	
//	TODO triggered by an x button when the user selects another bus/bus stop
	public void clear() {
		currentStop = null;
		currentBus = null;
	}
	
	
}
