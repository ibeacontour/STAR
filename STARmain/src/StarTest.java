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
    ArrayList<File> stuff;
    String f1 = "randomFile.txt";
    String f2 = "anotherRandomFile.txt";
    
	
	@Test
	// tests for basic testing functionality, basically if it will find the file or at
	// least any related files
	  public void basicTestSearch() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
	    
		// set up model/controller
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
	    
	    // simple file search for f1
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
	// test that tests if a thread can be cancelled and a new one stated without errors
	  public void advancedTestSearch() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
	
		// set up model and controller
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
	    // we want it to find f2, not f1
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
	// test that tests to see if it can execute an executable
	  public void executeTest() throws InterruptedException, IOException {
		
		// try to execute a copy of notepad that is in the library
		Process p = Runtime.getRuntime().exec("notepad.exe");
		p.waitFor();
		
		// if programs come up, it works!
		
		
		
	}
	
	
	
	
	
	
}
