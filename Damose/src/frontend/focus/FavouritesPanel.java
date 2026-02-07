package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import backendNOTPUSH.BusStop;
import frontend.ScrollablePanel;
import frontend.main.RightPanel;

public class FavouritesPanel extends ScrollablePanel{

	private ArrayList<BusStop> favStops = new ArrayList<BusStop>();
	
	
	public FavouritesPanel() {
		super();
		setLayout(new BorderLayout());
		addLabel();
		
		showFavourites();
//		setPreferredSize(new Dimension(300,1000));
		
//		getting favourites from database
//		favourites = ....
//		setContent(favourites);
//		addFavourites();
		
	}
	private void showFavourites() {
//		updates the list from the backend
		this.add(setContent(convertList(favStops)), BorderLayout.CENTER);
	}
	
	private void addLabel() {
		JLabel lab = new JLabel("Your Stops : ", JLabel.CENTER);
		lab.setFont(new Font("Monsterrat", Font.BOLD, 20));
		add(lab, BorderLayout.NORTH);
	}
	
//	private void addFavourites() {
////		calls the backend to output favourites
//	}
	
	
}
