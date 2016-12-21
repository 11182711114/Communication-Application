package Util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Logger {
	private File logFile;
	
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
	
	public void Log(String toLog,LogLevel lvl, int currentTime){
		if(!logFile.exists())
			return;
		
		Date time = new Date(currentTime);
		
		String output = "[" + time + "] " + lvl + " | " + toLog;
		
		try {
			FileUtil.writeToFile(output, logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
		
	
}
