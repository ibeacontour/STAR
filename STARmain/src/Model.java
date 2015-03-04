import java.util.ArrayList;


public class Model {
	String fileToFind;
	ArrayList<String> results = new ArrayList<String>();
	
	// blank constructor
	public Model() {
		// Commit Testing, please ignore
		// Still trying to figure out EGit
	}

	// function that runs the thread then prints the results it gets from the thread
	public void SearchFor(String fileNameToFind) throws InterruptedException {
		ArrayList<String> results2;
		fileToFind = fileNameToFind;
		MyRunnable run = new MyRunnable("AA");
		Thread t1 = new Thread(run);
		t1.start();
		t1.sleep(1000);
		
		
		results = run.getStuff();
		
		for(int i = 0;i < results.size();i++) {
			System.out.println(" " + results.get(i));
		}
		//System.out.println(" " + results.size());
		//System.out.println(" " + results2.size());
		
	}
}
