package main.java.frontend.rightpanel;

import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.java.frontend.user.FavouritesDBManager;
import main.java.frontend.user.LoginToMainFrame;
import main.java.frontend.utilities.ListToScrollConverter;

/**
 * The Class FavouritesPanel displays all of the 
 * stops and lines saved by the user in a j scroll pane.
 */
public class FavouritesPanel extends JPanel{

	/** The {@link JScrollPane} containing the favourites stops. */
	private JScrollPane favStops;
	
	/** True if the "no favourites" label was added to the panel. */
	private boolean labelPresent = false;
	
	
	/**
	 * Instantiates a new favourites panel.
	 */
	public FavouritesPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		addLabel();
		
	}
	
	/**
	 * Retrieves the favourites from the {@link FavouritesDBManager} and then
	 * adds them to a {@link JScrollPane}.
	 */
	public void showFavourites() {
		List<JPanel> favs = FavouritesDBManager.getFavourites(LoginToMainFrame.getCurrentUser());
		if (favStops != null) { this.remove(favStops); }
		if (!favs.isEmpty()) {
			JScrollPane x = ListToScrollConverter.setContent(favs);
			favStops = x;
			add(x);
		}
		else {
			if (!labelPresent) {
			JLabel l = new JLabel("Non hai ancora salvato preferiti!");
			l.setAlignmentX(CENTER_ALIGNMENT);
			add(l);
			labelPresent = true;
			} 
		}
		
		repaint();
		revalidate();
	}
	
	
	
	/**
	 * Adds the header label.
	 */
	private void addLabel() {
		JLabel lab = new JLabel("Le tue fermate e linee preferite : ", JLabel.CENTER);
		lab.setFont(new Font("Monsterrat", Font.BOLD, 20));
		lab.setAlignmentX(CENTER_ALIGNMENT);
		
		add(lab);
	}
	

	
	
}
