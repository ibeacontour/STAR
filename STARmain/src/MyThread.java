import java.util.ArrayList;

public class MyThread extends Thread{
	boolean stop = false;
	ArrayList<String> stuff = new ArrayList<String>();
	ArrayList<String> dummyDemo = new ArrayList<String>();
	
	public MyThread() {
		// delete these when things start to work
		// just a demo arraylist to show that something can work
		dummyDemo.add("AAA");
		dummyDemo.add("AA");
		dummyDemo.add("A");
	}

	// main heavy lifting method that likes a string to look for
	public void run(String theFile) {
		// when thread.stopThread is called, this while condition will break and end the thread process
		while(stop == false) {
			// go through the dummy arraylist and compare strings it has with the string it was passed
			for(int i = 0;i < dummyDemo.size();i++) {
				if(theFile.compareTo(dummyDemo.get(i)) <= 0) {
					stuff.add(dummyDemo.get(i));
				}
			} stop = true;
			
		}
		// print out that tells when it stops
		System.out.println("I stopped");
	}
	// safely stops the thread
	public void stopThread() {
		stop = true;
	}
	// getter function
	public ArrayList<String> getStuff() {
		return stuff;
	}
	
}
