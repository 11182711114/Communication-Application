package log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import log.Logger.LogLineStorage;
import util.FileUtil;

public class LogWriter implements Runnable {

	private static File logFileStatic;
	private static boolean append = true;
	
	private static class LogWriterHolder {
		private static final LogWriter INSTANCE = new LogWriter(logFileStatic, append);
	}

	public static LogWriter getInstance() {
		if (logFileStatic == null)
			return null;
		return LogWriterHolder.INSTANCE;
	}

	public static void setLogFile(File f) {
		logFileStatic = f;
	}
	public static void setAppend(boolean app){
		append = app;
	}

	private File logFile;
	private boolean active;

	private LinkedBlockingQueue<LogLineStorage> writeBuffer = new LinkedBlockingQueue<>();

	public LogWriter(File logFileLocation, boolean append) {
		logFile = logFileLocation;
	}

	@Override
	public void run() {
		active = true;
		if(!append){
			if(logFile.exists()){
				logFile.delete();
			}
		}
		while (active) {
			try {
				String toWrite = writeBuffer.take().toWrite();
				FileUtil.writeToFile(toWrite, logFile);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Queue<LogLineStorage> wb = log.getWriteBuffer();
			// if (wb != null) {
			// synchronized(wb){ //Non atomic calls to wb, not really necessary
			// if (wb.isEmpty()) {
			// try {
			// Thread.sleep(ttw);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// } else {
			// if (logFile == null)
			// return;
			//
			// try {
			// FileUtil.writeToFile(wb.poll().toWrite(), logFile);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			// }
			// }

		}
	}

	public void add(LogLineStorage lls) {
		writeBuffer.add(lls);
	}
}