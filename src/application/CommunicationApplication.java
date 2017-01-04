package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import discovery.Discovery;
import discovery.RoutingTable;
import interDeviceCommunication.Channel;
import interDeviceCommunication.Device;
import interDeviceCommunication.PortListener;
import ipc.StatusMonitor;
import log.LogWriter;
import log.Logger;

public class CommunicationApplication {
	private static final long STATUS_CHECK_INTERVAL = 5000;
	
	// PROGRAM OPTIONS - DEFAULT VALUES
	private int listenPort = 10231; // "-port X"
	private boolean continuous = false; // "-c" = true
	private File logFile = new File("./db/ComApp.log");
	private File monitorDir = new File("./monitor/");
	private boolean doDisc = false;
	private File discoveryOutput = new File("./hosts/ComApp.hosts");
	private String network = "192.168.1.*";
	private File configFile = new File("./ComApp.conf");
	private String deviceId;
	private boolean logAppend = true;
	private File statusFile = new File("./status");
	private String status = StatusMonitor.INACTIVE_CODE; //default inactive
	
	private boolean changedStatus = true;
	
	private ChannelHandler cH;

	private Logger log;

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
					File logDirTMP = new File(args[i + 1] + File.separator + logFile.getName());
					logFile = logDirTMP;
					break;

				case "-port":
					listenPort = Integer.parseInt(args[i + 1]);
					break;

				case "-mon":
					monitorDir = new File(args[i + 1]);
					if (!monitorDir.exists())
						monitorDir.mkdirs();
					break;

				case "-disc":
					doDisc = true;
					File discOutTmp = new File(args[i + 1] + File.separator + discoveryOutput.getName());
					discoveryOutput = discOutTmp;
					if (!discoveryOutput.exists())
						discoveryOutput.mkdirs();
					break;

				case "-network":
					network = args[i + 1];
					break;

				case "-L":
					logAppend = Boolean.parseBoolean(args[i + 1]);
					break;
				case "-S":
					statusFile = new File(args[i + 1]);
					new Thread(new StatusMonitor(statusFile,this)).start();
					break;
				default:
					break;
				}
			}
		}
		// No arguments supplied? Nothing to do, exit
		else {
			System.out.println("No arguments supplied, exiting");
			System.exit(0);
		}
		startLogger();
		// Try to load config file
		try {
			log.info("Loading config file");
			String[] configFileOutput = util.FileUtil.readFromFile(configFile);
			Map<String, String> config = new HashMap<String, String>();
			for (String s : configFileOutput) {
				log.trace("Trying to parse config string: " + s);
				String[] keyValue = s.split("=");
				if (keyValue.length == 2 && (!keyValue[0].isEmpty() || !keyValue[1].isEmpty()))
					config.put(keyValue[0], keyValue[1]);
			}
			deviceId = config.get("deviceId");
			log.debug("Setting deviceId: " + deviceId);

		} catch (FileNotFoundException e) {
			log.error("No config file found");
		}
	}

	private void start() {
		log.info("Starting the program");
		if (continuous) {
			log.info("Starting continuous operation");
			startContinuousOperation();
		}
	}

	private void startLogger() {
		LogWriter.setLogFile(logFile);
		LogWriter.setAppend(logAppend);
		LogWriter lw = LogWriter.getInstance();
		if (lw != null)
			new Thread(lw).start();
		log = Logger.getLogger(this.getClass().getSimpleName());
	}

	private void startContinuousOperation() {
		do{
			if(changedStatus){
				log.info("New status: " + status);
				if(status == StatusMonitor.ACTIVE_CODE){
					log.debug("Program is active");
					if (doDisc) {
						log.debug(
								"Making ChannelHandler with monitorDir: " +	monitorDir + 
								" network: " + 	network + 
								" discoveryOutput: " + discoveryOutput
						);
						cH = new ChannelHandler(
								new LinkedList<Channel>(),
								monitorDir,
								new Discovery(
										new RoutingTable(new ArrayList<Device>()
								),
								network,
								discoveryOutput)
						);
			
						try {
							cH.setPortListener(new PortListener(cH, new ServerSocket(listenPort)));
						} catch (IOException e) {
							log.exception(e);
						}
						new Thread(cH).start();
					} else {
						log.debug(
								"Making ChannelHandler with monitorDir: " + monitorDir + 
								" network: " + network
						);
						cH = new ChannelHandler(new LinkedList<Channel>(), monitorDir);
			
						try {
							cH.setPortListener(new PortListener(cH, new ServerSocket(listenPort)));
						} catch (IOException e) {
							log.exception(e);
						}
						new Thread(cH).start();
					}
				} else if (status == StatusMonitor.INACTIVE_CODE){
					log.debug("Program is inactive");
					stopChannelHandler();
				} else if (status == StatusMonitor.SHUTDOWN_CODE){
					shutdown();
				}
				changedStatus = false;
			}
			try {
				Thread.sleep(STATUS_CHECK_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(true);
	}
	private void shutdown(){
		log.info("Shutting down");
		stopChannelHandler();
		System.exit(0);
		
	}
	private void stopChannelHandler(){
		if(cH != null && cH.getActive() == true){
			log.debug("ChannelHandler is active, shutting it down");
			cH.stop();
		}
	}
	public void setStatus(String newStatus){
		this.status = newStatus;		
	}

	public String getStatus() {
		return status;
	}
	public void setChangedStatus(boolean bol){
		changedStatus = bol;
	}
}
