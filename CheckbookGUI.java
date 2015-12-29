// CheckbookGUI Class
// Author: Matthew Cucuzza 
// 
// Main class which creates the Checkbook GUI 

import javax.swing.JFrame;


public class CheckbookGUI extends JFrame {
	
	// Variables declaring size of frame 
	private static final int WIDTH = 860;
	private static final int HEIGHT = 555;
	
	public CheckbookGUI() { 
		CheckbookPanel cbPanel = new CheckbookPanel(); 
		JFrame frame = new JFrame("Checkbook Application"); 		
		frame.add(cbPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public static void main(String[] args) { 
		new CheckbookGUI(); 
	}

}
