package frontend.utilities;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
	import javax.swing.JScrollPane;

import backend.model.DatoGTF;
import backend.model.Fermata;
import backend.model.Risultato;
import backend.model.RisultatoFermata;
import backend.service.TransitServiceImpl.WrapperGenerico;
import frontend.rightpanel.panels.BusStopPanel;
import frontend.rightpanel.panels.EntitiesPanelFactory;

public abstract class ListToScrollConverter{
	
	
	
	
	public static <W extends JPanel> JScrollPane setContent(List<W> panels) {
		if (panels.isEmpty()) 
			{return null;}
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, BoxLayout.PAGE_AXIS));
		
		for ( JPanel p : panels) {
			support.add(p);
		}
		JScrollPane scroll = new JScrollPane(support);
//		
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getHorizontalScrollBar().setUnitIncrement(16);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		
		return scroll;
	}
	
//	potrebbe diventare inutile questo metodo poiché basta convertSearchedList da cui si recuperano le liste di autobus
	
	public static <T extends DatoGTF> List<JPanel> convertList(List<T> input) {
		
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (DatoGTF e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
//	TODO da rivedere questo doppio metodo
//	public static List<JPanel> convertList(List<Fermata> input) {
//		
//		List<JPanel> panels = new ArrayList<>();
//		EntitiesPanelFactory epf = new EntitiesPanelFactory();
//		for (Fermata e : input) {
//			panels.add(new BusStopPanel(e));
//			
//		}
//		
//		return panels;
//	}
	
	
	public static List<JPanel> convertSearchedList(List<WrapperGenerico> input) {
		
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (WrapperGenerico e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
	
	
	
}
