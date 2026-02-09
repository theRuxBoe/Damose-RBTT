package frontend.rightpanel;

import java.awt.BorderLayout;
//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.border.BevelBorder;

public class FocusPanel extends JPanel {
	
	private JPanel previous;
	private RightPanel rp;
	private JPanel current;
	
	public FocusPanel() {
		super();
		setLayout(new BorderLayout());
		addBackButton();
	
	}
	
	public void setFocus(JPanel p) {
		if (current != null) {
			remove(current);
		}
		current = p;
		add(p, BorderLayout.CENTER);
		
	}
	
	private void addBackButton() {
		JPanel p = new JPanel();
		JButton b = new JButton("GO BACK");
		b.setSize(getPreferredSize());
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				rp.setShowCurrent(previous);
				FocusController.openPrevious();
			}
		});
		
		JLabel label = new JLabel("You have selected : ", JLabel.CENTER);
		
		p.add(b);
		p.add(label);
		
		add(p,BorderLayout.NORTH);
	}

	public void setRightPanel(RightPanel r) {
		if (rp == null) {
			this.rp = r;
		}
		
	}
	
	public void setPrevious(JPanel p) {
		previous = p;
	}
	
	public JPanel getPrevious() {
		return previous;
	}
	
}
