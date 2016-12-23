package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {
	private static final long LOG_WRITER_TIME_TO_WAIT = 5 * 1000;

	private List<LogLineStorage> writeBuffer = new ArrayList<>();

	private LogWriter lw;
	private Thread lwThread;

	private static File logFileStatic;

	private Logger() {
		lw = new LogWriter(logFileStatic, LOG_WRITER_TIME_TO_WAIT, this);
		lwThread = new Thread(lw);
		lwThread.start();
	}

	private static class LoggerHolder {
		private static final Logger INSTANCE = new Logger();
	}

	public static Logger getInstance() {
		return LoggerHolder.INSTANCE;
	}

	public static void setLogFile(File f) {
		logFileStatic = f;
	}

	public void debug(String toLog, long currentTime, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.DEBUG, currentTime, who);
		addToLog(lls);
	}

	public void error(String toLog, long currentTime, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.ERROR, currentTime, who);
		addToLog(lls);
	}

	public void message(String toLog, long currentTime, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.MESSAGE, currentTime, who);
		addToLog(lls);
	}

	private synchronized void addToLog(LogLineStorage lls) {
		writeBuffer.add(lls);
	}

	// private void writeToLog(String toLog,LogLevel lvl, long
	// currentTime,String who){
	// if(logFile == null)
	// return;
	//
	// Date time = new Date(currentTime);
	//
	// String output = "["+time+"] "+who+" "+lvl +" # "+toLog;
	//
	// try {
	// FileUtil.writeToFile(output, logFile);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	// Log helper classes
	public List<LogLineStorage> getWriteBuffer() {
		return writeBuffer;
	}

}
