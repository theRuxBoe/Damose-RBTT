package frontend.focus;

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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class SearchPanel extends JPanel {
	
	private JScrollPane scroller;
	
	public SearchPanel() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		addSearcher();
		addScrollPane();
		
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
//		
//		b.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//			}
////				we call the database search method (which we don't know at the moment)
//				System.out.println(text.getText()); 
////				then we put all the data we get from backend into the panel
//				JPanel finale = new JPanel();
//				for (Searchable s : data_retrieved) { 	// da rivedere TODO
//					if (s instanceof Line) {
//						finale.add(new LinePanel(s));
//					}
//					else {
//						finale.add(new BusStopPanel(s));
//						
//					}
//					
//				}
//				scroller.add(finale);
//			}
//		});
		panel.add(text);
		panel.add(b);
		this.add(panel);
		
	}
	
	
	private void addScrollPane() {		
		JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(50,300));
		scroller = scroll;
		
		this.add(scroll);
		
	}
	
	
}
