package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import backend.model.Fermata;
import frontend.ScrollablePanel;

public class FavouritesPanel extends ScrollablePanel{

	private List<Fermata> favStops;
	
	
	public FavouritesPanel() {
		super();
		setLayout(new BorderLayout());
		addLabel();
		
//		showFavourites();
//		setPreferredSize(new Dimension(300,1000));
		
//		getting favourites from database
//		favourites = ....
//		setContent(favourites);
//		addFavourites();
		
	}
	private void showFavourites() {
//		updates the list from the backend
		this.add(setContent(convertList2(favStops)), BorderLayout.CENTER);
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
