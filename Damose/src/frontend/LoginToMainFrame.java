package frontend;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JFrame;

import frontend.user.LoginPanel;
import frontend.user.RegisterPanel;

public class LoginToMainFrame {

	private static LoginPanel logpane;
	private static RegisterPanel regpane;
	
	public static void openLogin(MainFrame f) {
		if (logpane == null ) {
			LoginPanel l = new LoginPanel();
			logpane = l;
			
		}
		if (regpane != null) {
			f.remove(regpane);
		}
		
		logpane.addObserver(f);
		f.setExtendedState(JFrame.NORMAL); 
//		f.setLocationRelativeTo(null);
		f.setLocation(new Point(800,300));
		
		f.add(logpane, BorderLayout.CENTER, 0);
		f.pack();
		f.repaint();
		f.revalidate();
//		
	}
	
	public static void openRegisteringPanel(MainFrame f) {
		if( regpane == null) {
			RegisterPanel r = new RegisterPanel();
			regpane = r;
		}
		if (logpane != null) {
			f.remove(logpane);
		}
		regpane.addObserver(f);
		
		f.setExtendedState(JFrame.NORMAL); 
		f.setLocation(new Point(800,300));
		
		f.add(regpane, BorderLayout.CENTER, 0);
		f.pack();
		f.repaint();
		f.revalidate();
	}
}
