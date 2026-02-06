package frontend.focus.entities;

import javax.swing.JPanel;

import backendNOTPUSH.Bus;
import backendNOTPUSH.BusStop;
import backendNOTPUSH.Entity;
import backendNOTPUSH.Line;

public class EntitiesPanelFactory {

	
	public JPanel createPanel(Entity e) {
		JPanel p = null;
		if (e instanceof BusStop) {
			p = new BusStopPanel((BusStop) e);
		}
		else if (e instanceof Line) {
			p = new LinePanel((Line) e);
		}
		else if (e instanceof Bus) {
			p = new BusPanel((Bus) e);
		}
		
		return p;
		
	}
}
