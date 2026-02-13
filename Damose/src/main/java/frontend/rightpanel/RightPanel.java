package main.java.frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import main.java.frontend.main.MainFrame;
import main.java.frontend.main.RightPanelToMainFrameController;
import main.java.frontend.rightpanel.panels.StopFocus;
import main.java.frontend.user.LoginToMainFrame;

/**
 * The Class RightPanel maintains the space needed to display 
 * the three types of panel (search, favourites and focus) while
 * also providing a button to switch between them.
 * 
 * 
 */
public class RightPanel extends JPanel {

	/** The {@link SearchPanel}. */
	private SearchPanel search;

	/** The {@link FavouritesPanel}. */
	private FavouritesPanel fav;

	/** The {@link FocusPanel}. */
	private FocusPanel focusPanel;

	/** The current panel. */
	private JPanel current;

	/** The {@link MainFrame}. */
//	private MainFrame frame;

	/**
	 * Instantiates a new right panel from a given {@link MainFrame}.
	 *
	 * @param m the {@link MainFrame}
	 */
	public RightPanel() {
		super();
		setBackground(new Color(0x7851a9));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setMaximumSize(getPreferredSize());
		setBorder(new BevelBorder(BevelBorder.RAISED));
		addTopButton();
		FocusController.setRightPanel(this);
	}

	/**
	 * Adds the top button to the right panel. This button
	 * allows the registered user to switch between the {@link FavouritesPanel}
	 * and the {@link SearchPanel}.
	 */
	private void addTopButton() {
		JButton button = new JButton();
		button.setAlignmentX(CENTER_ALIGNMENT);
		if (LoginToMainFrame.isLogged()) {
			button.setText("Ricerca");
			button.addActionListener(e -> {

				if (button.getText() == "Ricerca") {
					button.setText("Preferiti");
				} 
				else {
					button.setText("Ricerca");
				}
				RightPanelToMainFrameController.switchCurrentRightPanel();

			});

		} else {
			button.setText("Login");
			button.addActionListener(e -> {

					LoginToMainFrame.openLogin(RightPanelToMainFrameController.getFrame());
					repaint();
					revalidate();
				
			});
		}
		add(button);
	}

	/**
	 * Gets the current displayed panel.
	 *
	 * @return the current panel
	 */
	public JPanel getCurrent() {
		return current;
	}

	/**
	 * Gets the {@link FocusPanel}.
	 *
	 * @return the focus panel
	 */
	public FocusPanel getFocus() {
		return focusPanel;
	}

	/**
	 * 
	 */
	public void openSearchPanel() {
		if (search == null) {
			SearchPanel search = new SearchPanel();
			this.search = search;
		}

		setAndShowCurrent(search);

	}

	/**
	 * Creates the {@link FavouritesPanel} if not present, 
	 * adds the favourites j scroll panel and
	 * sets it to be displayed.	 
	 * */
	public void openFavouritePanel() {
		if (fav == null) {
			FavouritesPanel fpanel = new FavouritesPanel();
			fav = fpanel;
		}
		fav.showFavourites();
		setAndShowCurrent(fav);
	}

	/**
	 * Creates the {@link FocusPanel} if not present,
	 * sets the focus on the {@link StopFocus} given
	 * and sets it to be displayed.
	 *
	 * @param p the stop focus
	 */
	public void openFocusPanel(StopFocus p) {
		if (focusPanel == null) {
			FocusPanel foc = new FocusPanel();
			focusPanel = foc;
		}
		focusPanel.setPrevious(current);
		focusPanel.setFocus(p);

		setAndShowCurrent(focusPanel);
	}

	/**
	 * Sets the current panel and shows it.
	 *
	 * @param p the new current panel to be displayed
	 */
	public void setAndShowCurrent(JPanel p) {

		if (current != null) {
			this.remove(current);
		}
		current = p;
		this.add(current, BorderLayout.CENTER);
		repaint();
		revalidate();

	}

}
