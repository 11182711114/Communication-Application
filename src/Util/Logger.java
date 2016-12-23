package Util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {
	private static final long LOG_WRITER_TIME_TO_WAIT = 5 * 1000;

	private Queue<LogLineStorage> writeBuffer = new ConcurrentLinkedQueue<>();

	private LogWriter lw;

	private static File logFileStatic;

	private Logger() {
		lw = new LogWriter(logFileStatic, LOG_WRITER_TIME_TO_WAIT, this);
	}

	public void start() {
		new Thread(lw).start();
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

	public void debug(String toLog, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.DEBUG, System.currentTimeMillis(), who);
		addToLog(lls);
	}

	public void error(String toLog, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.ERROR, System.currentTimeMillis(), who);
		addToLog(lls);
	}

	public void message(String toLog, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.MESSAGE, System.currentTimeMillis(), who);
		addToLog(lls);
	}

	private void addToLog(LogLineStorage lls) {
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
	public Queue<LogLineStorage> getWriteBuffer() {
		return writeBuffer;
	}

}
