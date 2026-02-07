package frontend;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
	import javax.swing.JScrollPane;

import backendNOTPUSH.Entity;
import frontend.focus.entities.EntitiesPanelFactory;

public abstract class ScrollablePanel extends JPanel{
	
//			Change the data structure used for content
	
	public JScrollPane setContent(List<JPanel> panels) {
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, BoxLayout.PAGE_AXIS));
		for ( JPanel p : panels) {
			support.add(p);
		}
		JScrollPane scroll = new JScrollPane(support);
//		
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		return scroll;
	}
	
	public List<JPanel> convertList(ArrayList<? extends Entity> input) {
		
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (Entity e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
	
	
}
