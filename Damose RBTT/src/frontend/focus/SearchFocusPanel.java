package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class SearchFocusPanel extends JPanel{

	private FocusPanel focus;
	private SearchPanel search;
	
	public SearchFocusPanel() {
<<<<<<< Updated upstream
		// TODO Auto-generated constructor stub
	} 
	
	public JPanel createPanels() {
=======
		super();
		addPanels();
	} 
	
	public void addPanels() {
>>>>>>> Stashed changes
		FocusPanel focus = new FocusPanel();
		SearchPanel search = new SearchPanel();
		this.focus = focus;
		this.search = search;
		
		search.setBorder(new BevelBorder(0)); 
		
		
<<<<<<< Updated upstream
		search.setPreferredSize(new Dimension(500,500));
=======
//		search.setPreferredSize(new Dimension(500,500));
>>>>>>> Stashed changes
		
		this.setLayout(new BorderLayout());
		
		this.add(search, BorderLayout.NORTH);
		this.add(focus, BorderLayout.SOUTH);
		
<<<<<<< Updated upstream
		return this;
=======
>>>>>>> Stashed changes
		
	}

	public FocusPanel getFocus() {
		return focus;
	}

	public SearchPanel getSearch() {
		return search;
	}
	
	
	
}
