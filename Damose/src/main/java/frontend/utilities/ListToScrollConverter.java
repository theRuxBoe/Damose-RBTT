package main.java.frontend.utilities;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.java.backend.model.DatoGTF;
import main.java.backend.service.TransitServiceImpl.WrapperGenerico;
import main.java.frontend.rightpanel.panels.EntitiesPanelFactory;

/**
 * The Class ListToScrollConverter is a utility tool to convert a given
 * list of entities (like buses, stops and so on) into a JScrollPane
 * to be displayed.
 */
public abstract class ListToScrollConverter {

	/**
	 * Creates the actual {@link JScrollPane} from a list of Panels.
	 *
	 * @param <W> the generic panel type
	 * @param panels the panels' list
	 * @return the scroll pane ready
	 */
	public static <W extends JPanel> JScrollPane setContent(List<W> panels) {
		if (panels.isEmpty()) {
			return null;
		}
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, BoxLayout.PAGE_AXIS));

		for (JPanel p : panels) {
			support.add(p);
		}
		JScrollPane scroll = new JScrollPane(support);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		return scroll;
	}

	/**
	 * Converts a list of {@link DatoGTF} type into a list of panels to be
	 * added to the scroll pane later
	 *
	 * @param <T> the generic type that extends DatoGTF
	 * @param input the input list of DatoGTF objects
	 * @return the panels' list
	 */
	public static <T extends DatoGTF> List<JPanel> convertList(List<T> input) {

		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (DatoGTF e : input) {
			panels.add(epf.createPanel(e));

		}

		return panels;
	}

	/**
	 * Converts a list of {@link WrapperGenerico} into a list of panels to 
	 * be added to the scroll pane. Some sort of overload of the method 
	 * convertList with a different name to avoid type erasure-related issues.
	 * Used mainly to convert the output list from a database search.
	 *
	 * @param input the input list of Wrapper Generico 
	 * @return the panels' list 
	 */
	public static List<JPanel> convertSearchedList(List<WrapperGenerico> input) {

		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (WrapperGenerico e : input) {
			panels.add(epf.createPanel(e));

		}

		return panels;
	}

}
