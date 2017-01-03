package test;

import java.io.File;
import java.io.IOException;

import log.LogWriter;
import log.Logger;
import util.FileUtil;

public class TestProgram {

	static Logger log; 
	
	public static void main(String[] args)
	{
		startLogger();
		fileSetup();
		
		new TestChanelHandler().runTest();
	}
	
	private static void startLogger() {
		LogWriter.setAppend(false);
		LogWriter.setLogFile(new File("./files/log.txt"));
		LogWriter lw = LogWriter.getInstance();
		if (lw != null)
			new Thread(lw).start();
		log = Logger.getLogger("theLog");
	}
	
	private static void fileSetup()
	{
		for(File f : new File("./files/127.0.0.1/testCom/Output/Sent/").listFiles())
		{
			f.delete();
		}
		try {
			FileUtil.writeToFile("TestData",new File("./files/127.0.0.1/testCom/Output/Send/TestFile.txt" ));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
