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

	/** The {@link JScrollPane} containing the favourites. */
	private JScrollPane favourites;
	
	/** The label telling the user they haven't added any favourites. */
	private JLabel label;
	
	
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
		if (favourites != null) { this.remove(favourites); }
		if (!favs.isEmpty()) {
			JScrollPane x = ListToScrollConverter.setContent(favs);
			favourites = x;
			x.setAlignmentX(CENTER_ALIGNMENT);
			add(x);
			if (label != null ) {
				remove(label);
				label = null;
			}
		}
		else {
			
			if (label == null) {
				JLabel l = new JLabel("Non hai ancora salvato preferiti!");
				l.setAlignmentX(CENTER_ALIGNMENT);
				label = l;
				add(label);
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
