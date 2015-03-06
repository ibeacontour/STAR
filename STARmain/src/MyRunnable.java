import java.io.File;
import java.nio.file.attribute.*;
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
			
			// Here is the search stuff
			File[] roots;
			File[] tempDir;
			ArrayList<File> rootDir = new ArrayList<File>();
			ArrayList<File> results = new ArrayList<File>();
			File fsRootPrimary = null;
			String osName = "";
			String search = theFile;

			// Looks at the filesystem roots and determines which one to search
			roots = File.listRoots();
			for (File f:roots) {
				if (f.getAbsolutePath().startsWith("C")) {
					fsRootPrimary = f;
					System.out.println("Found the C:\\ Drive");
					System.out.println(f.getAbsolutePath());
				} else if (f.getAbsolutePath().startsWith("/")) {
					fsRootPrimary = f;
					System.out.println("Found the / root (UNIX/LINUX)");
					System.out.println(f.getAbsolutePath());
				}	
			}

			if (fsRootPrimary == null) {
				System.exit(1);
			}

			// Start sub-directory search, 
			tempDir = fsRootPrimary.listFiles();
			// DEBUG
			for (File f:tempDir) {
				System.out.println("Added " + f.getAbsolutePath() + " to arrayList");
			}

			for (File g:tempDir) {
				rootDir.add(g);
			}

			osName = System.getProperty("os.name");

			if (osName.startsWith("Windows")) {

				for (File f:rootDir) {
					if (f.getName().startsWith("Program Files") && f.isDirectory()) {
						System.out.println("Found a Program Files directory (Windows)");
						ArrayList<File> temp = this.searchDir(f, search);
						if (temp != null) {
							for (File g:temp) {
								results.add(g);
							}
						}
					} else if (f.getName().startsWith("Users") && f.isDirectory()) {
						System.out.println("Found a Users directory (Windows)");
						ArrayList<File> temp = this.searchDir(f, search);
						if (temp != null) {
							for (File g:temp) {
								results.add(g);
							}
						}
					} // Does not search any other than these directories at this stage
				}

			} else if (osName.startsWith("Linux")) {
				// Come up with some folder cases to check first
				for (File f:rootDir) {
					if (f.getName().startsWith("home") && f.isDirectory()) {
						System.out.println("Found a home directory (Linux)");
						ArrayList<File> temp = this.searchDir(f, search);
						if (temp != null) {
							for (File g:temp) {
								results.add(g);
							}
						}
					} else if ( f.getName().startsWith("bin") && f.isDirectory()) {
						System.out.println("Found a bin directory (Linux)");
						ArrayList<File> temp = this.searchDir(f, search);
						if (temp != null) {
							for (File g:temp) {
								results.add(g);
							}
						}
					}
				}
				
			}

			// Print some Results
			System.out.println();
			System.out.println("-----_RESULTS_-----");
			for (File f:results) {
				System.out.println("Found a match in: " + f.getAbsolutePath());
			}
			
				
			 finished = true;
			
		}
		
	}

	// getter function
	public ArrayList<String> getStuff() {
		return stuff;
	}
	
	public ArrayList<File> searchDir(File dir, String search) {
		File[] inThisDir;
		ArrayList<File> matchesInDir = new ArrayList<File>();

		// null checks on input
		if (dir == null) {
			return null;
		} else {
			inThisDir = dir.listFiles(); 
		}

		System.out.println("Entered a directory called: " + dir.getAbsolutePath());

		// quick check to make sure there were actually files found in this dir
		if (inThisDir == null) {
			return null;
		}

		for (File f:inThisDir) {
			if (f.getName().startsWith(search)) {
				// Add this match to the results list
				System.out.println("Added " + f.getName() + " to results");
				matchesInDir.add(f);
			} else {
				// if a directory, recurse
				if (f.isDirectory()) {
					ArrayList<File> temp = searchDir(f, search);
					// Add any matches in here to our results list 
					if (temp != null) {
						for (File g:temp) {
							matchesInDir.add(g);
						}
					}
				}
			}

		}


		return matchesInDir;
	}
	
	
}
