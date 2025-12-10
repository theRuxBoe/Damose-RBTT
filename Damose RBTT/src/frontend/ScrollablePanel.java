package main.frontend;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.frontend.focus.entities.EntitiesPanelFactory;

public abstract class ScrollablePanel extends JPanel{
	
//	private JScrollPane addScrollPanel() {
//		
//		return p;
//	}
	
	public JScrollPane setContent(List<JPanel> panels) {
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, 0));
		for ( JPanel p : panels) {
			support.add(p);
		}
		JScrollPane scroll = new JScrollPane(support);
		return scroll;
	}
	
//	we are making a method to convert a given list of elements into a list of panels (lines, bus stops, news, ...)
	public List<JPanel> convertList(List<Entity> input) {
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (Entity e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
}
