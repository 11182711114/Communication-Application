package ipc;

import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FolderMonitor implements Runnable{
	private util.Logger log = util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();
	
	private static final int SCAN_INTERVAL_IN_MS = 1000;
	
	private File parentDir;
	private List<File> files;
	private List<String[]> readFiles;
	
	private boolean active = false;
	
	public FolderMonitor(File parent){
		parentDir = parent;
		files = new ArrayList<>();
		readFiles = new ArrayList<>();
	}

	@Override
	public void run() {
		active = true;
		log.info("Starting continuous operations", nameForLog);
		
		while(active){
			scan();
			readFiles();
			try {
				Thread.sleep(SCAN_INTERVAL_IN_MS);
			} catch (InterruptedException e) {
				log.error("interrupted", nameForLog);
				log.exception(e);
			}
		}
		
	}
	
	public void readFiles(){
		if(files.isEmpty())
			return;
		log.info("Starting file reading", nameForLog);
		List<File> filesToRemove = new ArrayList<>();
		for(File f : files){
			log.debug("Checking if file: " + f.getName() + " is readable: " + f.canRead(), nameForLog);
			if(f.canRead()){
				log.debug("Reading file: " + f.getName(), nameForLog);
				try (FileChannel fc = new RandomAccessFile(f,"rw").getChannel();){
					FileLock fl = null;
					
					fl = fc.tryLock();
					if(fl != null){
						log.debug("Locked file: " + f.getName(), nameForLog);
						
						String[] fileCont = util.FileUtil.readFromFile(f);			
						
						for(String s : fileCont){
							log.debug("Looking for end tag in: " + f.getName(), nameForLog);
							if(s.contains("END")){
								log.debug("End found in: " + f.getName(), nameForLog);
								readFiles.add(fileCont);
								makeCheckedFile(f);
								filesToRemove.add(f);
							}
						}
												
						fl.release();
					}
				} catch (IOException e) {
					log.exception(e);
				}catch(OverlappingFileLockException e){
					log.debug("File: " + f.getName() + " is already locked", nameForLog);
				}
				
			}
		}
		files.removeAll(filesToRemove);
	}
	
	public boolean makeCheckedFile(File f){
		boolean toReturn = false;
		log.debug("Trying to mark: " + f.getName() + " as read", nameForLog);
		
		File parent = f.getParentFile();
		File newName = new File(f.getAbsolutePath()+".read");
		log.debug("Renaming: " + f.getAbsolutePath() + " to: " + newName.getAbsolutePath(), nameForLog);
		f.renameTo(newName);
		toReturn = true;
		log.debug("Successfully marked: " + f.getName() + " as read", nameForLog);
		
		return toReturn;
	}
	
	public void scan(){
		log.trace("Scanning files in: " + parentDir.getAbsolutePath(), nameForLog);
		File[] filesTMP = parentDir.listFiles();
		
		for(File f : filesTMP){
			boolean fileIsMarkedRead = f.getName().contains(".read");
			if(f.isFile() && f.canWrite() && !fileIsMarkedRead && !files.contains(f)){ // FIXME File locking stuff
				log.debug("File found: " + f.getAbsolutePath(), nameForLog);
				files.add(f);
			}
		}
	}
	public void stop(){
		log.info("Stopping", nameForLog);
		active = false;
	}
}
