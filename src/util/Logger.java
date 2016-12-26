package util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

	public void info(String toLog, String who) {
		LogLineStorage lls = new LogLineStorage(toLog, LogLevel.INFO, System.currentTimeMillis(), who);
		addToLog(lls);
	}
	public void exception(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			LogLineStorage lls = new LogLineStorage(exceptionAsString,LogLevel.STACKTRACE);
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
	private enum LogLevel {
		// Severity lowest down to highest
		ERROR, INFO, DEBUG, STACKTRACE,
	}
	private class LogLineStorage {
		private String toLog;
		private LogLevel lvl;
		private long time;
		private String who;

		public LogLineStorage(String toLog, LogLevel lvl, long time, String who) {
			this.toLog = toLog;
			this.lvl = lvl;
			this.time = time;
			this.who = who;
		}

		public LogLineStorage(String exceptionAsString, LogLevel stacktrace) {
			this.toLog = exceptionAsString;
			this.lvl = stacktrace;
		}

		public String toWrite() {
			if(this.lvl == LogLevel.STACKTRACE){
				return toLog;
			}
			else{
				DateFormat df = new SimpleDateFormat("y-M-d HH:mm:ss.S");
				String timeOutput = df.format(time);
				return "[" + timeOutput + "] " + lvl + "\t" + who + " :: " + toLog;
			}
		}
	}

	

}
