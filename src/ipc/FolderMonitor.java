package ipc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import log.Logger;

public class FolderMonitor implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private static final int SCAN_INTERVAL_IN_MS = 1000;

	private File parentDir;
//	private List<File> directories;
	private Set<File> knownDirectories;
//	private List<String[]> readFiles;

	private boolean active = false;

	public FolderMonitor(File parent, Set<File> knownFiles) {
		parentDir = parent;
		knownDirectories = knownFiles;
//		readFiles = new ArrayList<>();
	}

	@Override
	public void run() {
		active = true;
		log.info("Starting continuous operations");

		while (active) {
			scan();
//			readFiles();
			try {
				Thread.sleep(SCAN_INTERVAL_IN_MS);
			} catch (InterruptedException e) {
				log.error("interrupted");
				log.exception(e);
			}
		}

	}

//	public void readFiles() {
//		if (files.isEmpty())
//			return;
//		log.info("Starting file reading");
//		List<File> filesToRemove = new ArrayList<>();
//		for (File f : files) {
//			log.debug("Checking if file: " + f.getName() + " is readable: " + f.canRead());
//			if (f.canRead()) {
//				log.debug("Reading file: " + f.getName());
//				try (FileChannel fc = new RandomAccessFile(f, "rw").getChannel();) {
//					FileLock fl = null;
//
//					fl = fc.tryLock();
//					if (fl != null) {
//						log.debug("Locked file: " + f.getName());
//
//						String[] fileCont = util.FileUtil.readFromFile(f);
//
//						for (String s : fileCont) {
//							log.debug("Looking for end tag in: " + f.getName());
//							if (s.contains("END")) {
//								log.debug("End found in: " + f.getName());
//								readFiles.add(fileCont);
//								makeCheckedFile(f);
//								filesToRemove.add(f);
//							}
//						}
//
//						fl.release();
//					}
//				} catch (IOException e) {
//					log.exception(e);
//				} catch (OverlappingFileLockException e) {
//					log.debug("File: " + f.getName() + " is already locked");
//				}
//
//			}
//		}
//		files.removeAll(filesToRemove);
//	}

//	public boolean makeCheckedFile(File f) {
//		boolean toReturn = false;
//		log.debug("Trying to mark: " + f.getName() + " as read");
//
//		File newName = new File(f.getAbsolutePath() + ".read");
//		log.debug("Renaming: " + f.getAbsolutePath() + " to: " + newName.getAbsolutePath());
//		f.renameTo(newName);
//		toReturn = true;
//		log.debug("Successfully marked: " + f.getName() + " as read");
//
//		return toReturn;
//	}

	public void scan() {
		log.trace("Scanning files in: " + parentDir.getAbsolutePath());
		File[] filesTMP = parentDir.listFiles();

		for (File devIdFolder : filesTMP) {
			if (devIdFolder.isDirectory()) {
				log.trace("DeviceId folder found: " + devIdFolder.getAbsolutePath());
				for(File comIdFolder : devIdFolder.listFiles()){
					if(comIdFolder.isDirectory()){
						log.trace("ComId folder found: " + comIdFolder.getAbsolutePath());
						if(!knownDirectories.contains(comIdFolder)){
							
							//							log.trace("Folder is new, adding to known collection: " + comIdFolder.getAbsolutePath());
							knownDirectories.add(comIdFolder);
						}
					}
				}
			}
		}
	}
	
//	private boolean compareSubFolders(File file, File otherFile){
//		Pattern p = Pattern.compile("\\"+File.separator + "(\\w{1}" + "\\)" + File.separator);
//		try {
//			Matcher fileM = p.matcher(file.getCanonicalPath());
//			Matcher otherM = p.matcher(otherFile.getCanonicalPath());
//			
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		return active;
//	}

	public void stop() {
		log.info("Stopping");
		active = false;
	}
}
