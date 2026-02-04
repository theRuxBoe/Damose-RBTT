package frontend.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import frontend.LoginToMainFrame;
import frontend.MainFrame;
import frontend.focus.FavouritesPanel;
import frontend.focus.FocusPanel;
import frontend.focus.SearchPanel;

public class RightPanel extends JPanel{

	private SearchPanel search;
	private FavouritesPanel fav;
	private FocusPanel focusPanel;
	private JPanel current;
	private MainFrame f;
	
	public RightPanel(MainFrame m) {
		super();
		setBackground(new Color(0x7851a9));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500,100));
		setBorder(new BevelBorder(BevelBorder.RAISED));
		f = m;
		addTopButton();
		FocusController.setRightPanel(this);
//		FocusController.setFocusPanel(new FocusPanel());
	}
	
	private void addTopButton() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		if (MainFrame.isLogged()) {
			JButton favButton = new JButton("Search Bus");
			favButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (favButton.getText() == "Search Bus") {
					 	favButton.setText("Favourites");
					}
					else {
						favButton.setText("Search Bus");
					}
					f.switchCurrentRightPanel();
				}
			});
			p.add(favButton);
		}
		else {
			JButton logButton = new JButton("Login");
			logButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					LoginToMainFrame.openLogin(f);
					repaint();
					revalidate();
			}
		});
		p.add(logButton);
		}
		this.add(p, BorderLayout.NORTH);
	}
	
		
	public JPanel getCurrent() {
		return current;
	}
	
	public FocusPanel getFocus() {
		return focusPanel;
	}
	
	
	public void openSearchPanel() {
 		if (search == null) {
 			SearchPanel search = new SearchPanel();
 			this.search = search;
 		}
 		
 		setShowCurrent(search);
 		
 	}
	
 	public void openFavouritePanel() {
 		if (fav == null ) {
 			FavouritesPanel fpanel = new FavouritesPanel();
 			fav = fpanel;
 			}
 		setShowCurrent(fav);
 		}
 	
 	public void openFocusPanel(JPanel p) {
 		if (focusPanel == null) {
 			FocusPanel foc = new FocusPanel(p);
 			focusPanel = foc;
 			foc.setRightPanel(this);
 		}
 		focusPanel.setPrevious(current);
 		
 		setShowCurrent(focusPanel);
 	}
 	
	
 	public void setShowCurrent(JPanel p) {
 		
 		if (current != null) {
 			this.remove(current);
 		}
 		current = p;
 		this.add(current, BorderLayout.CENTER);
 		repaint();
 		revalidate();
 		
 	}
 	
 	
}
