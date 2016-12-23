package Util;

import java.io.File;
import java.io.IOException;
import java.util.Queue;

class LogWriter implements Runnable {
	long ttw;
	File logFile;
	Logger log;

	public LogWriter(File logFileLocation, long ttw, Logger log) {
		logFile = logFileLocation;
		this.ttw = ttw;
		this.log = log;
	}
	
	@Override
	public void run() {
		while (true) {
			Queue<LogLineStorage> wb = log.getWriteBuffer();
			if (wb != null) {
				synchronized(wb){ //Non atomic calls to wb, not really necessary 
					if (wb.isEmpty()) {
						try {
							Thread.sleep(ttw);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						if (logFile == null)
							return;
	
						try {
							FileUtil.writeToFile(wb.poll().toWrite(), logFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

}