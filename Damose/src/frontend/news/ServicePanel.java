package frontend.news;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import frontend.ScrollablePanel;

public class ServicePanel extends ScrollablePanel{
	
	public ServicePanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new BevelBorder(BevelBorder.RAISED));
		addLabel();
		addNews();
		
	}
	
	
	private void addLabel() {
		JLabel l = new JLabel("Real-Time service updates : ");
		l.setFont(new Font("Monospaced", Font.BOLD, 20));
		add(l);
	}
	
	private void addNews() {
//		prendiamo getall warnings, con un for li trasformiamo tutti in NewsPanel
		
	}
	
	
}
