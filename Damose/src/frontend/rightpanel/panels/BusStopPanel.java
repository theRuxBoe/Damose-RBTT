package frontend.rightpanel.panels;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jxmapviewer.viewer.GeoPosition;

import backend.model.Fermata;
import backend.model.RisultatoFermata;
import frontend.main.MainFrame;
import frontend.main.MapPanel;
import frontend.rightpanel.FocusController;
import frontend.user.FavouritesDBManager;
import frontend.waypoints.BusWaypoint;
import frontend.waypoints.WaypointManager;


public class BusStopPanel extends JPanel {


	
	private Fermata stop;
	
	
	public BusStopPanel(Fermata bs) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		
		stop = bs;
		addLabelsData();
		addListener();
		setMaximumSize(getPreferredSize());
//		setAlignmentX(Component.LEFT_ALIGNMENT);
		
	}
	
	public BusStopPanel(RisultatoFermata rf) {
		this(MainFrame.getTTS().getFermataById(rf.getStopId()).get());
	}
	
	private void addListener() {
		addMouseListener(new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}			
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {
					BusStopFocus bsfocus = new BusStopFocus(BusStopPanel.this.getStop());
					FocusController.openFocus(bsfocus);
				
			}
		});
	}

	
	

	
	private void addLabelsData() {
		JPanel labpane = new JPanel();
		JLabel data = new JLabel(stop.getName() + " - " + stop.getStopId(), JLabel.LEFT);
		data.setFont(new Font("Serif", Font.PLAIN, 30));
		labpane.add(data);
		if (MainFrame.isLogged()) {
			labpane.add(createFavouriteButton());
		}
		labpane.setMaximumSize(getPreferredSize());
		add(labpane);
		
	}
	
	private JButton createFavouriteButton() {
		String s = "☆";
		JButton b = new JButton(s);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setSize(new Dimension(10,10));
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getText() == "☆") {
					b.setText("★");
					FavouritesDBManager.addToFavourites(stop);
//					this will put the stop into the favourites area
				}
				else {
					b.setText("☆");
//					this will kick the stop from the favourites
				}
			}
		});
		return b;
		
	}
	

	public Fermata getStop() {
		return stop;
	}
}
