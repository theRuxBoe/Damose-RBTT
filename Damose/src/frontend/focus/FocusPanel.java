package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import backendNOTPUSH.Entity;
import frontend.focus.entities.BusPanel;
import frontend.focus.entities.BusStopFocus;
import frontend.focus.entities.BusStopPanel;
import frontend.focus.entities.EntitiesPanelFactory;
import frontend.focus.entities.LinePanel;
import frontend.main.FocusController;
import frontend.main.RightPanel;

public class FocusPanel extends JPanel {
	
	private JPanel previous;
	private RightPanel rp;
	
	public FocusPanel(JPanel p) {
		super();
		addBackButton();
		addLabel();
		add(p);
	}
	
	private void addBackButton() {
		JButton b = new JButton(" GO BACK");
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				rp.setShowCurrent(previous);
				FocusController.openPrevious();
			}
		});
		add(b);
	}

	public void setRightPanel(RightPanel r) {
		if (rp == null) {
			this.rp = r;
		}
		
	}
	
	private void addLabel() {
		JLabel label = new JLabel("You have selected : ", JLabel.CENTER);
		
		this.add(label, BorderLayout.NORTH);
	}

	
	
	public void setPrevious(JPanel p) {
		previous = p;
	}
	
	public JPanel getPrevious() {
		return previous;
	}
	
}
