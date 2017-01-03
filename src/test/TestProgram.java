package test;

import java.io.File;

import log.LogWriter;
import log.Logger;

public class TestProgram {

	static Logger log; 
	
	public static void main(String[] args)
	{
		startLogger();
		
		new TestChanelHandler().runTest();
	}
	
	private static void startLogger() {
		LogWriter.setLogFile(new File("./files/log.txt"));
		LogWriter lw = LogWriter.getInstance();
		if (lw != null)
			new Thread(lw).start();
		log = Logger.getLogger("theLog");
	}

}
