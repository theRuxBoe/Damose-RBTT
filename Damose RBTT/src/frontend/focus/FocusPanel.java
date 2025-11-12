package frontend.focus;

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
		createPanel();
		
	}

	
//	TODO enter the input type for shoBus, it should be something like a JPanel
	public void setBus(Bus b) {
		
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
		BusPanel panel = new BusPanel(b);
		this.current = panel;
		current.setVisible(true);
	}
	

//	TODO same as above except for bus stops
	public void setStop(BusStop s) {
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
		BusStopPanel stop = new BusStopPanel(s.getPosition());
		this.current = stop;
		current.setVisible(true);
	}
	
	public void setLine(Line l) {
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
		LinePanel panel = new LinePanel(l);
		this.current = panel;
		current.setVisible(true);
	}
	
//	I would need a specific class for buses and bus stops, with both of them exposing methods 
//	to get their infos, 
	
//	TODO triggered by an x button when the user selects another bus/bus stop
	public void clear() {
		current = null;
	}
	
	
	
}
