package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import backendDONTPUSH.*;

public class FocusPanel extends JPanel {
	
	private JPanel current;
	
	public FocusPanel() {
		super();
		this.setLayout(new BorderLayout());
//		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(50,350));
		JLabel label = new JLabel("You have selected : ");
//		label.setSize(new Dimension(50,30));
		this.add(label, BorderLayout.NORTH);
//		setBus(new Bus());
		
	}

	
	public void setBus(Bus b) {
		
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
		this.current = new BusPanel(b);
		this.add(current);
		current.setVisible(true);
	}
	

	public void setStop(BusStop s) {
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
		this.current = new BusStopPanel(s);
		this.add(current);
		current.setVisible(true);
	}
	
	public void setLine(Line l) {
		if (current != null) {
			current.setVisible(false);
			this.clear();
		}
		this.current = new LinePanel(l);
		this.add(current);
		current.setVisible(true);
	}
	

	
	public void clear() {
		current = null;
	}
	
	
	
}
