import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.junit.Test;

public class StarTest {
	
	@Test
	  public void testSearch() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
	    Model model = new Model();
	    
	    PrintWriter writer = new PrintWriter("randomFile.txt", "UTF-8");
	    writer.println("The first line");
	    writer.println("The second line");
	    writer.close();
	    
	    model.SearchFor("randomFile");
	    
	    
	    //assertEquals("1+2+3 must be 6", 6.0, tester.sumArray(testArr),1.0);
	  }

}
