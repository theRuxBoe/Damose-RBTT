package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

public class MainFrame extends JFrame {

	private JFrame frame;
	private JPanel mapPanel;
	private JPanel login;
	private boolean loginIsVisible = false;
	
	public MainFrame() {
		
	}
	private void addLoginButton() {
		JButton b = new JButton("Login");
		ActionListener l = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				login.setVisible(!loginIsVisible);
				loginIsVisible = !loginIsVisible;
			}
		};
		b.addActionListener(l);
		frame.add(b);
		
	}
	
	
//	the search panel will arrive from its class already split in two panels
//	to show the main search panel and the focused one
	
 	public void setFrame() {
// 		TODO add every panel to a field in this class
		JFrame f = new JFrame("Damose");
		JLayeredPane basePanel = new JLayeredPane();
		
		this.frame = f;
		
		JPanel mapPanel = new JPanel();
		SearchFocusPanel searchPanel = new SearchFocusPanel();
		JPanel servicePanel = new JPanel();
//		JPanel focusPanel = new JPanel();
		
		frame.add(basePanel);
		
		searchPanel.createPanels();
		
		mapPanel.setBorder(new BevelBorder(1));
		searchPanel.setBorder(new BevelBorder(0));
		servicePanel.setBorder(new BevelBorder(0));
//		focusPanel.setBorder(new BevelBorder(1));
		
		basePanel.setLayout(new BorderLayout());
		
		mapPanel.setPreferredSize(new Dimension(500,500));
		searchPanel.setPreferredSize(new Dimension(300,500));
		
		
		basePanel.add(mapPanel, BorderLayout.CENTER);
		basePanel.add(searchPanel, BorderLayout.EAST);
		basePanel.add(servicePanel, BorderLayout.WEST);
//		basePanel.add(focusPanel, BorderLayout.SOUTH);
		
		
		
		
		
		
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	

 	
	
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
		
		m.setFrame();
		
//		m.frame.add(new LoginPanel());
//		m.frame.setVisible(true);
	}
}
