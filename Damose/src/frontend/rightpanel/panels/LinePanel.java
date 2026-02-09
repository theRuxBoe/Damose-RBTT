package frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import backend.model.Linea;
import backend.model.RisultatoFermata;
import frontend.main.MainFrame;
import frontend.utilities.ListToScrollConverter;

public class LinePanel extends JPanel { 
	
//	private ArrayList<Fermata> stops;
//	private String id;
//	private String direction;
	private Linea line;
	
	
	public LinePanel(Linea l) {
		super();
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.line = l;
		
		addLabelsData();
		addScrollPanel();
		setMaximumSize(getPreferredSize());
		setAlignmentX(LEFT_ALIGNMENT);
	
	}
	
	
	private void addLabelsData() {
		JPanel p = new JPanel();
		JLabel data = new JLabel("  " + line.getName() + " - " + line.getDescription());
		data.setFont(new Font("Serif", Font.BOLD, 30));
		JButton b = new JButton("☆");
		
		b.setSize(new Dimension(10,10));
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getText() == "☆") {
					b.setText("★");
//					this will put the stop into the favourites area
				}
				else {
					b.setText("☆");
//					this will kick the stop from the favourites
				}
			}
		});
		
		p.add(data);
		p.add(b);
		add(p, BorderLayout.NORTH);
	}
	
	private void addScrollPanel() {
		List<RisultatoFermata> j = MainFrame.getTTS().trovaFermatePerLinea(line.getRouteId(), line.getDescription());
		JScrollPane x = ListToScrollConverter.setContent(ListToScrollConverter.convertList(j));
		x.setPreferredSize(new Dimension(400,100));
		add(x, BorderLayout.WEST);
		
	}



}
