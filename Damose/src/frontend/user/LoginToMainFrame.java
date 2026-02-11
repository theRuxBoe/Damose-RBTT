package frontend.user;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JFrame;

import frontend.main.MainFrame;

public class LoginToMainFrame {

	private static LoginPanel logpane;
	private static RegisterPanel regpane;
	
	public static void openLogin(MainFrame f) {
		if (logpane == null ) {
			LoginPanel l = new LoginPanel();
			logpane = l;
			f.getCardPanel().add(logpane, "Login Panel");
			
		}
		
		logpane.setObserver(f);
		f.getCardLayout().show(f.getCardPanel(), "Login Panel");
		
		
		f.repaint();
		f.revalidate();
	}
	
	public static void openRegisteringPanel(MainFrame f) {
		if( regpane == null) {
			RegisterPanel r = new RegisterPanel();
			regpane = r;
			f.getCardPanel().add(regpane, "Register Panel");
		}
		
		regpane.setObserver(f);
		f.getCardLayout().show(f.getCardPanel(), "Register Panel");
		
//		f.pack();
		f.repaint();
		f.revalidate();
	}
}
