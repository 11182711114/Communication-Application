package Application;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.LinkedList;

public class CommunicationApplication {
//	TESTING STUFF
//	private static int MODE = -1;
//	private static final String TEST_STRING = "TEST TEST TEST";
	
	//PROGRAM OPTIONS - DEFAULT VALUES
	private int listenPort = 10231; // "-port X"
	private boolean continuous = false; // "-c" = true
	private File logLocation = new File("./db/");
	private String logName = "ComApp.log";

	private ChannelHandler cH;

	private Util.Logger log;
	private String nameForLog = this.getClass().getSimpleName();

	public static void main(String[] args) {
		CommunicationApplication app = new CommunicationApplication();
		app.initialize(args);
		app.start();
	}

	public CommunicationApplication() {
	}

	private void initialize(String[] args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {

				case "-c":
					continuous = true;
					break;
					
				case "-logLoc":
					logLocation = new File(args[i+1]);
					break;
					
				case "-logName":
					logName = args[i+1];
					break;
					
				case "-log":
					String str = args[i+1];
					int pathNameSep = 0;
					
					if(str.contains("/"))
						pathNameSep = str.lastIndexOf("/");
					else
						pathNameSep = str.lastIndexOf("\\");
					
					String loc = str.substring(0, pathNameSep+1);
					logLocation = new File(loc);
					logName = str.substring(pathNameSep);
					break;					
					
				case "-port":
					listenPort = Integer.parseInt(args[i+1]);
					break;

//					old manual stuff
//					case "-IP":
//						try {
//							manualConnect(InetAddress.getByName(args[i + 1]), Integer.parseInt(args[i + 2]));
//						} catch (NumberFormatException e) {
//							e.printStackTrace();
//						} catch (UnknownHostException e) {
//							e.printStackTrace();
//						}
//						break;
	//
//					case "-server":
//						manualListen(Integer.parseInt(args[i + 1]));
//						break;
				}
			}
		}
		//No arguments supplied? Nothing to do, exit
		else{
			System.out.println("No arguments supplied, exiting");
			System.exit(0);
		}
		startLogger();
	}
	private void start() {
		log.info("Starting the program", nameForLog);
		if (continuous) {
			log.info("Starting continuous operation", nameForLog);
			startContinuousOperation();
		}
	}
	private void startLogger(){
		Util.Logger.setLogFile(new File(logLocation.getPath()+File.separator+logName));
		log = Util.Logger.getInstance();
		log.start();		
	}
	private void startContinuousOperation() {
		cH = new ChannelHandler(new HashSet<Channel>(), new LinkedList<Channel>());
		try {
			cH.setPortListener(new PortListener(cH, new ServerSocket(listenPort)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cH.start();
	}
/*
	Manual stuff, testing/debugging

	private void manualListen(int port) {
		ManualConnection manualCon;
		try {
			manualCon = new ManualConnection(new ServerSocket(port));
			manualCon.listenServerSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String manualConnect(InetAddress adr, int port) {
		String output = null;

		ManualConnection manualCon;
		try {
			manualCon = new ManualConnection(new Socket(adr, port));
			manualCon.connect();
			if (MODE == -1)
				manualCon.send(TEST_STRING);
			manualCon.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}
*/
}
