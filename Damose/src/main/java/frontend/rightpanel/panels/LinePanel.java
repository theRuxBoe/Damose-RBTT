package main.java.frontend.rightpanel.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import main.java.backend.model.Fermata;
import main.java.backend.model.Linea;
import main.java.backend.model.RisultatoLinea;
import main.java.frontend.BackendController;
import main.java.frontend.user.FavouritesDBManager;
import main.java.frontend.user.LoginToMainFrame;
import main.java.frontend.utilities.ListToScrollConverter;

/**
 * The Class LinePanel represents a {@link Linea} object
 * to be displayed.
 */
public class LinePanel extends JPanel { 
	
/** The {@link Linea} object. */
	private RisultatoLinea line;
	
	
	/**
	 * Instantiates a new line panel from a given line.
	 *
	 * @param l the line
	 */
//	public LinePanel(Linea l) {
//		super();
//		setLayout(new BorderLayout());
//		setBorder(new BevelBorder(BevelBorder.LOWERED));
//		this.line = l;
//		
//		addDataLabel();
//		addScrollPanel();
//		setMaximumSize(getPreferredSize());
//		setAlignmentX(LEFT_ALIGNMENT);
//	
//	}
	
	public LinePanel(RisultatoLinea l) {
		super();
		setLayout(new BorderLayout());
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		this.line = l;
		
		addDataLabel();
		addScrollPanel();
		setMaximumSize(getPreferredSize());
		setAlignmentX(CENTER_ALIGNMENT);
	
	}
	
	
	/**
	 * Adds to the panel a label with the line's data (name and direction) and, if a user is
	 * logged, the favourite button.
	 */
	private void addDataLabel() {
		JPanel p = new JPanel();
		if (LoginToMainFrame.isLogged()) {
			p.add(createFavouriteButton());
		}
		JLabel data = new JLabel(line.getLinea().getName() + " - " + line.getDirectionName() + " - Qualità : " + BackendController.getTTS().getValutazioneLinea(line.getRouteId()));
		data.setFont(new Font("Serif", Font.BOLD, 30));
		
		p.add(data);
		add(p, BorderLayout.NORTH);
	}
	
	/**
	 * Creates the "add to favourites" button.
	 *
	 * @return the favourites button
	 */
	private JButton createFavouriteButton() {
		String s = FavouritesDBManager.isPresent(line) ? "★" : "☆";
		JButton b = new JButton(s);
		b.setSize(new Dimension(10,10));
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.addActionListener(e -> {
				if (b.getText().equals("☆")) {
					b.setText("★");
					
					FavouritesDBManager.addToFavourites(line);
					
				}
				else {
					b.setText("☆");
					FavouritesDBManager.remove(line);
				}
			
		});
		return b;
	}

	/**
	 * Adds a scroll panel containing all the {@link StopPanel}s 
	 * that the line goes through.
	 */
	private void addScrollPanel() {
		List<Fermata> j = BackendController.getTTS().trovaFermatePerLinea(line.getRouteId(), line.getDirectionName());
		JScrollPane x = ListToScrollConverter.setContent(ListToScrollConverter.convertList(j));
		if (x != null) {
		x.setMaximumSize(getPreferredSize());
		add(x, BorderLayout.EAST);
		}
	}



}
