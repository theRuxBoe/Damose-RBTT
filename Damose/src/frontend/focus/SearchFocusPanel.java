package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class SearchFocusPanel extends JPanel{

//	private FocusPanel focus;
	private SearchPanel search;
	
	public SearchFocusPanel() {
		super();
		setLayout(new BorderLayout());
		addPanels();
	} 
	
	public void addPanels() {
//		FocusPanel focus = new FocusPanel();
		SearchPanel search = new SearchPanel();
//		this.focus = focus;
		this.search = search;
		
		
		this.add(search, BorderLayout.CENTER);
//		this.add(focus, BorderLayout.SOUTH);
	}

	
//	public FocusPanel getFocus() {
//		return focus;
//	}

	
	public SearchPanel getSearch() {
		return search;
	}
	
	
}
