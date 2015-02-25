import java.util.ArrayList;


public class MyThread extends Thread{
	boolean stop = false;
	ArrayList<Integer> stuff = new ArrayList<Integer>();

	public void run() {
		while(stop == false) {
			stuff.add(1);
			
		}
		System.out.println("I stopped");
	}
	
	public void stopThread() {
		stop = true;
	}
	
	public ArrayList<Integer> getStuff() {
		return stuff;
	}
	
}
