package frontend.focus.entities;

import javax.swing.JPanel;

public class EntitiesPanelFactory {

	
	public JPanel createPanel(DatoGtf e) {
		JPanel p;
		if (e instanceof BusStop) {
			p = new BusStopPanel(e);
		}
		else if (e instanceof Line) {
			p = new LinePanel(e);
		}
		else if (e instanceof Bus) {
			p = new BusPanel(e);
		}
		
		return p;
		
	}
}
