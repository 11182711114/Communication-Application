package Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	Socket iSocket;
	
	public Connection(InetAddress adr,int port) throws IOException {
		iSocket = new Socket(adr,port);
		iSocket.bind(null);
		iSocket.close();
	}

	public void connect() {
		// TODO Auto-generated method stub
		
	}

	public void send(String string) {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}
}
