import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;


public class Model {
	ArrayList<File> results = new ArrayList<File>();
	Controller controller;
	Thread t1;
	MyRunnable run;
	int dirDepth;
	Boolean simpleMode;
	HashMap<String, File> history = new HashMap<String, File>();

	// blank constructor
	public Model() {
		t1 = new Thread();

	}

	// function that runs the thread that will search for possible files
	public void SearchFor(String fileNameToFind) throws InterruptedException {
		
		run = new MyRunnable(fileNameToFind, this,dirDepth,simpleMode);
		System.out.println("I started a new search");
		
		// run the search
		// set the priority to low so it doesn't starve other more time sensitive operations
		t1 = new Thread(run);
		t1.setPriority(Thread.MIN_PRIORITY);
		t1.start();

	}

	// sets a flag in the runnable that stops it (not instantaneous)
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
		controller.refreshSearchResults(results);
	}
	
	public void finishSearch() {
		controller.finishSearch();
	}
	
	public void genericExecuteFile(File f) throws InterruptedException {
		
		// Try to add this new file to the history table
		if (f != null) {
			history.put(f.getName(), f);
			
		}
		
		// check to see if the file is 'executable'
		if(f == null || !f.canExecute()) {
			System.out.println("Can not execute: " + f.getName());
			return;
		}
		
		// using runtime and exec's in java requires use of a process
		Process p;
		// the 'canExecute' check is insuffient, so a try/catch block is needed
		try {
			p = Runtime.getRuntime().exec(f.getAbsolutePath());
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("invalid file to run: " + f.getName());
		}
		
	}
	public void setDirDepth(int i) {
		dirDepth = i;
	}
	public void setSimpleMode(boolean b) {
		simpleMode = b;
	}
	
	public HashMap<String, File> getHistory() {
		if (history != null) {
			return history;
		} else {
			return new HashMap<String, File>();
		}
	}
}
