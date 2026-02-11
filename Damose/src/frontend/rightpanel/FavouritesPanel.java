package frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
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

	private JScrollPane favStops;
	
	
	public FavouritesPanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		addLabel();
		
	}
	public void showFavourites() {
		List<JPanel> favs = FavouritesDBManager.getFavourites(MainFrame.getCurrentUser());
		if (favStops != null) { this.remove(favStops); }
		if (!favs.isEmpty()) {
			JScrollPane x = ListToScrollConverter.setContent(favs);
			favStops = x;
			add(x);
		}
		else {
			JLabel l = new JLabel("Non hai ancora salvato preferiti!");
			l.setAlignmentX(CENTER_ALIGNMENT);
			add(l);
		}
		
		repaint();
		revalidate();
	}
	
	
	
	private void addLabel() {
		JLabel lab = new JLabel("Le tue fermate e linee preferite : ", JLabel.CENTER);
		lab.setFont(new Font("Monsterrat", Font.BOLD, 20));
//		lab.setMaximumSize(getPreferredSize());
		add(lab);
	}
	

	
	
}
