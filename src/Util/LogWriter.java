package Util;

import java.io.File;
import java.io.IOException;

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
			if (log.getWriteBuffer() != null) {
				if (log.getWriteBuffer().isEmpty()) {
					try {
						Thread.sleep(ttw);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					if (logFile == null)
						return;

					try {
						FileUtil.writeToFile(log.getWriteBuffer().remove(0).toWrite(), logFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}