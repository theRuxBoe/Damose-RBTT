package frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


import backend.service.TransitServiceImpl.WrapperGenerico;
import frontend.main.MainFrame;
import frontend.utilities.ListToScrollConverter;

public class SearchPanel extends JPanel {
	
	private JPanel resultPanel;
	private JScrollPane scrollResult;
	private JTextField text;	
	
	public SearchPanel() {
		super();
		setLayout(new BorderLayout());
		addSearcher();
		
	}
	
	
	private void addSearcher() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JTextField text = new JTextField("Search", 10);
		this.text = text;
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
		
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String x = SearchPanel.this.getText();
					if (x.length() != 0 && !x.equals("Search")) {
						if (scrollResult != null) {
							resultPanel.remove(scrollResult);
						}
//						database search
						List<WrapperGenerico> risultato = MainFrame.getTTS().ricercaGenerica(x);
						
						showResults(risultato);
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		JButton b = new JButton("🔎");
		b.setPreferredSize(b.getMaximumSize());
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String x = SearchPanel.this.getText();
				if (x.length() != 0 && !x.equals("Search")) {
					if (scrollResult != null) {
						resultPanel.remove(scrollResult);
					}
//					database search
					List<WrapperGenerico> risultato = MainFrame.getTTS().ricercaGenerica(x);
					
					showResults(risultato);
				}
				
				
			}
		});
		panel.add(text);
		panel.add(b);
		
		this.add(panel, BorderLayout.NORTH);
		
	}
	
	private String getText() {
		return text.getText();
	}
	
	private void showResults(List<WrapperGenerico> res) {
		if (resultPanel == null) {
			JPanel p = new JPanel();
			resultPanel = p;
			resultPanel.setLayout(new BorderLayout());
//			resultPanel.setPreferredSize(new Dimension(450,800));
			resultPanel.setPreferredSize(new Dimension(500,700));
		}
		
		JScrollPane scrollResult = ListToScrollConverter.setContent(ListToScrollConverter.convertSearchedList(res));
		scrollResult.setPreferredSize(new Dimension(500,500));
		this.scrollResult = scrollResult;
		
		resultPanel.add(scrollResult, BorderLayout.NORTH);
		
		add(resultPanel, BorderLayout.CENTER);
		
		repaint();
		revalidate();
	
	}
	
	
}
