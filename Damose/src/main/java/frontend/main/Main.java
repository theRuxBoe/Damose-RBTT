package main.java.frontend.main;

import main.java.frontend.user.LoginToMainFrame;

public class Main {

	
	public static void main(String[] args) {
		BackendController.openTransit();
		
		MainFrame f = new MainFrame();
		LoginToMainFrame.openLogin(f);
		
	}
}
