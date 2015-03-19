import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class view extends JFrame implements KeyListener {
	private static final long serialVersionUID = -4531812284827958061L;
	// create the controls to be placed on the form
	private JTextField searchField;
	private String search;
	private Controller controller;
	//TODO: create custom type for results, no way string will be sufficient
	private JList<String> results;
  
	//NOTE: this is a hack for the code below so that we have access to our JFrame since
	//keyword "this" in the below context refers to the component adapter and not THIS as
	//in our frame itself. Bad practice, please advise.
	private JFrame myFrame = this;
	
	//controller for on shown and hidden events
	ComponentAdapter myComponentAdapter = new ComponentAdapter() {
	  public void componentHidden(ComponentEvent e) 
	  {
	      /* code run when component hidden*/
	  }
	  public void componentShown(ComponentEvent e) {
	    /* code run when component shown */
	    //Center on screen
	    Dimension screenDims = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	    myFrame.setLocation((screenDims.width - myFrame.getWidth())/2, (screenDims.height - myFrame.getHeight())/2);
	    
	  }
	};
	
	
  public view() {
    // TODO: check for headless environment, add multiple desktop handling
    
    //Windows Configuration Parameters
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.addComponentListener(myComponentAdapter);
    
    //Borderless Window
    this.setUndecorated(true);
    
		//set default size to some same ratio of the current desktops size
    Dimension screenDims = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenDims.width/3, screenDims.height/5);
		
		
		//initialize layout
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridBag);
		
		// initialize controls	
		
		//Search Field
		//initialization
		searchField = new JTextField();
		Font font1 = new Font("SansSerif", Font.PLAIN, this.getHeight()/5);
		searchField.setFont(font1);
		searchField.setHorizontalAlignment(JTextField.LEFT);
		searchField.addKeyListener(this);
		
		//layout
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		//c.weighty = .10;
		//add component
		this.add(searchField, c);
		
		
		//Results Box
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		results = new JList<String>(listModel);
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.gridheight = 3;
		
		//dummy data
		listModel.addElement("The Fall of Hyperion");
		listModel.addElement("The Tempest");
		listModel.addElement("Othello");
		
		this.add(results, c);
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
