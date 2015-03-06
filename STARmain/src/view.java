import javax.swing.*;

import java.awt.Font;
import java.awt.Toolkit.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class view extends JFrame implements KeyListener {

	private static final long serialVersionUID = -4531812284827958061L;
	// create the controls to be placed on the form
	JPanel myPanel;
	JTextField searchField;
	String search;
  	Controller controller;
  
  public view() {
    // TODO: check for headless environment, add multiple desktop handling
		// also set default size to some same ratio of the current desktops size
		this.setSize(800, 256);
		// initialize controls
		myPanel = new JPanel();
		searchField = new JTextField();
		myPanel.setLayout(null);
		
		JLabel promt = new JLabel("Type the name of the file you want to search");
		promt.setLocation(230, 10);
		promt.setSize(400,20);
		
		Font font1 = new Font("SansSerif", Font.PLAIN, 20);
		
		searchField.addKeyListener(this);
		
		searchField.setLocation(200, 50);
		searchField.setSize(400,30);
		searchField.setFont(font1);
		searchField.setHorizontalAlignment(JTextField.LEFT);
		
		// add controls to actual JFrame
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		myPanel.add(searchField);
		myPanel.add(promt);
		this.add(myPanel);
  }
  
  public void setController(Controller c) {
		controller = c;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			search = searchField.getText();
			System.out.println(search);
			try {
				controller.newSearch(search);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
  
}
