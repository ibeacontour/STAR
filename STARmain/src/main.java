import java.awt.GraphicsEnvironment;


public class main {
  // herro, I am a placeholder!
  
  //instantiate view class
  static view mySearchView = new view();
  
  public static void main(String[] args){
    
    //BEGIN VIEW
    //check for a windowed environment
    if (!GraphicsEnvironment.isHeadless()) {
      //add a tray item of sorts
      
      //TODO: add keyboard triggers
      //show search window
      mySearchView.setVisible(true);
    }
  }
}
