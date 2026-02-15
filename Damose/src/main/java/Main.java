package main.java;

import main.java.frontend.BackendController;
import main.java.frontend.MainFrame;
import main.java.frontend.user.LoginToMainFrame;

public class Main {

	
	public static void main(String[] args) {
		BackendController.openTransit();
		MainFrame f = new MainFrame();
		LoginToMainFrame.openLogin(f);
		
	}
}
