package frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import backend.favorite.FavoriteAlreadyExistingException;
import backend.model.Fermata;
import backend.model.Linea;
import backend.model.RisultatoFermata;
import frontend.main.MainFrame;
import frontend.user.FavouritesDBManager;
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
		if (MainFrame.isLogged()) {
			p.add(createFavouriteButton());
		}
		
		
		p.add(data);
		add(p, BorderLayout.NORTH);
	}
	
	private JButton createFavouriteButton() {
		String s = FavouritesDBManager.isPresent(line) ? "★" : "☆";
		JButton b = new JButton(s);
		b.setSize(new Dimension(10,10));
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getText() == "☆") {
					b.setText("★");
					
					FavouritesDBManager.addToFavourites(line);
					
				}
				else {
					b.setText("☆");
					FavouritesDBManager.remove(line);
				}
			}
		});
		return b;
	}
	
	private void addScrollPanel() {
		List<Fermata> j = MainFrame.getTTS().trovaFermatePerLinea(line.getRouteId(), line.getDescription());
		JScrollPane x = ListToScrollConverter.setContent(ListToScrollConverter.convertList(j));
		if (x != null) {
		x.setMaximumSize(getPreferredSize());
		add(x, BorderLayout.WEST);
		}
	}



}
