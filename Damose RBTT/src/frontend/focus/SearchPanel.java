package main.frontend.focus;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import frontend.ScrollablePanel;

public class SearchPanel extends ScrollablePanel {
	
//	private JScrollPane scroller;
	private List<Entity> searchResult;
	
	
	public SearchPanel() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		addSearcher();
//		addScrollPane();
		
	}
	
	
	private void addSearcher() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JTextField text = new JTextField("Search", 10);
		text.setPreferredSize(text.getMinimumSize());
		text.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				text.setText("");
			}
		});
		JButton b = new JButton("🔎");
		b.setPreferredSize(b.getMaximumSize());
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String x = text.getText();
//				database search
//				searchResult = List<Entity>;
			}
		});
		setContent(convertList(searchResult));
		panel.add(text);
		panel.add(b);
		this.add(panel);
		
	}
	
	
//	private void addScrollPane() {		
//		JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		scroll.setPreferredSize(new Dimension(50,300));
//		scroller = scroll;
//		
//		this.add(scroll);
//		
//	}
	
	
}
