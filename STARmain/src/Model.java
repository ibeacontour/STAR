import java.util.ArrayList;


public class Model {
	ArrayList<String> results = new ArrayList<String>();
	Controller controller;
	Thread t1;

	// blank constructor
	public Model() {
		t1 = new Thread();

	}

	// function that runs the thread then prints the results it gets from the thread
	public ArrayList<String> SearchFor(String fileNameToFind) throws InterruptedException {
		MyRunnable run = new MyRunnable(fileNameToFind);
		t1.interrupt();
		// while loop that halts the program until t1 has time to get interrupted
		while(t1.isAlive()){

		}
		// run the search
		t1 = new Thread(run);
		t1.start();
		// while loop that halts the main program until the search is completed or interrupted
		while(t1.isAlive()){
			// if the thread is interrupted (most likely from a new search) stop and return null and let the next search complete
			// change this code if we want it to send partial results when interrupted
			if(t1.isInterrupted() == true) {
				return null;
			}
		}
		// once thread is done, get the results from it
		results = run.getStuff();

		// print results
		// for testing purposes
		for(int i = 0;i < results.size();i++) {
			System.out.println(" " + results.get(i));
		}
		// pass results from the thread search to controller
		return results;
	}

	public void interruptSearch() {
		if(t1.isAlive()) {
			t1.interrupt();
		}
	}

	public void setController(Controller c) {
		controller = c;
	}
}
