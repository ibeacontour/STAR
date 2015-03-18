import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class STARmain  {
	static view mySearchView = new view();
	static TrayIcon tIcon;
	
	public static void main(String[] args) throws InterruptedException  {

		// creates a model and controller
		// also links the model, view, and controller together in the MCV style
		Model model = new Model();
		Controller controller = new Controller();
		model.setController(controller);
		mySearchView.setController(controller);
		controller.setModel(model);
		controller.setView(mySearchView);

		//BEGIN VIEW
		//check for a windowed environment
		if (!GraphicsEnvironment.isHeadless()) {
			//add a tray item of sorts
			if (SystemTray.isSupported()) {
				try {
					//create an image
					ClassLoader cLoad = Thread.currentThread().getContextClassLoader();
					BufferedImage tImage = ImageIO.read(cLoad.getResource("icon.png"));
					int tIconWidth = new TrayIcon(tImage).getSize().width;
					//TrayIcon trayIcon = new TrayIcon(trayIconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
					
					//create a tray icon
					tIcon = new TrayIcon(tImage.getScaledInstance(tIconWidth, -1, Image.SCALE_SMOOTH), "STARsearch");
				
					//create popup menu
					PopupMenu pMenu = new PopupMenu();
					
					MenuItem aboutItem = new MenuItem("About");
					aboutItem.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							JOptionPane.showMessageDialog(null, "STARsearch" + System.lineSeparator() 
									+ "©2015 S.T.A.R. Developement Team" + System.lineSeparator() 
									+ System.lineSeparator() 
									+ "Built as a project for CS3141 \"Team Software Project\" at Michigan Technological University." +System.lineSeparator() 
									+ "This simple program allows you to type some inputs into its popup search box and will promptly search for it."
									, "About!", JOptionPane.INFORMATION_MESSAGE);
						}
					});
					
					MenuItem exitItem = new MenuItem("Exit");
					exitItem.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							System.exit(0);
						}
					});
					
					pMenu.add(aboutItem);
					pMenu.add(exitItem);
					tIcon.setPopupMenu(pMenu);
					
					//add icon to system tray
					SystemTray.getSystemTray().add(tIcon);
					
					
				} catch (AWTException e) {
					//create a popup error
					System.err.println(e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			//TODO: add keyboard triggers
			//show search window
			mySearchView.setVisible(true);



		}

	}
	
	
}
