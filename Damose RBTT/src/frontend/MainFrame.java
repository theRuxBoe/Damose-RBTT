package frontend;

<<<<<<< Updated upstream
=======
//TODO add setimageIcon to put a small logo on the frame

>>>>>>> Stashed changes
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
<<<<<<< Updated upstream
=======
import java.util.List;
>>>>>>> Stashed changes

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

<<<<<<< Updated upstream
=======
import org.jxmapviewer.JXMapViewer;

import frontend.focus.FavouritesPanel;
>>>>>>> Stashed changes
import frontend.focus.SearchFocusPanel;

public class MainFrame extends JFrame {

	private JFrame frame;
<<<<<<< Updated upstream
	private JPanel mapPanel;
	private JPanel login;
	private boolean loginIsVisible = false;
	
	public MainFrame() {
		
	}
//	private void addLoginButton() {
//		JButton b = new JButton("Login");
//		ActionListener l = new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				login.setVisible(!loginIsVisible);
//				loginIsVisible = !loginIsVisible;
//			}
//		};
//		b.addActionListener(l);
//		frame.add(b);
//		
//	}
=======
	private static JPanel mapPanel;
	private JPanel login;
	private static boolean logged;
	
	public MainFrame() {
		createBaseFrame();
	}
>>>>>>> Stashed changes
	
	
//	the search panel will arrive from its class already split in two panels
//	to show the main search panel and the focused one
	
<<<<<<< Updated upstream
 	public void setFrame() {
=======
	private void addSwitchButton() {
		JButton b = new JButton("search");
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				b.setText("prefe");
				getFrame().add(new SearchFocusPanel(), BorderLayout.EAST);
				getFrame().repaint();
				
			}
		});
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
 	private void createBaseFrame() {
>>>>>>> Stashed changes
// 		TODO add every panel to a field in this class
		JFrame f = new JFrame("Damose");
		JLayeredPane basePanel = new JLayeredPane();
		
		this.frame = f;
		
<<<<<<< Updated upstream
		JPanel mapPanel = new JPanel();
		SearchFocusPanel searchPanel = new SearchFocusPanel();
=======
		JPanel mapPanel = new Map();
		FavouritesPanel favouritePanel = new FavouritesPanel();
>>>>>>> Stashed changes
		JPanel servicePanel = new JPanel();
//		JPanel focusPanel = new JPanel();
		
		frame.add(basePanel);
<<<<<<< Updated upstream
		
		searchPanel.createPanels();
		
		mapPanel.setBorder(new BevelBorder(1));
		searchPanel.setBorder(new BevelBorder(0));
=======
				
		mapPanel.setBorder(new BevelBorder(1));
		favouritePanel.setBorder(new BevelBorder(0));
>>>>>>> Stashed changes
		servicePanel.setBorder(new BevelBorder(0));
//		focusPanel.setBorder(new BevelBorder(1));
		
		basePanel.setLayout(new BorderLayout());
		
		mapPanel.setPreferredSize(new Dimension(500,500));
<<<<<<< Updated upstream
		searchPanel.setPreferredSize(new Dimension(300,500));
		
		
		basePanel.add(mapPanel, BorderLayout.CENTER);
		basePanel.add(searchPanel, BorderLayout.EAST);
=======
		favouritePanel.setPreferredSize(new Dimension(300,500));
		servicePanel.setPreferredSize(new Dimension(100,200));
		
		
		basePanel.add(mapPanel, BorderLayout.CENTER);
		basePanel.add(favouritePanel, BorderLayout.EAST);
>>>>>>> Stashed changes
		basePanel.add(servicePanel, BorderLayout.WEST);
//		basePanel.add(focusPanel, BorderLayout.SOUTH);
		
		
		
		
		
		
<<<<<<< Updated upstream
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
=======
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
>>>>>>> Stashed changes
		frame.setVisible(true);
	}
	

<<<<<<< Updated upstream
=======
 	public static boolean isLogged() {
 		return logged;
 	}
 	
 
 
>>>>>>> Stashed changes
 	
	
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
		
<<<<<<< Updated upstream
		m.setFrame();
=======
//		m.setFrame();
		
>>>>>>> Stashed changes
		
//		m.frame.add(new LoginPanel());
//		m.frame.setVisible(true);
	}
}
