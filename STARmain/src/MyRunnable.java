import java.io.File;
import java.nio.file.attribute.*;
import java.util.ArrayList;

public class MyRunnable implements Runnable{
	volatile boolean finished = false;
	volatile boolean running = true;
	String theFile;
	Model model;
	ArrayList<File> stuff = new ArrayList<File>();
	// Here, simple mode is true for simple, false for advanced (full)
	// Please don't use full unless you REALLY want to search for a while
	Boolean simpleMode = true;
	// Here, -1 indicated that there is no limit to depth in directory searches. This is the default.
	int dirDepth = 2;

	// 
	public MyRunnable(String theFileToFind, Model theModel) {
		// delete these when things start to work
		// just a demo arraylist to show that something can work
		theFile = theFileToFind;
		model = theModel;
	}

	// main heavy lifting method that likes a string to look for
	//public void run(String theFile) {
	public void run() {
		finished = false;
		stuff.clear();
		//System.out.println("I get here");

		// Here is the search stuff
		File[] roots;
		File tempDir;
		File[] inDir;
		ArrayList<File> rootDir = new ArrayList<File>();
		ArrayList<File> results = new ArrayList<File>();
		File fsRootPrimary = null;
		String osName = "";
		String search = theFile;


		// Looks at the filesystem roots and determines which one to search
		roots = File.listRoots();
		for (File f:roots) {
			if(finished == true) {
				return;
			}
			if (f.getAbsolutePath().startsWith("C")) {
				fsRootPrimary = f;
				//System.out.println("Found the C:\\ Drive");
				//System.out.println(f.getAbsolutePath());
			} else if (f.getAbsolutePath().startsWith("/")) {
				fsRootPrimary = f;
				//System.out.println("Found the / root (UNIX/LINUX)");
				//System.out.println(f.getAbsolutePath());
			}	
		}

		// Check that we have a real fs root directory
		if (fsRootPrimary == null) {
			System.exit(1);
		}

		if (simpleMode) {
			// Start sub-directory search for simple mode 
			// New strategy is to search first the user.home directory, and then the program files and x86 on windows, and the /bin folder on linux
			// First to search is the users folder, should be platform agnostic
			tempDir = new File(System.getProperty("user.home"));
			System.out.println("Home dir: " + tempDir.getAbsolutePath());

			if (tempDir.exists()) {
				inDir = tempDir.listFiles(); 
				for (File g:inDir) {
					if(finished == true) {
						return;
					}
					ArrayList<File> temp = searchDir(g, search, 0);
					if (temp != null) {
						results.addAll(temp);
					}
				}
			}

			//		tempDir = fsRootPrimary.listFiles();
			//		// DEBUG
			//		for (File f:tempDir) {
			//			if(finished == true) {
			//				return;
			//			}
			//			//System.out.println("Added " + f.getAbsolutePath() + " to arrayList");
			//		}

			if (System.getProperty("os.name").startsWith("Windows")) {
				// Search the 64-bit binary folder on windows, if it exists
				tempDir = new File(System.getenv("ProgramFiles"));
				System.out.println("Program Files (64): " + tempDir.getAbsolutePath());

				if (tempDir.exists()) {
					inDir = tempDir.listFiles();
					for (File g:inDir) {
						if (finished == true) {
							return;
						}
						ArrayList<File> temp = searchDir(g, search, 0);
						if (temp != null) {
							results.addAll(temp);
						}
					}
				}

				// Search the 32-bit binary folder on windows, if it exists
				tempDir = new File(System.getenv("ProgramFiles") + " " + "(x86)");
				System.out.println("Program Files (x86): " + tempDir.getAbsolutePath());

				if (tempDir.exists()) {
					inDir = tempDir.listFiles();
					for (File g:inDir) {
						if (finished == true) { 
							return;
						}
						ArrayList<File> temp = searchDir(g, search, 0);
						if (temp != null) {
							results.addAll(temp);
						}
					}
				}

			} else if (System.getProperty("os.name").startsWith("Linux")) {

				// executables folder for linux
				tempDir = new File("/usr/bin");
				System.out.println("Linux $PATH: " + tempDir.getAbsolutePath());
				
				if (tempDir.exists()) {
					inDir = tempDir.listFiles();
					for (File g:inDir) {
						if (finished == true) {
							return;
						}
						ArrayList<File> temp = searchDir(g, search, 0);
						if (temp != null) {
							results.addAll(temp);
						}
					}
				}
				

			}
			// if simplemode end
		} else {
			// Advanced, or full search, search every single thing, in every single file system root.
			for (File f:roots) {
				if (finished == true) {
					return;
				}
				ArrayList<File> temp = searchDir(f, search, 0);
				if (temp != null) {
					results.addAll(temp);
				}
			}
		}

		//		osName = System.getProperty("os.name");
		//
		//		if (osName.startsWith("Windows")) {
		//
		//			for (File f:rootDir) {
		//				if(finished == true) {
		//					return;
		//				}
		//				/*if (f.getName().startsWith("Program Files") && f.isDirectory()) {
		//					//System.out.println("Found a Program Files directory (Windows)");
		//					ArrayList<File> temp = this.searchDir(f, search);
		//					if (temp != null) {
		//						for (File g:temp) {
		//							results.add(g);
		//						}
		//					}
		//				} else*/ if (f.getName().startsWith("Users") && f.isDirectory()) {
		//					//System.out.println("Found a Users directory (Windows)");
		//					ArrayList<File> temp = this.searchDir(f, search);
		//					if (temp != null) {
		//						for (File g:temp) {
		//							results.add(g);
		//						}
		//					}
		//				} // Does not search any other than these directories at this stage
		//			}
		//
		//		} else if (osName.startsWith("Linux")) {
		//			// Come up with some folder cases to check first
		//			for (File f:rootDir) {
		//				if(finished == true) {
		//					return;
		//				}
		//				if (f.getName().startsWith("home") && f.isDirectory()) {
		//					System.out.println("Found a home directory (Linux)");
		//					ArrayList<File> temp = this.searchDir(f, search);
		//					if (temp != null) {
		//						for (File g:temp) {
		//							results.add(g);
		//						}
		//					}
		//				} else if ( f.getName().startsWith("bin") && f.isDirectory()) {
		//					System.out.println("Found a bin directory (Linux)");
		//					ArrayList<File> temp = this.searchDir(f, search);
		//					if (temp != null) {
		//						for (File g:temp) {
		//							results.add(g);
		//						}
		//					}
		//				}
		//			}
		//
		//		}

		// Print some Results
		System.out.println();
		System.out.println("-----_RESULTS_-----");
		for (File f:results) {
			if(finished == true) {
				return;
			}
			System.out.println("Found a match in: " + f.getAbsolutePath());
			stuff.add(f);
		}

		model.setResults(results);



	}

	// getter function
	public ArrayList<File> getStuff() {
		return stuff;
	}

	public ArrayList<File> searchDir(File dir, String search, int depth) {
		File[] inThisDir;
		ArrayList<File> matchesInDir = new ArrayList<File>();

		// null checks on input
		if (dir == null) {
			return null;
		} else if (depth >= dirDepth && !(dirDepth < 0)) {
			// If we aren't in free default depth mode, kill the recursion if we go over the depth specified. 
			// Note that this method can return null, while it handles that recursively it may act strangely
			return null;
		} else {
			inThisDir = dir.listFiles(); 
			System.out.println("Entered a dir: " + dir.getAbsolutePath());
		}

		//System.out.println("Entered a directory called: " + dir.getAbsolutePath());

		// quick check to make sure there were actually files found in this dir
		if (inThisDir == null) {
			return null;
		}

		for (File f:inThisDir) {
			if (f.getName().contains(search) && !f.isDirectory()) {
				// Add this match to the results list
				//System.out.println("Added " + f.getName() + " to results");
				matchesInDir.add(f);
			} else {
				// if a directory, recurse
				if (f.isDirectory()) {
					ArrayList<File> temp = searchDir(f, search, depth + 1);
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

	public void terminate() {
		finished = true;
	}


}
