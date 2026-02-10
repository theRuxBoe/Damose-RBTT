package frontend.main;


import java.awt.BorderLayout;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
	private boolean panelsCreated = false;
	private JPanel basePanel;
//	private static boolean online;
	private static String u;
	
	
	public MainFrame() {
		super("Damose - Rome Bus Transit Tracker");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIcon();
		
		
		
		openTransit();
		LoginToMainFrame.openLogin(this);
		
		
		pack();
		setVisible(true);	
	}
	
	private void openTransit() {
		try {
			tts = TransitServiceImpl.createDefault();
		} catch (IOException e) {
			int x = JOptionPane.showConfirmDialog(basePanel, e.getMessage() + "Do you wish to try again?");
			if (x == 0) {
				openTransit();
			}
		}
	}
	
	public static TransitServiceImpl getTTS() {
		return tts;
	}
	
//	public static boolean isConnected(){
//		return online;
//	}
	
	public void update(boolean log, String user) {
		logged = log;
		u = user;
		if (!panelsCreated) {
			panelsCreated = true;
			createDefaultPanels();
			
			
			
		}
		if (rightPanel != null) {
			basePanel.remove(rightPanel);
		}
		
		RightPanel rightPanel = new RightPanel(this);
		this.rightPanel = rightPanel;
		basePanel.add(rightPanel, BorderLayout.EAST);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		
		
		
		if (log) { 
			
			rightPanel.openFavouritePanel();
		}
		else { 
			
			rightPanel.openSearchPanel();
		}
		
		repaint();
		revalidate();
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


	
	
 	private void createDefaultPanels() {
 		JPanel basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		this.basePanel = basePanel;
		add(basePanel, BorderLayout.CENTER);
 		
		
		MapPanel mapPanel = new MapPanel();
		ServicePanel servicePanel = new ServicePanel();
		
		
		
		
		basePanel.add(mapPanel, BorderLayout.CENTER, 0);
		basePanel.add(servicePanel, BorderLayout.WEST, 0);
		
		
		
		
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
