package main.frontend.news;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class NewsPanel extends JPanel{
		
	private String line_affected;
	private String title;
	private String time_period;
	private String description;
	
	
	public NewsPanel(News n) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		eventually we need some graphic things
//		getting infos from n to add them to the fields
	}
}
