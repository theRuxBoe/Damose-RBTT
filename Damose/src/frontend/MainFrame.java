package frontend;


import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import frontend.focus.SearchPanel;
import frontend.main.RightPanel;
import frontend.news.ServicePanel;

public class MainFrame extends JFrame {

	private static boolean logged;
	private RightPanel rightPanel;
	private boolean panelsCreated = false;
	private JPanel basePanel;
	
	
	
	public MainFrame() {
		super("Damose - Rome Bus Transit Tracker");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIcon();
		
		LoginToMainFrame.openLogin(this);
		
		
		pack();
		setVisible(true);	
	}
	
	
	public void update(boolean log) {
		logged = log;
		if (!panelsCreated) {
			panelsCreated = true;
			createDefaultPanels();
			
			
			
		}
		if (rightPanel != null) {
//			rightPanel.remove(rightPanel.getCurrent());
			basePanel.remove(rightPanel);
		}
		
		RightPanel rightPanel = new RightPanel(this);
		this.rightPanel = rightPanel;
		basePanel.add(rightPanel, BorderLayout.EAST, 0);
		
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
			img = new ImageIcon("res/bus.ico");
		}
		else {	
			img = new ImageIcon("res/bus.png");
		}
		
		setIconImage(img.getImage());
	}


	
	
 	private void createDefaultPanels() {
 		JPanel basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());
		this.basePanel = basePanel;
		add(basePanel, BorderLayout.CENTER);
 		
		
		Map mapPanel = new Map();			
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
 	
 	

 		
 	public static boolean isLogged() {
 		return logged;
 	}

 	
 		
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
	}
}
