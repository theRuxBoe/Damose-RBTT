package frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.model.Fermata;
import backend.user.User;
import backend.user.UserDB;
import frontend.main.MainFrame;
import frontend.user.FavouritesDBManager;
import frontend.utilities.ListToScrollConverter;

public class FavouritesPanel extends JPanel{

	private List<Fermata> favStops;
	
	
	public FavouritesPanel() {
		super();
		setLayout(new BorderLayout());
		addLabel();
		
		
		
	}
	public void showFavourites() {
		JScrollPane x = ListToScrollConverter.setContent(FavouritesDBManager.getFavourites(MainFrame.getCurrentUser()));
		if (x != null) {
			this.add(x, BorderLayout.CENTER);
		}
		
		
	}
	
	
	
	private void addLabel() {
		JLabel lab = new JLabel("Your Stops : ", JLabel.CENTER);
		lab.setFont(new Font("Monsterrat", Font.BOLD, 20));
		add(lab, BorderLayout.NORTH);
	}
	

	
	
}
