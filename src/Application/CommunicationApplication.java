package Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommunicationApplication {
	
	private static final String[] OPTIONS = {"-IP", "-m"};
		// "-m X" = X manual message to send, assumes -IP
		// "-IP X:Y" = X IP, Y port to connect to in manual mode, assumes -m mode
		// "-server X" = app acts as server listening on port X
	private String[] args;
	
	public static void main(String[] args) {
		CommunicationApplication app = new CommunicationApplication(args);
		app.run();
	}
	public CommunicationApplication(String[] args){
		this.args = args;
	}
	private void run(){
		if(args.length>0){
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
				}
			
			}
		}
	}
	private void manualListen(int port) {
		Connection manualCon = new Connection(port);
		manualCon.listenServerSocket();		
	}
	private String manualConnect(InetAddress adr, int port) {
		String output = null;
		
		Connection manualCon;
		try {
			manualCon = new Connection(new Socket(adr, port));
			manualCon.connect();
			manualCon.send("TESTTEST");
			manualCon.close();
		} catch (IOException e) {e.printStackTrace();}
		
		return output;
	}
	

}
