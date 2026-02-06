package frontend.focus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import backendNOTPUSH.Line;
import backendNOTPUSH.Line;
//import backendNOTPUSH.Line;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import backendNOTPUSH.Entity;

import frontend.ScrollablePanel;

public class SearchPanel extends ScrollablePanel {
	
	private  JScrollPane searchResult;
	
	
	public SearchPanel() {
		super();
		setLayout(new BorderLayout());
		addSearcher();
		
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
//				String x = text.getText();
////				database search
//				searchResult = new ArrayList<Entity>();
//				setContent(convertList(searchResult));
				
				if (searchResult != null) {
					clearResults();
				}
				
//				results = database search
				showResults();	// parameter needs to be a result
				
			}
		});
		panel.add(text);
		panel.add(b);
		
		this.add(panel, BorderLayout.NORTH);
		
	}
	
	private void showResults() {	//param == result from db search		ArrayList<Entity> input
		ArrayList<Line> sr = new ArrayList<Line>();
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		sr.add(new Line());
		
		
		searchResult = setContent(convertList(sr));		
		searchResult.setPreferredSize(new Dimension(450,800));
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.setPreferredSize(new Dimension(500,500));
		
		pane.add(searchResult, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		repaint();
		revalidate();
	
	}
	
	private void clearResults() {
		
		this.remove(searchResult);
		searchResult = null;
	}
	
}
