import java.util.ArrayList;
import java.awt.GraphicsEnvironment;

public class STARmain  {
	static view mySearchView = new view();
	public static void main(String[] args) throws InterruptedException  {

		// creates a model then does a search
		Model model = new Model();
		model.SearchFor("AA");

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
