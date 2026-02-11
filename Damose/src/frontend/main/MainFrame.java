package frontend.main;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import backend.service.TransitService;
import backend.service.TransitServiceImpl;
import frontend.news.ServicePanel;
import frontend.rightpanel.RightPanel;
import frontend.rightpanel.SearchPanel;
import frontend.user.LoginToMainFrame;

public class MainFrame extends JFrame {

	private static boolean logged;
	private static TransitServiceImpl tts;
	private RightPanel rightPanel;
	private JPanel basePanel;
	private JPanel cardPanel;
	private CardLayout cardLayout = new CardLayout();
	
	private static String u;
	
	
	public MainFrame() {
		super("Damose - Rome Bus Transit Tracker");
//		setLocationRelativeTo(null);
		
		JPanel p = new JPanel(cardLayout);
		cardPanel = p;
		add(p);
		
		openTransit();
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		createDefaultPanels();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIcon();
		
		
		
		LoginToMainFrame.openLogin(this);
		checkConnection();
		
		setVisible(true);	
	}
	
	private void openTransit() {
		try {
			tts = TransitServiceImpl.createDefault();
		} catch (IOException e) {
			int x = JOptionPane.showConfirmDialog(null, e.getMessage() + "Do you wish to try again?");
			if (x == 0) {
				openTransit();
			}
		}
	}
	
	public static TransitServiceImpl getTTS() {
		return tts;
	}
	
	private void checkConnection() {
		if (!getTTS().isOnline()) {
			JOptionPane.showMessageDialog(this, "Sei offline!");
		}
	}
	
	
	public void update(boolean log, String user) {
		logged = log;
		u = user;
		if (rightPanel != null) {
			basePanel.remove(rightPanel);
		}
		
		
		RightPanel rightPanel = new RightPanel(this);
		this.rightPanel = rightPanel;
		basePanel.add(rightPanel, BorderLayout.EAST);
		
		
		cardLayout.show(cardPanel, "Main Panel");
		
		repaint();
		revalidate();
		if (log) { 
			
			rightPanel.openFavouritePanel();
		}
		else { 
			
			rightPanel.openSearchPanel();
		}
		
	}
	
	private void setIcon() {
		ImageIcon img;
		if (System.getProperty("os.name").startsWith("Windows")) {
			img = new ImageIcon("src/res/bus.ico");
		}
		else {	
			img = new ImageIcon("src/res/busIcon.png");
		}
		setIconImage(img.getImage());
	}

	public JPanel getCardPanel() {
		return	cardPanel;	}
	
	private void addBasePanel() {
		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BorderLayout());
		this.basePanel = mPanel;
		cardPanel.add(mPanel, "Main Panel");
	}
	
 	private void createDefaultPanels() {
 		
 		addBasePanel();
		
		MapPanel mapPanel = new MapPanel();
		ServicePanel servicePanel = new ServicePanel();
		basePanel.add(servicePanel, BorderLayout.WEST);
		basePanel.add(mapPanel, BorderLayout.CENTER);
		
	}
 	
 	public JPanel getBasePanel() {
 		return basePanel;
 	}
 	
 	public void switchCurrentRightPanel() {
 		
 		if (rightPanel.getCurrent() instanceof SearchPanel) {
 				rightPanel.openFavouritePanel();
 		}
 		else {
 			rightPanel.openSearchPanel();
 		}
 		repaint();
 		revalidate();
 		
 	}
 	
 	public CardLayout getCardLayout() {
 		return cardLayout;
 	}

 	public static String getCurrentUser() {
 		return u;
 	}
 		
 	public static boolean isLogged() {
 		return logged;
 	}

 	
 		
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
	}
}
