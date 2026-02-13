package frontend.rightpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import backend.service.TransitServiceImpl.WrapperGenerico;
import frontend.main.BackendController;
import frontend.utilities.ListToScrollConverter;

/**
 * The Class SearchPanel creates a panel that allows
 * the user to search for stops and lines and displays them in 
 * a j scroll pane.
 * 
 */
public class SearchPanel extends JPanel {

	/** The result panel. */
	private JPanel resultPanel;
	
	/** The scroll result. */
	private JScrollPane scrollResult;
	
	/**
	 * Instantiates a new search panel.
	 */
	public SearchPanel() {
		super();
		setLayout(new BorderLayout());
		addSearcher();
	}

	/**
	 * Adds the search interface to the search panel. The interface contains 
	 * j text field for user input and
	 * search button to start the research.
	 */
	private void addSearcher() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JTextField text = new JTextField("Search", 10);
		text.setPreferredSize(text.getMinimumSize());
		text.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				text.setText("");
			}
		});

		
		JButton b = new JButton("🔎");
		b.setFocusPainted(false);
		b.setContentAreaFilled(false);
		b.setPreferredSize(b.getMaximumSize());
		b.addActionListener(e -> {
				String x = text.getText();
				if (x.length() != 0 && !x.equals("Search")) {
					if (scrollResult != null) {
						resultPanel.remove(scrollResult);
					}
//					database search
					List<WrapperGenerico> risultato = BackendController.getTTS().ricercaGenerica(x);

					showResults(risultato);
				

			}
		});
		panel.add(text);
		panel.add(b);

		this.add(panel, BorderLayout.NORTH);

	}

	/**
	 * Shows the search results inside a j scroll pane.
	 *
	 * @param res the list resulted from the search
	 */
	private void showResults(List<WrapperGenerico> res) {
		if (resultPanel == null) {
			JPanel p = new JPanel();
			resultPanel = p;
			resultPanel.setLayout(new BorderLayout());
			resultPanel.setPreferredSize(new Dimension(500, 700));
		}
		if (!res.isEmpty()) {

			JScrollPane scrollResult = ListToScrollConverter.setContent(ListToScrollConverter.convertSearchedList(res));

			scrollResult.setPreferredSize(new Dimension(500, 500));
			this.scrollResult = scrollResult;

			resultPanel.add(scrollResult, BorderLayout.NORTH);

			add(resultPanel, BorderLayout.CENTER);
		}
		repaint();
		revalidate();

	}

}
