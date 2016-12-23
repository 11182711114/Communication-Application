package Application;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import Util.LogLevel;

public class CommunicationApplication {

	private static final int MODE = -1;
	private static final String TEST_STRING = "TEST TEST TEST";
	private static final String[] OPTIONS = { "-IP", "-m", "-c" };
	// "-m X" = X manual message to send, assumes -IP
	// "-IP X:Y" = X IP, Y port to connect to in manual mode, assumes -m mode
	// "-server X" = app acts as server listening on port X
	// "-c" = continuous mode

	private ChannelHandler cH;
	private int listenPort = 10231; // default port

	private Util.Logger log;
	private String className = this.getClass().getSimpleName();

	private boolean continuous = false;

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

				case "-IP":
					try {
						manualConnect(InetAddress.getByName(args[i + 1]), Integer.parseInt(args[i + 2]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					break;

				case "-server":
					manualListen(Integer.parseInt(args[i + 1]));
					break;

				case "-c":
					continuous = true;
					break;
				case "-log":
					Util.Logger.setLogFile(new File(args[i + 1]));
					break;
				}
			}
		}
		log = Util.Logger.getInstance();
	}

	private void start() {
		log.debug("Starting the program", System.currentTimeMillis(), className);
		if (continuous) {
			log.debug("Starting continuous operation", System.currentTimeMillis(), className);
			startContinuousOperation();
		}
	}

	private void startContinuousOperation() {
		cH = new ChannelHandler(new HashSet<Channel>(), new ArrayList<Channel>());
		try {
			cH.setPortListener(new PortListener(cH, new ServerSocket(listenPort)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cH.start();
	}

	// Manual stuff, testing/debugging

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

}
