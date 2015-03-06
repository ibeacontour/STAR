import java.util.ArrayList;

public class MyRunnable implements Runnable{
	boolean finished = false;
	volatile boolean running = true;
	String theFile;
	ArrayList<String> stuff = new ArrayList<String>();
	ArrayList<String> dummyDemo = new ArrayList<String>();
	
	// 
	public MyRunnable(String theFileToFind) {
		// delete these when things start to work
		// just a demo arraylist to show that something can work
		theFile = theFileToFind;
		dummyDemo.add("AAA");
		dummyDemo.add("AA");
		dummyDemo.add("A");
	}

	// main heavy lifting method that likes a string to look for
	//public void run(String theFile) {
	public void run() {
		//System.out.println("I get here");
		// when thread is interrupted or the search is done, this while condition will break and end the thread process
		while(!Thread.currentThread().isInterrupted() && finished == false) {
			// go through the dummy arraylist and compare strings it has with the string it was passed
			// replace these lines when real search is there
			for(int i = 0;i < dummyDemo.size();i++) {
				// a dummy comparison
				if(theFile.compareTo(dummyDemo.get(i)) >= 0) {
					stuff.add(dummyDemo.get(i));
				}
				
			} finished = true;
			
		}
		
	}

	// getter function
	public ArrayList<String> getStuff() {
		return stuff;
	}
	
}
