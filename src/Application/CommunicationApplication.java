package Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommunicationApplication {
	
	private static final String[] OPTIONS = {"-IP", "-m"};
		// "-m X" = X manual message to send, assumes -IP
		// "-IP X:Y" = X IP, Y port to connect to in manual mode, assumes -m mode
		
	public static void main(String[] args) {
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
				}
			
			}
		}
	}
		private static String manualConnect(InetAddress adr, int port) {
			String output = null;
			
			try {
				Connection manualCon = new Connection(adr, port);
				manualCon.connect();
				manualCon.send("TESTTEST");
				manualCon.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return output;
		}

}
