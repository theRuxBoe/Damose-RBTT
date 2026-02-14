package main.java.frontend.rightpanel.panels;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


import main.java.backend.model.Fermata;
import main.java.frontend.rightpanel.FocusController;
import main.java.frontend.user.FavouritesDBManager;
import main.java.frontend.user.LoginToMainFrame;


/**
 * The Class StopPanel displays a {@link Fermata} object
 * and allows the user to open a {@link StopFocus} from it
 * to see the next arriving public transport vehicle.
 */
public class StopPanel extends JPanel {


	
	/** The {@link Fermata} object. */
	private Fermata stop;
	
	
	/**
	 * Instantiates a new stop panel from a given {@link Fermata}.
	 *
	 * @param f the stop
	 */
	public StopPanel(Fermata f) {
		super();
		if (f != null) {
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		stop = f;
		addDataLabels();
		addListener();
		setMaximumSize(getPreferredSize());
		setAlignmentX(Component.LEFT_ALIGNMENT);
		}
	}
	
	
	
	/**
	 * Adds the mouse-click listener which opens the {@link StopFocus}
	 * through the {@link FocusController}. 
	 */
	private void addListener() {
		addMouseListener(new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}			
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {
					StopFocus bsfocus = new StopFocus(StopPanel.this.getStop());
					FocusController.openFocus(bsfocus);
					bsfocus.showOnMap();
					bsfocus.showVehiclesOnMap();
				
			}
		});
	}

	
	

	
	/**
	 * Adds the stop's data (name and id) to a label and the favourite button if a
	 * user is logged in.
	 */
	private void addDataLabels() {
		JPanel labpane = new JPanel();
		if (LoginToMainFrame.isLogged()) {
			labpane.add(createFavouriteButton());
		}
		JLabel data = new JLabel(stop.getName() + " - " + stop.getStopId(), JLabel.LEFT);
		data.setFont(new Font("Serif", Font.PLAIN, 30));
		labpane.add(data);
		
		labpane.setMaximumSize(getPreferredSize());
		add(labpane);
		
	}
	
	/**
	 * Creates the "add to favourites" button.
	 *
	 * @return the "add to favourites" button
	 */
	private JButton createFavouriteButton() {
		String s = FavouritesDBManager.isPresent(stop) ? "★" : "☆";
		JButton b = new JButton(s);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setSize(new Dimension(10,10));
		b.addActionListener(e -> {
				if (b.getText().equals("☆")) {
					b.setText("★");
					FavouritesDBManager.addToFavourites(stop);
//					this will put the stop into the favourites area
				}
				else {
					b.setText("☆");
//					this will kick the stop from the favourites
					FavouritesDBManager.remove(stop);
				}
			
		});
		return b;
		
	}
	

	/**
	 * Gets the {@link Fermata} object.
	 *
	 * @return the stop
	 */
	public Fermata getStop() {
		return stop;
	}
}
