package main.frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import main.frontend.focus.entities.BusPanel;
import main.frontend.focus.entities.BusStopPanel;
import main.frontend.focus.entities.EntitiesPanelFactory;
import main.frontend.focus.entities.LinePanel;

public class FocusPanel extends JPanel {
	
	private JPanel current;
	
	public FocusPanel() {
		super();
		this.setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setPreferredSize(new Dimension(300,500));
		//		this.setBorder(new BevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(50,350));
		JLabel label = new JLabel("You have selected : ");
//		label.setSize(new Dimension(50,30));
		this.add(label, BorderLayout.NORTH);

	}


	
	public void setFocusFromBackend(Object o) {		//just a placeholder, this will accept the superclass for bus, lines and stops
//													we need a factory pattern which decides what to instantiate based on the type of the object given
		if (current != null) {
			this.clear();
		}
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		current = epf.createPanel(o);
		this.add(current);
		repaint();
	}
	
	public void setFocusFromPanel(JPanel p) {
		if (current != null) {
			this.clear();
		}
		this.current = p;
		this.add(current);
//		current.setVisible(true);
		repaint();
	}
	
	
	public void clear() {
		current = null;
	}
	
	
	
}
