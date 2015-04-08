import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.junit.Test;

public class StarTest {
	
	Boolean foundExactFile = false;
	Model model = new Model();
	Controller controller = new Controller();
	static view mySearchView = new view();
	//model.setController(controller);
	//mySearchView.setController(controller);
	//controller.setModel(model);
	//controller.setView(mySearchView);
    ArrayList<File> stuff;
    String f1 = "randomFile.txt";
    String f2 = "anotherRandomFile.txt";
    
	
	@Test
	  public void basicTestSearch() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
	    
		model.setController(controller);
		mySearchView.setController(controller);
		controller.setModel(model);
		controller.setView(mySearchView);
		
		// use printer writers to make a couple test files
	    PrintWriter writer = new PrintWriter(f1, "UTF-8");
	    writer.println("The first line");
	    writer.println("The second line");
	    writer.close();
	    
	    PrintWriter writer2 = new PrintWriter(f2, "UTF-8");
	    writer2.println("The first line");
	    writer2.println("The second line");
	    writer2.close();
	    
	    // simple file search
	    // join pauses program to wait until it's done for accurate results
	    model.SearchFor(f1);
	    model.t1.join();
	    
	    stuff = model.getResults();
	    
	    // test to see if the search was able to find ANYTHING
	    assertFalse("search should find something", stuff.isEmpty());
	    
	    // search for the exact file
	    for(File file:stuff) {
	    	if (file.getName().compareTo(f1) == 0) {
	    		foundExactFile = true;
	    		break;
	    	}
	    }
	    // test to see if the search was able to find the file
	    assertTrue("search should find the file somewhere in the results", foundExactFile);
	    
	    
	    
	  }
	@Test
	  public void advancedTestSearch() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
	
		model.setController(controller);
		mySearchView.setController(controller);
		controller.setModel(model);
		controller.setView(mySearchView);
		
		// use printer writers to make a couple test files
	    PrintWriter writer = new PrintWriter(f1, "UTF-8");
	    writer.println("The first line");
	    writer.println("The second line");
	    writer.close();
	    
	    PrintWriter writer2 = new PrintWriter(f2, "UTF-8");
	    writer2.println("The first line");
	    writer2.println("The second line");
	    writer2.close();
		
		// interupted search
	    // start one search then check to see if it tosses it when next one starts
		model.SearchFor(f1);
		model.SearchFor(f2);
		model.t1.join();
		
		stuff = model.getResults();
		
		// look to see if it found the second searched file, not first
		for(File file:stuff) {
	    	if (file.getName().compareTo(f2) == 0) {
	    		foundExactFile = true;
	    		break;
	    	}
	    }
	    // the search should abandon searching for f1 and find f2 instead
	    assertTrue("search should find the file somewhere in the results", foundExactFile);
		
	}
	
	@Test
	  public void executeTest() throws InterruptedException, IOException {
		model.setController(controller);
		mySearchView.setController(controller);
		controller.setModel(model);
		controller.setView(mySearchView);
		
	
		//Process p = Runtime.getRuntime().exec("notepad.exe");
		Process p = Runtime.getRuntime().exec("java -jar JunitExecTest.jar");
		p.waitFor();
		
		// if programs come up, it works!
		
		
		
	}
	
	
	
	
	
	
}
