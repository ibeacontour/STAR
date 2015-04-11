import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.jnativehook.*;
import org.jnativehook.keyboard.*;

public class STARmain implements NativeKeyListener {
	static view mySearchView = new view();
	static TrayIcon tIcon;
	
	static int spaceState = 0;
	static int controlState = 0;
	
	public static void main(String[] args) throws InterruptedException, InvalidFileFormatException, IOException  {

		// creates a model and controller
		// also links the model, view, and controller together in the MCV style
		Model model = new Model();
		Controller controller = new Controller();
		model.setController(controller);
		mySearchView.setController(controller);
		controller.setModel(model);
		controller.setView(mySearchView);
		boolean firstTime = true;
		
		// loads settings from an ini file
		// makes sure it only would load setting on start up
		if(firstTime == true) {
			firstTime = false;
			// create a file pointing to the ini file to make sure it exists or not
			File tempFile = new File("STAR.ini");
			// if the ini file does not exist, create one
			// by using a printwriting and project agreed base options
			if(!tempFile.exists()) {
				PrintWriter writer = new PrintWriter("STAR.ini", "UTF-8");
				writer.println("[basic options]");
				writer.println("simple mode = true");
				writer.println("directory depth = -1");
				writer.close();
				System.out.println("I created a new ini file");
			}
			// once the ini file is found/created, load the options
			// also, send the options to the model where the options will matter
			Wini ini = new Wini(new File("STAR.ini"));
			int dirDepth = ini.get("basic options","directory depth",int.class);
			Boolean simpleMode = ini.get("basic options","simple mode",boolean.class);
			model.setDirDepth(dirDepth);
			model.setSimpleMode(simpleMode);
		}

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
					mySearchView.setIconImage(tImage.getScaledInstance(tIconWidth, -1, Image.SCALE_SMOOTH));
					mySearchView.setTitle("STARsearch");
					
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
									+ "This simple program allows you to type some inputs into its popup search box and will promptly search for it." + System.lineSeparator()
									+ System.lineSeparator()
									+ "Icons made by Freepik from www.flaticon.com" + System.lineSeparator()
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
					
					//register global hook for key listener
					GlobalScreen.registerNativeHook();
					GlobalScreen.addNativeKeyListener(new STARmain());
					
					//disable verbose output
					Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
					logger.setLevel(Level.OFF);
					
				} catch (AWTException e) {
					//create a popup error
					System.err.println(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NativeHookException e) {
					JOptionPane.showConfirmDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
			
			//on exit
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run()
				{
					//remove native hook
					try {
						GlobalScreen.unregisterNativeHook();
					} catch (NativeHookException e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		//implement state as per detected key
		if (arg0.getKeyCode() == NativeKeyEvent.VC_CONTROL_L) {
			controlState = 1;
			System.out.println("control");
		} else if (arg0.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			spaceState = 1;
			System.out.println("space");
		}
		
		//if both keys are down we will be in state 2 and show the form
		if ((controlState > 0) && (spaceState > 0)) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mySearchView.setVisible(true);
				}
				
			});
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		//decrement states as keys are released
		if (arg0.getKeyCode() == NativeKeyEvent.VC_CONTROL_L) {
			controlState = 0;
		} else if (arg0.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			spaceState = 0;
		}
		//don't hide the form, it does it itself when it has focus and escape is hit		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
