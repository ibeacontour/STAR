import java.util.ArrayList;


public class Controller {
	Model model;
	view view;
	
	public Controller() {
		
	}
	
	public void setModel(Model m) {
		model = m;
	}
	
	public void setView(view v) {
		view = v;
	}
	
	// the view will call this method whenever it wants to start a new search with a String it has
	 public ArrayList<String> newSearch(String fileToSearch) throws InterruptedException {
		 model.interruptSearch();
		 model.SearchFor(fileToSearch);
		 
		return null;
	 
	 
	 
	 }
	 

}
