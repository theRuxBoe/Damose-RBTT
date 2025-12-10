package main.frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class SearchFocusPanel extends JPanel{

	private static FocusPanel focus;
	private SearchPanel search;
	
	public SearchFocusPanel() {
		super();
		addPanels();
	} 
	
	public void addPanels() {
		FocusPanel focus = new FocusPanel();
		SearchPanel search = new SearchPanel();
		this.focus = focus;
		this.search = search;
		search.setBorder(new BevelBorder(0)); 
		this.setLayout(new BorderLayout());
		this.add(search, BorderLayout.NORTH);
		this.add(focus, BorderLayout.SOUTH);
	}

	
	public static FocusPanel getFocus() {
		return focus;
	}

	
	public SearchPanel getSearch() {
		return search;
	}
	
	
}
