package frontend;

//TODO promemoria : separare dalla classe mainframe tutta la parte di dialogo con il loginpanel

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import frontend.focus.FavouritesPanel;
import frontend.focus.SearchFocusPanel;
import frontend.news.ServicePanel;
import frontend.user.LoginPanel;

public class MainFrame extends JFrame {

	private static boolean logged;
	private SearchFocusPanel foc;
	private FavouritesPanel fav;
	
	public MainFrame() {
		super("Damose");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		openLogin();
		
		pack();
		setVisible(true);		
	}
	
	
	public void update(boolean b) {
		logged = b;
		createDefaultPanels();
		if (b) {
			openFavouritePanel();
		}
		else {
			openFocusPanel();
		}
		
			
	}
	
	private void openLogin() {
		LoginPanel l = new LoginPanel();
		add(l, BorderLayout.CENTER);
		l.addObserver(this);
	}
	
	
 	private void createDefaultPanels() {
		JLayeredPane basePanel = new JLayeredPane();
		basePanel.setLayout(new BorderLayout());
		
		Map mapPanel = new Map();			
		ServicePanel servicePanel = new ServicePanel();
		
		add(basePanel, BorderLayout.CENTER);
		basePanel.add(mapPanel, BorderLayout.CENTER);
		basePanel.add(servicePanel, BorderLayout.WEST);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
	}
 	
 	private void setCurrentRightPanel(JPanel p) {
 		this.remove(getBasePanel());
 		this.add(p);
 		
 		
 	}
 	
 	private void openFocusPanel() {
 		if (foc == null) {
 		SearchFocusPanel focusPanel = new SearchFocusPanel();
 		foc = focusPanel;
 		}
 		setCurrentRightPanel(foc);
 		
 	}
	
 	private void openFavouritePanel() {
 		if (fav == null) {
 		FavouritesPanel fpanel = new FavouritesPanel();
 		fav = fpanel;
 		}
 		setCurrentRightPanel(fav);
 	}
 	
 	private void addSwitcherButton() {
 		
 	}
 	
 		
 	public static boolean isLogged() {
 		return logged;
 	}
 	
 	private JLayeredPane getBasePanel() {
 		return (JLayeredPane) this.getComponent(0);
 	}
 	
 		
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
	}
}
