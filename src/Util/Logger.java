package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {
	private File logFile;
	
	
	private List<LogLineStorage> writeBuffer = new ArrayList<>();

	private static File logFileStatic;
	
	private Logger(){
		logFile = logFileStatic;
	}
	
	private static class LoggerHolder{
		private static final Logger INSTANCE = new Logger();
	}
	public static Logger getInstance(){
		return LoggerHolder.INSTANCE;
	}
	public static void setLogFile(File f){
		logFileStatic = f;
	}
	
	public synchronized void Log(String toLog,LogLevel lvl, long currentTime,String who){
		LogLineStorage lls = new LogLineStorage(toLog,lvl,currentTime,who);
		writeBuffer.add(lls);
	}
	private void writeToLog(String toLog,LogLevel lvl, long currentTime,String who){
		if(logFile == null)
			return;
		
		Date time = new Date(currentTime);
		
		String output = "["+time+"] "+who+" "+lvl +" # "+toLog;
		
		try {
			FileUtil.writeToFile(output, logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private class LogLineStorage{
		private String toLog;
		private LogLevel lvl;
		private long time;
		private String who;
		
		public LogLineStorage(String toLog, LogLevel lvl, long time,String who){
			this.toLog = toLog;
			this.lvl = lvl;
			this.time = time;
			this.who = who;
		}
		
		public String getToLog(){
			return toLog;
		}
		public LogLevel getLvl(){
			return lvl;
		}
		public long getTime(){
			return time;
		}
		public String getWho(){
			return who;
		}
	}
	
}
