package main.frontend.news;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import main.frontend.ScrollablePanel;

public class ServicePanel extends ScrollablePanel{
	
	public ServicePanel() {
		super();
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setPreferredSize(new Dimension(100,200));
		add(new JLabel("Real-Time service updates : "));
//		NewsData news = new NewsData();
//		news.getNews();
//		setContentList(news.getNews());
		
		
	}
	
	

	
	
}
