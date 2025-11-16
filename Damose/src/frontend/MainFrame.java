package frontend;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

import org.jxmapviewer.JXMapViewer;

import frontend.focus.FavouritesPanel;
import frontend.focus.SearchFocusPanel;

public class MainFrame extends JFrame {

	private JFrame frame;
	private static JPanel mapPanel;
	private JPanel login;
	private static boolean logged;
	
	public MainFrame() {
		createBaseFrame();
	}
	
	
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

		JFrame f = new JFrame("Damose");
		JLayeredPane basePanel = new JLayeredPane();
		
		this.frame = f;
		
		JPanel mapPanel = new Map();
		FavouritesPanel favouritePanel = new FavouritesPanel();
		JPanel servicePanel = new JPanel();
//		JPanel focusPanel = new JPanel();
		
		frame.add(basePanel);
				
		mapPanel.setBorder(new BevelBorder(1));
		favouritePanel.setBorder(new BevelBorder(0));
		servicePanel.setBorder(new BevelBorder(0));
//		focusPanel.setBorder(new BevelBorder(1));
		
		basePanel.setLayout(new BorderLayout());
		
		mapPanel.setPreferredSize(new Dimension(500,500));
		favouritePanel.setPreferredSize(new Dimension(300,500));
		servicePanel.setPreferredSize(new Dimension(100,200));
		
		
		basePanel.add(mapPanel, BorderLayout.CENTER);
		basePanel.add(favouritePanel, BorderLayout.EAST);
		basePanel.add(servicePanel, BorderLayout.WEST);
//		basePanel.add(focusPanel, BorderLayout.SOUTH);
		
		
		
		
		
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
	}
	

 	public static boolean isLogged() {
 		return logged;
 	}
 	
 
 
 	
	
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
	}
}
