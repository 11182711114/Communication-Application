package Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

public class CommunicationApplication {
	
	private static final int MODE = -1;
	private static final String TEST_STRING= "TEST TEST TEST";
	private static final String[] OPTIONS = {"-IP", "-m", "-c"};
		// "-m X" = X manual message to send, assumes -IP
		// "-IP X:Y" = X IP, Y port to connect to in manual mode, assumes -m mode
		// "-server X" = app acts as server listening on port X
		// "-c" = continuous mode
	private String[] args;
	
	private ChannelHandler cH;
	private int listenPort = 8080; //default port
	
	public static void main(String[] args) {
		CommunicationApplication app = new CommunicationApplication(args);
		app.run();
	}
	public CommunicationApplication(String[] args){
		this.args = args;
		cH = new ChannelHandler(new HashSet<Channel>(), new ArrayList<Channel>());
		try {
			cH.setPortListener(new PortListener(cH,new ServerSocket(listenPort)));
		} catch (IOException e) {e.printStackTrace();}
	}
	private void run(){
		if(args.length>0){
			boolean continuous = false;
			for(int i = 0; i<args.length;i++){
				switch(args[i]){
				case "-IP":
					try {
						manualConnect(InetAddress.getByName(args[i+1]),Integer.parseInt(args[i+2]));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					break;
				case "-server":
					manualListen(Integer.parseInt(args[i+1]));
					break;
				case "-c":
					continuous = true;
				}
			}
			startContinuousOp();
		}
	}
	private void startContinuousOp(){
		while(true){
			
		}
	}
	
	
	
	
	
	
	
	
	//Manual stuff, testing/debugging
	
	private void manualListen(int port) {
		Connection manualCon;
		try {
			manualCon = new Connection(new ServerSocket(port));
			manualCon.listenServerSocket();		
		} catch (IOException e) {e.printStackTrace();}
	}
	private String manualConnect(InetAddress adr, int port) {
		String output = null;
		
		Connection manualCon;
		try {
			manualCon = new Connection(new Socket(adr, port));
			manualCon.connect();
			if(MODE == -1)
				manualCon.send(TEST_STRING);
			manualCon.close();
		} catch (IOException e) {e.printStackTrace();}
		
		return output;
	}
	

}
