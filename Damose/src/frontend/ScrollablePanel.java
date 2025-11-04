package frontend;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ScrollablePanel extends JPanel{
	
//	private JScrollPane addScrollPanel() {
//		
//		return p;
//	}
	
	private JScrollPane setContent(List<JPanel> panels) {
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, 0));
		for ( JPanel p : panels) {
			support.add(p);
		}
		JScrollPane scroll = new JScrollPane(support);
		return scroll;
	}
	
}
