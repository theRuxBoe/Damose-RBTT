package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class SearchFocusPanel extends JPanel{

	private FocusPanel focus;
	private SearchPanel search;
	
	public SearchFocusPanel() {
		// TODO Auto-generated constructor stub
	} 
	
	public JPanel createPanels() {
		FocusPanel focus = new FocusPanel();
		SearchPanel search = new SearchPanel();
		this.focus = focus;
		this.search = search;
		
		search.setBorder(new BevelBorder(0)); 
		
		
		search.setPreferredSize(new Dimension(500,500));
		
		this.setLayout(new BorderLayout());
		
		this.add(search, BorderLayout.NORTH);
		this.add(focus, BorderLayout.SOUTH);
		
		return this;
		
	}

	public FocusPanel getFocus() {
		return focus;
	}

	public SearchPanel getSearch() {
		return search;
	}
	
	
	
}
