package application;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import discovery.Discovery;
import discovery.RoutingTable;
import interDeviceCommunication.Channel;
import interDeviceCommunication.PortListener;

public class CommunicationApplication {
//	TESTING STUFF
//	private static int MODE = -1;
//	private static final String TEST_STRING = "TEST TEST TEST";
	
	//PROGRAM OPTIONS - DEFAULT VALUES
	private int listenPort = 10231; // "-port X"
	private boolean continuous = false; // "-c" = true
	private File logDir = new File("./db/");
	private File monitorDir = new File("./monitor/");
	private String logName = "ComApp.log";
	private boolean doDisc = false;
	private String network = "192.168.1.*";

	private ChannelHandler cH;

	private util.Logger log;
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
					
				case "-log":
					logDir = new File(args[i+1]);
					break;				
					
				case "-port":
					listenPort = Integer.parseInt(args[i+1]);
					break;
					
				case "-mon":
					monitorDir = new File(args[i+1]);
					if(!monitorDir.exists())
						monitorDir.mkdirs();
					break;
				case "-disc":
					doDisc = true;
					break;
				case "-network":
					network = args[i+1];
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
		util.Logger.setLogFile(new File(logDir.getPath()+File.separator+logName));
		log = util.Logger.getInstance();
		log.start();		
	}
	private void startContinuousOperation() {
		if(doDisc){
			cH = new ChannelHandler(
					new HashSet<Channel>(),
					new LinkedList<Channel>(),
					monitorDir,
					new Discovery(
							new RoutingTable(
									new ArrayList<Device>())));
			try {
				cH.setPortListener(new PortListener(cH, new ServerSocket(listenPort)));
			} catch (IOException e) {
				log.exception(e);
			}
			cH.start();			
		}
		else{
			
			cH = new ChannelHandler(new HashSet<Channel>(), new LinkedList<Channel>(),monitorDir);
			try {
				cH.setPortListener(new PortListener(cH, new ServerSocket(listenPort)));
			} catch (IOException e) {
				log.exception(e);
			}
			cH.start();
		}
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
