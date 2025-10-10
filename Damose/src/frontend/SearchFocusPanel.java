package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class SearchFocusPanel extends JPanel{

	public SearchFocusPanel() {
		// TODO Auto-generated constructor stub
	} 
	
	public JPanel createPanels() {
		BusStopFocusPanel focus = new BusStopFocusPanel();
		BusSearchPanel search = new BusSearchPanel();
		
		focus.setBorder(new BevelBorder(1));
		search.setBorder(new BevelBorder(0)); 
		
		focus.setPreferredSize(new Dimension(500,300));
		search.setPreferredSize(new Dimension(500,500));
		
		this.setLayout(new BorderLayout());
		
		this.add(search, BorderLayout.NORTH);
		this.add(focus, BorderLayout.SOUTH);
		
		return this;
		
	}
	
}
