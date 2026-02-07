package frontend;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
	import javax.swing.JScrollPane;

import backend.model.DatoGTF;
import backend.model.Risultato;
import backend.service.TransitServiceImpl.WrapperGenerico;
//import backendNOTPUSH.Entity;
import frontend.focus.entities.EntitiesPanelFactory;

public abstract class ScrollablePanel extends JPanel{
	
	
	
	
	public JScrollPane setContent(List<JPanel> panels) {
		JPanel support = new JPanel();
		support.setLayout(new BoxLayout(support, BoxLayout.PAGE_AXIS));
		for ( JPanel p : panels) {
			support.add(p);
		}
		JScrollPane scroll = new JScrollPane(support);
//		
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		return scroll;
	}
	
//	potrebbe diventare inutile questo metodo poiché basta convertSearchedList da cui si recuperano le liste di autobus
	
	public <T extends DatoGTF> List<JPanel> convertList2(List<T> input) {
		
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (DatoGTF e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
	
	public <S extends Risultato> List<JPanel> convertList(List<S> input) {
		
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (Risultato e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
	
	
	public List<JPanel> convertSearchedList(List<WrapperGenerico> input) {
		
		List<JPanel> panels = new ArrayList<>();
		EntitiesPanelFactory epf = new EntitiesPanelFactory();
		for (WrapperGenerico e : input) {
			panels.add(epf.createPanel(e));
			
		}
		
		return panels;
	}
	
	
}
