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
import frontend.main.BackendController;
import frontend.main.MainFrame;
import frontend.utilities.ListToScrollConverter;

/**
 * The Class ServicePanel creates all the alert panels from
 * the alerts from roma mobilità website and displays them in
 * a j scroll pane.
 */
public class ServicePanel extends JPanel{
	
	/**
	 * Instantiates a new service panel and adds the alerts to it.
	 */
	public ServicePanel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setPreferredSize(new Dimension(350, 0));
		addLabel();
		
		addAlerts();
	}
	
	
	/**
	 * Adds the top label for the panel.
	 */
	private void addLabel() {
		JLabel l = new JLabel("Aggiornamenti servizio :");
		l.setFont(new Font("Monospaced", Font.BOLD, 20));
		add(l);
	}
	
	/**
	 * Adds the alerts inside a j scroll pane and then adds it to
	 * the Service Panel.
	 */
	private void addAlerts() {
		List<ServiceAlertInfo> alerts = BackendController.getTTS().getAllAlerts();
		if (alerts.isEmpty()) { System.out.println("alerts vuoti"); }
		List<AlertPanel> alertPanel = new ArrayList<>();
		for (ServiceAlertInfo alert : alerts) {
			AlertPanel n = new AlertPanel(alert);
			alertPanel.add(n);
		}
		JScrollPane scroll =  ListToScrollConverter.setContent(alertPanel);
		this.add(scroll);
	}
	
	
}
