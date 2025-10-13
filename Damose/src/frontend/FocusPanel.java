package frontend;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class FocusPanel extends JPanel {
	
	private JPanel current;
	
	public FocusPanel() {
		super();
		this.setLayout(null);	// TODO da sistemare
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(500,300));
		this.add(new JLabel("You have selected :"));
		
	}

	
//	TODO enter the input type for shoBus, it should be something like a JPanel
	public JPanel showBus(BusPanel b) {
		if (current != null) {
			this.clear();
		}
		JPanel panel = b.createBusPanel();
		return panel;
	}
	

//	TODO same as above except for bus stops
	public JPanel showStop(BusStopPanel s) {
		if (current != null) {
			this.clear();
		}
		return s;
	}
	
//	I would need a specific class for buses and bus stops, with both of them exposing methods 
//	to get their infos, 
	
//	TODO triggered by an x button when the user selects another bus/bus stop
	public void clear() {
		current = null;
	}
	
	
	
}
