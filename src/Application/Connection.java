package Application;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	Socket iSocket;
	
	public Connection(byte[] adr) throws IOException {
		InetAddress ip = InetAddress.getByAddress(adr);
		iSocket = new Socket(ip,8080);
		iSocket.bind(null);
		iSocket.close();
	}
}
