import java.util.ArrayList;


public class Controller {
	Model model;
	view view;
	ArrayList<String> results;
	
	public Controller() {
		
	}
	
	public void setModel(Model m) {
		model = m;
	}
	
	public void setView(view v) {
		view = v;
	}
	
	public ArrayList<String> getIntermediateSearchResults() {
		return model.getResults();
	}
	
	public void refreshSearchResults (ArrayList<String> rslt) {
		// view.setResultBox(rslt);
	}
	
	// the view will call this method whenever it wants to start a new search with a String it has
	 public void newSearch(String fileToSearch) throws InterruptedException {
		 if(model.t1.isAlive()) {
			 model.interruptSearch();
		 }
		 model.SearchFor(fileToSearch);
	 
	 }
	 

}
