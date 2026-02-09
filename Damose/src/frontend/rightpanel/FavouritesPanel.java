package frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.model.Fermata;
import frontend.utilities.ListToScrollConverter;

public class FavouritesPanel extends JPanel{

	private List<Fermata> favStops;
	
	
	public FavouritesPanel() {
		super();
		setLayout(new BorderLayout());
		addLabel();
		
//		setPreferredSize(new Dimension(300,1000));
		
//		getting favourites from database
//		favourites = ....
//		setContent(favourites);
//		addFavourites();
		
	}
	public void showFavourites() {
//		updates the list from the backend
		JScrollPane p = ListToScrollConverter.setContent(ListToScrollConverter.convertList2(favStops));
		
		this.add(p, BorderLayout.CENTER);
	}
	
	
	
	private void addLabel() {
		JLabel lab = new JLabel("Your Stops : ", JLabel.CENTER);
		lab.setFont(new Font("Monsterrat", Font.BOLD, 20));
		add(lab, BorderLayout.NORTH);
	}
	

	
	
}
