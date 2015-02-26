import java.util.ArrayList;


public class Model {
	String fileToFind;
	MyThread searchThread = new MyThread();
	ArrayList<String> results = new ArrayList<String>();
	
	// blank constructor
	public Model() {
		
	}

	// function that runs the thread then prints the results it gets from the thread
	public void SearchFor(String fileNameToFind) throws InterruptedException {
		fileToFind = fileNameToFind;
		searchThread.run(fileToFind);
		
		results = searchThread.getStuff();
		
		for(int i = 0;i < results.size();i++) {
			System.out.println(" " + results.get(i));
		}
		
	}
}
