package frontend.focus;

<<<<<<< Updated upstream
=======
import java.awt.BorderLayout;
>>>>>>> Stashed changes
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

<<<<<<< Updated upstream
=======
import org.jxmapviewer.viewer.GeoPosition;

import backendDONTPUSH.*;

>>>>>>> Stashed changes
public class FocusPanel extends JPanel {
	
	private JPanel current;
	
	public FocusPanel() {
		super();
<<<<<<< Updated upstream
		this.setLayout(null);	// TODO da sistemare
		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(500,300));
		this.add(new JLabel("You have selected :"));
		createPanel();
		
	}

	
//	TODO enter the input type for shoBus, it should be something like a JPanel
=======
		this.setLayout(new BorderLayout());
//		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(50,350));
		JLabel label = new JLabel("You have selected : ");
//		label.setSize(new Dimension(50,30));
		this.add(label, BorderLayout.NORTH);
//		setBus(new Bus());
	}

	
>>>>>>> Stashed changes
	public void setBus(Bus b) {
		
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
<<<<<<< Updated upstream
		BusPanel panel = new BusPanel(b);
		this.current = panel;
=======
		this.current = new BusPanel(b);
		this.add(current);
>>>>>>> Stashed changes
		current.setVisible(true);
	}
	

<<<<<<< Updated upstream
//	TODO same as above except for bus stops
=======
>>>>>>> Stashed changes
	public void setStop(BusStop s) {
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
<<<<<<< Updated upstream
		BusStopPanel stop = new BusStopPanel(s.getPosition());
		this.current = stop;
=======
		this.current = new BusStopPanel(s);
		this.add(current);
>>>>>>> Stashed changes
		current.setVisible(true);
	}
	
	public void setLine(Line l) {
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
<<<<<<< Updated upstream
		LinePanel panel = new LinePanel(l);
		this.current = panel;
		current.setVisible(true);
	}
	
//	I would need a specific class for buses and bus stops, with both of them exposing methods 
//	to get their infos, 
	
//	TODO triggered by an x button when the user selects another bus/bus stop
=======
		this.current = new LinePanel(l);
		this.add(current);
		current.setVisible(true);
	}
	

	
>>>>>>> Stashed changes
	public void clear() {
		current = null;
	}
	
	
	
}
