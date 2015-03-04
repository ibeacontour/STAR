import java.util.ArrayList;
import java.awt.GraphicsEnvironment;

public class STARmain  {
	static view mySearchView = new view();
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

			//TODO: add keyboard triggers
			//show search window
			mySearchView.setVisible(true);



		}

	}
	
	
}
