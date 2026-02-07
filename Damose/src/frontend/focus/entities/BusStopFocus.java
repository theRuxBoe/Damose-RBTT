package frontend.focus.entities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.List;


import backend.model.Fermata;
import backend.model.PredizioneArrivo;
import frontend.MainFrame;

//import backendNOTPUSH.Corsa;
//import backendNOTPUSH.CorsaStop;

public class BusStopFocus extends BusStopPanel  {

//	private ArrayList<Corsa> arriving;
	private Fermata stop;
	
	public BusStopFocus(Fermata bs) {
		super(bs);
		stop = bs;
		
		addScroll();
		removeListener();
		setPreferredSize(new Dimension(400,400));
		
	}
	
	
	private void removeListener() {
		MouseListener[] x = this.getMouseListeners();
		for (MouseListener m : x) {
			this.removeMouseListener(m);
		}
	}
	
	public BusStopFocus(BusStopPanel pan) {
		this(pan.getStop());
	}
	
	private void addScroll() {
		List<PredizioneArrivo> arriving = MainFrame.getTTS().prediciArriviPerFermata(stop.getStopId(), 5);
		
		this.add(setContent(convertList2(arriving)), BorderLayout.SOUTH);
	}

	

}