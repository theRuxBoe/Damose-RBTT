package frontend.focus;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchPanel extends JPanel {

	
	public SearchPanel() {
		super();
		add(searchButton());
		
	}
	
	
	private JTextField searchButton() {
		JTextField text = new JTextField("Search", 10);
		text.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				text.setText("Search");
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				text.setText("");
			}
		});
		return text;
	}
	
}
