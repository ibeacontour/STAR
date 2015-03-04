import java.util.ArrayList;


public class Model {
	ArrayList<String> results = new ArrayList<String>();
	Controller controller;
	Thread t1;
	
	// blank constructor
	public Model() {
		// Commit Testing, please ignore
		// Still trying to figure out EGit
	}

	// function that runs the thread then prints the results it gets from the thread
	public ArrayList<String> SearchFor(String fileNameToFind) throws InterruptedException {
		MyRunnable run = new MyRunnable(fileNameToFind);
		while(!t1.isInterrupted() || !t1.isAlive()){
			
		}
		t1 = new Thread(run);
		t1.start();
		t1.sleep(5000);
		
		
		results = run.getStuff();
		
		for(int i = 0;i < results.size();i++) {
			System.out.println(" " + results.get(i));
		}
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
