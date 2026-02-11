package frontend.news;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import backend.realtime.ServiceAlertInfo;
import frontend.main.MainFrame;
import frontend.utilities.ListToScrollConverter;

public class ServicePanel extends JPanel{
	
	public ServicePanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setPreferredSize(new Dimension(300, 0));
		addLabel();
		
		addNews();
		
		
	}
	
	
	private void addLabel() {
		JLabel l = new JLabel("Real-Time service updates : ");
		l.setFont(new Font("Monospaced", Font.BOLD, 20));
		add(l);
	}
	
	private void addNews() {
		List<ServiceAlertInfo> alerts = MainFrame.getTTS().getAllAlerts();
		if (alerts.isEmpty()) { System.out.println("alerts vuoti"); }
		List<NewsPanel> alertPanel = new ArrayList<>();
		for (ServiceAlertInfo alert : alerts) {
			NewsPanel n = new NewsPanel(alert);
			alertPanel.add(n);
		}
		JScrollPane scroll =  ListToScrollConverter.setContent(alertPanel);
//		scroll.setPreferredSize(getPreferredSize());
		this.add(scroll);
	}
	
	
}
