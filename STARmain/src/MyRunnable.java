import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MyRunnable implements Runnable{
	volatile boolean finished = false;
	volatile boolean running = true;
	String theFile;
	Model model;
	ArrayList<File> stuff = new ArrayList<File>();
	// Here, simple mode is true for simple, false for advanced (full)
	// Please don't use full unless you REALLY want to search for a while
	Boolean simpleMode;
	// Here, -1 indicated that there is no limit to depth in directory searches. This is the default.
	int dirDepth;

	// 
	public MyRunnable(String theFileToFind, Model theModel,int dD,boolean sM) {
		// delete these when things start to work 
		// just a demo arraylist to show that something can work
		theFile = theFileToFind;
		model = theModel;
		dirDepth = dD;
		simpleMode = sM;
	}

	// main heavy lifting method that likes a string to look for
	//public void run(String theFile) {
	public void run() {
		finished = false;
		stuff.clear();
		//System.out.println("I get here");

		// Containers for everything we need to handle in here
		File[] roots;
		File tempDir;
		File[] inDir;
		//ArrayList<File> rootDir = new ArrayList<File>();
		ArrayList<File> results = new ArrayList<File>();
		ArrayList<File> temp;
		File fsRootPrimary = null;
		//String osName = "";
		String search = theFile;


		// Do a quick check of the file history map, and if it contains any matches to our query, if so add them and push them right away. 
		HashMap<String, File> history = model.getHistory();
		if (!history.isEmpty()) {
			for (String s:history.keySet()) {
				if (s.contains(search) || s.startsWith(search)) {
					results.add(history.get(s));
				}
			}
		}
		// now push these to the view ahead of the brute force search
		model.setResults(results);

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

			// This shortened search for some reason doesn't work, so the longer one will have to work for now.
			//			temp = searchDir(tempDir, search, 0);
			//			if (temp != null) {
			//				// Add only those that weren't added from the history already.
			//				for (File h:temp) {
			//					if (!results.contains(h)) {
			//						results.add(h);
			//					}
			//				}
			//			}

			if (tempDir.exists()) {
				inDir = tempDir.listFiles(); 
				for (File g:inDir) {
					if(finished == true) {
						return;
					}
					temp = searchDir(g, search, 0);
					if (temp != null) {
						// Add only those that weren't added from the history already.
						for (File h:temp) {
							if (!results.contains(h)) {
								results.add(h);
							}
						}
					}
				}
			}

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
						temp = searchDir(g, search, 0);
						if (temp != null) {
							// Add only those that weren't added from the history already.
							for (File h:temp) {
								if (!results.contains(h)) {
									results.add(h);
								}
							}
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
						temp = searchDir(g, search, 0);
						if (temp != null) {
							// Add only those that weren't added from the history already.
							for (File h:temp) {
								if (!results.contains(h)) {
									results.add(h);
								}
							}
						}
					}
				}

			} else if (System.getProperty("os.name").startsWith("Linux")) {

				// executables folder(s) for linux
				tempDir = new File("/usr/bin");
				System.out.println("Linux executable path: " + tempDir.getAbsolutePath());

//				if (tempDir.exists()) {
//					inDir = tempDir.listFiles();
//					for (File g:inDir) {
//						if (finished == true) {
//							return;
//						}
//						temp = searchDir(g, search, 0);
//						if (temp != null) {
//							// Add only those that weren't added from the history already.
//							for (File h:temp) {
//								if (!results.contains(h)) {
//									results.add(h);
//								}
//							}
//						}
//					}
//				}
				temp = searchDir(tempDir, search, 0);
				if (temp != null) {
					// Add only those that weren't added from the history already.
					for (File h:temp) {
						if (!results.contains(h)) {
							results.add(h);
						}
					}
				}

				tempDir = new File("/bin");
				System.out.println("Linux executable path: " + tempDir.getAbsolutePath());

				//				if (tempDir.exists()) {
				//					inDir = tempDir.listFiles();
				//					for (File g:inDir) {
				//						if (finished == true) {
				//							return;
				//						}
				//						temp = searchDir(g, search, 0);
				//						if (temp != null) {
				//							// Add only those that weren't added from the history already.
				//							for (File h:temp) {
				//								if (!results.contains(h)) {
				//									results.add(h);
				//								}
				//							}
				//						}
				//					}
				//				}
				temp = searchDir(tempDir, search, 0);
				if (temp != null) {
					// Add only those that weren't added from the history already.
					for (File h:temp) {
						if (!results.contains(h)) {
							results.add(h);
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
				temp = searchDir(f, search, 0);
				if (temp != null) {
					// Add only those that weren't added from the history already.
					for (File h:temp) {
						if (!results.contains(h)) {
							results.add(h);
						}
					}
				}
			}
		}

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
		model.finishSearch();

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
		} else if (depth >= dirDepth && dirDepth >= 0) {
			// If we aren't in free default depth mode, kill the recursion if we go over the depth specified. 
			// Note that this method can return null, and while it handles that recursively, it may act strangely
			return null;
		} else {
			inThisDir = dir.listFiles(); 
			//System.out.println("Entered a dir: " + dir.getAbsolutePath());
		}

		//System.out.println("Entered a directory called: " + dir.getAbsolutePath());

		// quick check to make sure there were actually files found in this dir
		if (inThisDir == null) {
			return null;
		}

		for (File f:inThisDir) {
			if ((f.getName().contains(search) || f.getName().startsWith(search)) && !f.isDirectory()) {
				// Add this match to the results list, if it isn't already in the results.
				//System.out.println("Added " + f.getName() + " to results");
				matchesInDir.add(f);


			} else if (f.isDirectory()){
				// if a directory, recurse
				ArrayList<File> temp = searchDir(f, search, depth + 1);
				// Add any matches in here to our results list 
				if (temp != null) {
					for (File g:temp) {
						matchesInDir.add(g);
					}
				}
			} else {
				Path symPath = null;
				try {
					symPath = Paths.get(f.toURI());
				} catch (InvalidPathException e) {
					// If the path comes up to be invalid, we need to skip this file
					//e.printStackTrace();
					//System.out.println("Bad path, moving on: " + f.getAbsolutePath());
					continue;
				}
				if (Files.isSymbolicLink(symPath)) {
					// we have a symbolic link, see what the real file is and if it matches.
					File sym = null;
					try {
						sym = Files.readSymbolicLink(symPath).toFile();
						//System.out.println("Resolved a symlink, from : " + symPath.toString() + " to : " + sym);
					} catch (IOException e) {
						// Something messed up
						//System.out.println("Something messed up trying to resolve a symbolic link");
						e.printStackTrace();
					}
					if (sym != null && !sym.isDirectory()) {
						if (sym.getName().contains(search) || sym.getName().startsWith(search)) {
							matchesInDir.add(sym);
						}
					} else if (sym.isDirectory()) {
						ArrayList<File> temp;
						temp = searchDir(sym, search, depth+1);
						if (temp != null) {
							for (File g:temp) {
								matchesInDir.add(g);
							}
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
