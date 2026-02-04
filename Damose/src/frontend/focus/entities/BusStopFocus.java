package frontend.focus.entities;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import backendNOTPUSH.Bus;
import backendNOTPUSH.BusStop;

public class BusStopFocus extends BusStopPanel  {

	private ArrayList<Bus> arriving;
	
	public BusStopFocus(BusStop bs) {
		super(bs);
		addScroll();
		this.arriving = bs.getArrivingBuses();
		removeListener();
		
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
		arriving = new ArrayList<Bus>();
		arriving.add(new Bus());
		this.add(setContent(convertList(arriving)), BorderLayout.SOUTH);
	}

	
//	public static void main(String[] args) {
//		JFrame f = new JFrame();
//		f.add(new BusStopFocus(new BusStop()));
//		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		f.setVisible(true);
//		f.pack();
//	}
}