import javax.swing.*;
import java.awt.Toolkit.*;

public class view extends JFrame {
  //create the controls to be placed on the form
  JPanel myPanel;
  JTextField searchField;
  
  public view() {
    //TODO: check for headless environment, add multiple desktop handling
    // also set default size to some same ratio of the current desktops size
    this.setSize(512, 256);
    
    //initialize controls
    myPanel = new JPanel();
    searchField = new JTextField("Type to Search");
    
    //add controls to actual JFrame
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    myPanel.add(searchField);
    this.add(myPanel);
  }
  
}
