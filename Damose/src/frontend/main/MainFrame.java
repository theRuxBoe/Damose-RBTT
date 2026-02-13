package frontend.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import frontend.news.ServicePanel;
import frontend.rightpanel.RightPanel;
import frontend.user.LoginPanel;
import frontend.user.LoginToMainFrame;

/**
 * The Class MainFrame is the core of the front-end structure.
 * It manages the main panels inside the application.
 * <p>
 * When starting the class opens the {@link LoginPanel}
 * to let the user authenticate themselves.
 * 
 */
public class MainFrame {

	/** The frame. */
	private JFrame frame;
	
	/** The main panel for {@link MapPanel}, {@link ServicePanel} and 
	 * {@link RightPanel}. */
	private JPanel mainPanel;
	
	/** The {@link CardLayout}. */
	private CardLayout cardLayout = new CardLayout();
	
	/**
	 * Instantiates a new main frame by creating the frame,
	 * creating the {@link MapPanel}and the {@link ServicePanel}, 
	 * and opening the {@link LoginPanel}.
	 */
	public MainFrame() {
		createFrame();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		createMainPanels();
		setIcon();
		frame.setVisible(true);
	}

	
	

	/**
	 * Creates the frame and sets its layout.
	 */
	private void createFrame() {
		JFrame f = new JFrame("Damose - Rome Bus Transit Tracker");
		frame = f;
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(cardLayout);
	}

	/**
	 * Sets the frame's icon.
	 */
	private void setIcon() {
		ImageIcon img;
		if (System.getProperty("os.name").startsWith("Windows")) {
			img = new ImageIcon("src/res/bus.ico");
		} else {
			img = new ImageIcon("src/res/busIcon.png");
		}
		frame.setIconImage(img.getImage());
	}

	/**
	 * Creates the main panels ({@link MapPanel} and
	 * {@link ServicePanel}).
	 */
	private void createMainPanels() {

		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BorderLayout());
		this.mainPanel = mPanel;
		frame.getContentPane().add(mainPanel, "Main Panel");
		
		MapPanel mapPanel = new MapPanel();
		ServicePanel servicePanel = new ServicePanel();
		mainPanel.add(servicePanel, BorderLayout.WEST);
		mainPanel.add(mapPanel, BorderLayout.CENTER);

	}

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	
	/**
	 * Gets the {@link CardLayout}
	 *
	 * @return the {@link CardLayout}
	 */
	public CardLayout getCardLayout() {
		return cardLayout;
	}

	/**
	 * Gets the main panel.
	 *
	 * @return the main panel
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}
	
}
