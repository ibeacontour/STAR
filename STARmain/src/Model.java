import java.util.ArrayList;
import java.io.File;


public class Model {
	ArrayList<File> results = new ArrayList<File>();
	Controller controller;
	Thread t1;
	MyRunnable run;

	// blank constructor
	public Model() {
		t1 = new Thread();

	}

	// function that runs the thread that will search for possible files
	public void SearchFor(String fileNameToFind) throws InterruptedException {
		
		run = new MyRunnable(fileNameToFind, this);
		System.out.println("I started a new search");
		
		// run the search
		t1 = new Thread(run);
		t1.setPriority(Thread.MIN_PRIORITY);
		t1.start();

	}

	public void interruptSearch() throws InterruptedException {
		if(t1.isAlive()) {
			System.out.println("stopping");
			run.terminate();
			System.out.println("stop success");
		}
	}

	public void setController(Controller c) {
		controller = c;
	}
	
	public ArrayList<File> getResults() {
		return results;
	}
	
	public void setResults(ArrayList<File> newResults) {
		results = newResults;
		
	}
	
}
