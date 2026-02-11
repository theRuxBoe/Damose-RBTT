package frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import frontend.main.MainFrame;
import frontend.user.LoginPanel;
import frontend.user.LoginToMainFrame;

public class RightPanel extends JPanel{

	private SearchPanel search;
	private FavouritesPanel fav;
	private FocusPanel focusPanel;
	private JPanel current;
	private MainFrame f;
	
	public RightPanel(MainFrame m) {
		super();
		setBackground(new Color(0x7851a9));
//		setLayout(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setMaximumSize(getPreferredSize());
		setBorder(new BevelBorder(BevelBorder.RAISED));
		f = m;
		addTopButton();
		FocusController.setRightPanel(this);
//		FocusController.setFocusPanel(new FocusPanel());
	}
	
	private void addTopButton() {
		JButton button = new JButton();
//		button.setBorderPainted(false);
//		button.setFocusPainted(false);
//		button.setContentAreaFilled(false);
		button.setAlignmentX(CENTER_ALIGNMENT);
		if (MainFrame.isLogged()) {
			button.setText("Ricerca");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (button.getText() == "Ricerca") {
					 	button.setText("Preferiti");
					}
					else {
						button.setText("Ricerca");
					}
					f.switchCurrentRightPanel();
				}
			});
			
		}
		else {
			button.setText("Login");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
//					f.getContentPane().add(new LoginPanel(), "Login Panel");
					
					LoginToMainFrame.openLogin(f);
					repaint();
					revalidate();
			}
		});
		}
		add(button);
	}
	
		
	public JPanel getCurrent() {
		return current;
	}
	
	public FocusPanel getFocus() {
		return focusPanel;
	}
	
	
	public void openSearchPanel() {
 		if (search == null) {
 			SearchPanel search = new SearchPanel();
 			this.search = search;
 		}
 		
 		setShowCurrent(search);
 		
 	}
	
 	public void openFavouritePanel() {
 		if (fav == null ) {
 			FavouritesPanel fpanel = new FavouritesPanel();
 			fav = fpanel;
 			}
 		fav.showFavourites();
 		setShowCurrent(fav);
 		}
 	
 	public void openFocusPanel(JPanel p) {
 		if (focusPanel == null) {
 			FocusPanel foc = new FocusPanel();
 			focusPanel = foc;
 			foc.setRightPanel(this);
 		}
 		focusPanel.setPrevious(current);
 		focusPanel.setFocus(p);
 		
 		setShowCurrent(focusPanel);
 	}
 	
	
 	public void setShowCurrent(JPanel p) {
 		
 		if (current != null) {
 			this.remove(current);
 		}
 		current = p;
 		this.add(current, BorderLayout.CENTER);
 		repaint();
 		revalidate();
 		
 	}
 	
 	
}
