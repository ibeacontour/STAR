
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class view extends JFrame implements KeyListener {
	private static final long serialVersionUID = -4531812284827958061L;
	// create the controls to be placed on the form
	private static JTextField searchField;
	private String search;
	private Controller controller;
	private static JLabel imageLabel;

	//TODO: create custom type for results, no way string will be sufficient
	private static JScrollPane rScroll;
	private static JList<Object> results;
	private static DefaultListModel<Object> listModel;
	private static ImageIcon sIcon;
	private static ImageIcon wIcon;
	private static int cHeight;
	
	//NOTE: this is a hack for the code below so that we have access to our JFrame since
	//keyword "this" in the below context refers to the component adapter and not THIS as
	//in our frame itself. Bad practice, please advise.
	private static JFrame myFrame; // = this;

	//controller for on shown and hidden events
	ComponentAdapter myComponentAdapter = new ComponentAdapter() {
		public void componentHidden(ComponentEvent e) 
		{
			/* code run when component hidden*/
			//clear the search field
			searchField.setText("");

			//clear the results
			listModel.clear();
			
			//restore the size
			rScroll.setVisible(false);
			myFrame.setSize(myFrame.getWidth(), searchField.getHeight());
			
			//release from the foreground
			myFrame.setAlwaysOnTop(false);
		}
		public void componentShown(ComponentEvent e) {
			/* code run when component shown */
			//Center on screen	
			Dimension screenDims = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			myFrame.setLocation((screenDims.width - myFrame.getWidth())/2, (screenDims.height - myFrame.getHeight())/2);

			//compact the view (hide the results field)
			rScroll.setVisible(false);
			cHeight = searchField.getHeight();
			myFrame.setSize(myFrame.getWidth(), cHeight);
			
			//make the search field's border sexy
			//searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
			
			//lock to the foreground
			myFrame.setAlwaysOnTop(true);
			
			//grab focus
			searchField.requestFocus();
			
		}
	};


	public view() {
		//we need this hacked solution to access the frame outside of its context
		myFrame = this;
		
		//Windows Configuration Parameters
		this.addComponentListener(myComponentAdapter);

		//Borderless Window
		this.setUndecorated(true);
		((JComponent)this.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

		//set default size to some same ratio of the current desktops size
		Dimension screenDims = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenDims.width/3, screenDims.height/5);
		this.getContentPane().setBackground(Color.DARK_GRAY);

		//initialize layout
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridBag);

		// initialize controls	

		//Search Icon
		//create an image
		try {
			//load searching image
			ClassLoader cLoad = Thread.currentThread().getContextClassLoader();
			BufferedImage sImage = ImageIO.read(cLoad.getResource("Search.png"));
			int sIconWidth = this.getHeight() / 5;
			sIcon = new ImageIcon(sImage.getScaledInstance(sIconWidth, -1, Image.SCALE_SMOOTH), "STARsearch");
			
			//load waiting imageSear
			cLoad = Thread.currentThread().getContextClassLoader();
			BufferedImage wImage = ImageIO.read(cLoad.getResource("Wait.png"));
			int wIconWidth = this.getHeight() / 5;
			wIcon = new ImageIcon(wImage.getScaledInstance(wIconWidth, -1, Image.SCALE_SMOOTH), "STARsearch");

			
			//Set Current Image
			imageLabel = new JLabel(sIcon);
			imageLabel.setSize(this.getHeight()/5, this.getHeight() / 5);
			

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = (double)(imageLabel.getWidth())/(double)this.getWidth();
			this.add(imageLabel, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Search Field
		//initialization
		searchField = new JTextField();
		Font font1 = new Font("SansSerif", Font.PLAIN, this.getHeight()/5);
		searchField.setFont(font1);
		searchField.setHorizontalAlignment(JTextField.LEFT);
		searchField.addKeyListener(this);

		searchField.setBorder(BorderFactory.createMatteBorder(3, 0, 3, 0, Color.BLACK));
		//layout
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		//c.weighty = .10;
		//add component
		this.add(searchField, c);


		TextPrompt testPrompt = new TextPrompt("Search", searchField);
		testPrompt.changeAlpha(0.25f);
		
		//Results Box
		listModel = new DefaultListModel<Object>();
		results = new JList<Object>(listModel);
		rScroll = new JScrollPane(results);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		c.gridheight = 3;
		c.gridwidth = 2;

		this.add(rScroll, c);
		((JComponent)rScroll).setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		//rScroll.add(results, );
		
		//for the JLIST
		results.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				@SuppressWarnings("unchecked")
				JList<File> eList = (JList<File>)e.getSource();
				if (e.getClickCount() == 2) {
					//to make sure we don't double click outside the bounds of the last element
					Rectangle r = eList.getCellBounds(0, eList.getLastVisibleIndex());
					if ((r != null) && (r.contains(e.getPoint()))){
					//on double click
					int index = eList.locationToIndex(e.getPoint());
					
					//send index on
					//System.out.println(eList.getModel().getElementAt(index));
					controller.execute(eList.getModel().getElementAt(index));
					}
				} 
			}
		});
	}

	public void setController(Controller c) {
		controller = c;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			this.setVisible(false);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			//change the icon
			imageLabel.setIcon(wIcon);
			
			//clear the list view
			listModel.clear();
						
			//compact the view (hide the results field)
			rScroll.setVisible(false);
			myFrame.setSize(myFrame.getWidth(), cHeight);
			
			//make the search field's border sexy
			searchField.setBorder(BorderFactory.createMatteBorder(3, 0, 3, 0, Color.BLACK));
			
			search = searchField.getText();
			System.out.println(search);

			try {
				controller.newSearch(search);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if (e.getKeyChar() == KeyEvent.VK_DOWN) {
			if (results.getSelectedIndex() == -1) 
				results.setSelectedIndex(0);
			else if (results.getSelectedIndex() == (results.getComponentCount() - 1))
				results.setSelectedIndex(0);
			else {
				results.setSelectedIndex(results.getSelectedIndex() + 1);
			}
		}
		
		if (e.getKeyChar() == KeyEvent.VK_UP) {
			if (results.getSelectedIndex() == -1) 
				results.setSelectedIndex(results.getComponentCount() - 1);
			else if (results.getSelectedIndex() == 0)
				results.setSelectedIndex(results.getComponentCount() - 1);
			else {
				results.setSelectedIndex(results.getSelectedIndex() - 1);
			}
		}

	}
	
	public static void setResults(ArrayList<File> rslts){
		
		//used to pass ArrayList<File> Parameter to a runnable
		class ParameterizedRunnable implements Runnable {
			ArrayList<File> newFiles;
			
			ParameterizedRunnable(ArrayList<File> files) { newFiles = files;}

			public void run() {
				//basically our old view method neatly wrapped up
				//restore the icon
				imageLabel.setIcon(sIcon);
				
				//clear the list
				listModel.clear();
				
				//show the results field
				//set default size to some same ratio of the current desktops size
				Dimension screenDims = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				myFrame.setSize(screenDims.width/3, screenDims.height/5);
				
				//fix the jank search border
				searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
				
				//Add a border to the top of the results box
				((JComponent)rScroll).setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLACK));
				
				//finally show the results
				rScroll.setVisible(true);
				
				
				//add all the new results to the list
				for (int i = 0; i < newFiles.size(); i++) {
					listModel.addElement(newFiles.get(i));
				}
				
				results.setModel(listModel);
				
				
				if (listModel.getSize() > 0) {
					results.setEnabled(true);
				} else {
					listModel.addElement("(No Results Found)");
					results.setEnabled(false);
				}
			}
		}
		
		//call on UI thread
		SwingUtilities.invokeLater(new ParameterizedRunnable(rslts));
	}

}
