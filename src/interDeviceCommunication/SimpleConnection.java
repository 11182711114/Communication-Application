package interDeviceCommunication;

import java.net.Socket;
import java.util.List;

public class SimpleConnection implements Runnable{
	
	private Socket socket;
	private List<String> received;
	private List<String> toSend;
	private boolean active = false;
	
	public SimpleConnection(Socket s, List<String> received, List<String> toSend){
		this.socket = s;
		this.received = received;
		this.toSend = toSend;
	}
	
	@Override
	public void run() {
				
	}
	
	public List<String> getReceived(){
		return received;
	}
	

}
